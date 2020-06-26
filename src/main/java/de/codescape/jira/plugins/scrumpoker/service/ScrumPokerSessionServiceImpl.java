package de.codescape.jira.plugins.scrumpoker.service;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerSession;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerVote;
import net.java.ao.DBParam;
import net.java.ao.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Implementation of {@link ScrumPokerSessionService} using Active Objects as persistence model.
 */
@Component
public class ScrumPokerSessionServiceImpl implements ScrumPokerSessionService {

    private final ActiveObjects activeObjects;
    private final IssueManager issueManager;
    private final GlobalSettingsService globalSettingsService;
    private final EstimateFieldService estimateFieldService;
    private final ErrorLogService errorLogService;

    @Autowired
    public ScrumPokerSessionServiceImpl(@ComponentImport ActiveObjects activeObjects,
                                        @ComponentImport IssueManager issueManager,
                                        GlobalSettingsService globalSettingsService,
                                        EstimateFieldService estimateFieldService,
                                        ErrorLogService errorLogService) {
        this.activeObjects = activeObjects;
        this.issueManager = issueManager;
        this.globalSettingsService = globalSettingsService;
        this.estimateFieldService = estimateFieldService;
        this.errorLogService = errorLogService;
    }

    @Override
    public List<ScrumPokerSession> recent() {
        return Arrays.asList(activeObjects.find(ScrumPokerSession.class, Query.select()
            .where("UPDATE_DATE > ?", sessionTimeout())
            .order("CREATE_DATE DESC")));
    }

    @Override
    public ScrumPokerSession byIssueKey(String issueKey, String userKey) {
        ScrumPokerSession scrumPokerSession = findScrumPokerSession(issueKey);
        if (scrumPokerSession == null) {
            return createScrumPokerSession(issueKey, userKey);
        }
        if (scrumPokerSession.getUpdateDate().before(sessionTimeout())) {
            deleteScrumPokerSession(issueKey);
            return createScrumPokerSession(issueKey, userKey);
        }
        return scrumPokerSession;
    }

    @Override
    public ScrumPokerSession addVote(String issueKey, String userKey, String vote) {
        ScrumPokerSession scrumPokerSession = byIssueKey(issueKey, userKey);
        scrumPokerSession.setVisible(false);
        scrumPokerSession.setUpdateDate(new Date());
        scrumPokerSession.save();
        ScrumPokerVote[] scrumPokerVotes = activeObjects.find(ScrumPokerVote.class, Query.select()
            .where("SESSION_ID = ? and USER_KEY = ?", issueKey, userKey));
        ScrumPokerVote scrumPokerVote;
        if (scrumPokerVotes.length == 0) {
            scrumPokerVote = activeObjects.create(ScrumPokerVote.class);
            scrumPokerVote.setSession(scrumPokerSession);
            scrumPokerVote.setUserKey(userKey);
        } else {
            scrumPokerVote = scrumPokerVotes[0];
        }
        scrumPokerVote.setCreateDate(new Date());
        scrumPokerVote.setVote(vote);
        scrumPokerVote.save();
        return scrumPokerSession;
    }

    @Override
    public ScrumPokerSession reveal(String issueKey, String userKey) {
        ScrumPokerSession scrumPokerSession = byIssueKey(issueKey, userKey);
        scrumPokerSession.setVisible(true);
        scrumPokerSession.setUpdateDate(new Date());
        scrumPokerSession.save();
        return scrumPokerSession;
    }

    @Override
    public ScrumPokerSession confirm(String issueKey, String userKey, String estimate) {
        ScrumPokerSession scrumPokerSession = byIssueKey(issueKey, userKey);
        scrumPokerSession.setConfirmedEstimate(estimate);
        scrumPokerSession.setConfirmedUserKey(userKey);
        scrumPokerSession.setConfirmedDate(new Date());
        scrumPokerSession.setUpdateDate(new Date());
        scrumPokerSession.save();
        return scrumPokerSession;
    }

    @Override
    public ScrumPokerSession reset(String issueKey, String userKey) {
        deleteScrumPokerSession(issueKey);
        return byIssueKey(issueKey, userKey);
    }

    @Override
    public ScrumPokerSession cancel(String issueKey, String userKey) {
        ScrumPokerSession scrumPokerSession = byIssueKey(issueKey, userKey);
        if (scrumPokerSession.getCreatorUserKey().equals(userKey)) {
            scrumPokerSession.setCancelled(true);
        }
        scrumPokerSession.setUpdateDate(new Date());
        scrumPokerSession.save();
        return scrumPokerSession;
    }

    @Override
    public List<ScrumPokerSession> references(String userKey, String estimate) {
        return Arrays.asList(activeObjects.find(ScrumPokerSession.class, Query.select()
            .alias(ScrumPokerSession.class, "SPS")
            .alias(ScrumPokerVote.class, "SPV")
            .join(ScrumPokerVote.class, "SPV.SESSION_ID = SPS.ISSUE_KEY")
            .where("SPV.USER_KEY = ? and SPS.CONFIRMED_ESTIMATE = ?", userKey, estimate)
            .order("SPS.CONFIRMED_DATE DESC")
            .limit(3)));
    }

    @Override
    public boolean hasActiveSession(String issueKey) {
        /* a session is defined as an active session if the session exists, the session has not been confirmed yet, the
           session has not been cancelled and the session has not timed out yet */
        ScrumPokerSession scrumPokerSession = findScrumPokerSession(issueKey);
        return scrumPokerSession != null
            && scrumPokerSession.getConfirmedEstimate() == null
            && !scrumPokerSession.isCancelled()
            && !(scrumPokerSession.getUpdateDate().before(sessionTimeout()));
    }

    private Date sessionTimeout() {
        long sessionTimeoutMillis = hoursToMillis(globalSettingsService.load().getSessionTimeout());
        return new Date(System.currentTimeMillis() - sessionTimeoutMillis);
    }

    private static long hoursToMillis(Integer hours) {
        return 3600 * 1000 * hours;
    }

    private ScrumPokerSession createScrumPokerSession(String issueKey, String userKey) {
        MutableIssue issue = issueManager.getIssueObject(issueKey);
        if (issue == null) {
            String message = "Unable to create session for non-existing issue " + issueKey + ".";
            errorLogService.logError(message);
            throw new IllegalStateException(message);
        }
        if (!estimateFieldService.isEstimable(issue)) {
            String message = "Unable to create session for non-estimable issue " + issueKey + ".";
            errorLogService.logError(message);
            throw new IllegalStateException(message);
        }
        ScrumPokerSession scrumPokerSession = activeObjects.create(ScrumPokerSession.class,
            new DBParam("ISSUE_KEY", issueKey));
        scrumPokerSession.setCreateDate(new Date());
        scrumPokerSession.setCreatorUserKey(userKey);
        scrumPokerSession.setVisible(false);
        scrumPokerSession.setConfirmedEstimate(null);
        scrumPokerSession.setCardSet(globalSettingsService.load().getCardSet());
        scrumPokerSession.setUpdateDate(new Date());
        scrumPokerSession.save();
        return scrumPokerSession;
    }

    private void deleteScrumPokerSession(String issueKey) {
        ScrumPokerSession scrumPokerSession = findScrumPokerSession(issueKey);
        Arrays.stream(scrumPokerSession.getVotes()).forEach(activeObjects::delete);
        activeObjects.delete(scrumPokerSession);
    }

    private ScrumPokerSession findScrumPokerSession(String issueKey) {
        return activeObjects.get(ScrumPokerSession.class, issueKey);
    }

}
