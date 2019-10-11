package de.codescape.jira.plugins.scrumpoker.service;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerSession;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerVote;
import de.codescape.jira.plugins.scrumpoker.condition.ScrumPokerForIssueCondition;
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
    private final ScrumPokerSettingService scrumPokerSettingService;
    private final ScrumPokerForIssueCondition scrumPokerForIssueCondition;
    private final ScrumPokerErrorService scrumPokerErrorService;

    @Autowired
    public ScrumPokerSessionServiceImpl(ActiveObjects activeObjects,
                                        @ComponentImport IssueManager issueManager,
                                        ScrumPokerSettingService scrumPokerSettingService,
                                        ScrumPokerForIssueCondition scrumPokerForIssueCondition,
                                        ScrumPokerErrorService scrumPokerErrorService) {
        this.activeObjects = activeObjects;
        this.issueManager = issueManager;
        this.scrumPokerSettingService = scrumPokerSettingService;
        this.scrumPokerForIssueCondition = scrumPokerForIssueCondition;
        this.scrumPokerErrorService = scrumPokerErrorService;
    }

    @Override
    public List<ScrumPokerSession> recent() {
        Date date = new Date(System.currentTimeMillis() - sessionTimeoutInMillis());
        return Arrays.asList(activeObjects.find(ScrumPokerSession.class, Query.select()
            .where("CREATE_DATE > ?", date)
            .order("CREATE_DATE DESC")));
    }

    private int sessionTimeoutInMillis() {
        return 3600 * 1000 * scrumPokerSettingService.load().getSessionTimeout();
    }

    @Override
    public ScrumPokerSession byIssueKey(String issueKey, String userKey) {
        ScrumPokerSession scrumPokerSession = activeObjects.get(ScrumPokerSession.class, issueKey);
        if (scrumPokerSession == null) {
            scrumPokerSession = create(issueKey, userKey);
        }
        return scrumPokerSession;
    }

    @Override
    public ScrumPokerSession addVote(String issueKey, String userKey, String vote) {
        ScrumPokerSession scrumPokerSession = byIssueKey(issueKey, userKey);
        scrumPokerSession.setVisible(false);
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
        scrumPokerSession.save();
        return scrumPokerSession;
    }

    @Override
    public ScrumPokerSession confirm(String issueKey, String userKey, Integer estimation) {
        ScrumPokerSession scrumPokerSession = byIssueKey(issueKey, userKey);
        scrumPokerSession.setConfirmedVote(estimation);
        scrumPokerSession.setConfirmedUserKey(userKey);
        scrumPokerSession.setConfirmedDate(new Date());
        scrumPokerSession.save();
        return scrumPokerSession;
    }

    @Override
    public ScrumPokerSession reset(String issueKey, String userKey) {
        ScrumPokerVote[] votes = activeObjects.find(ScrumPokerVote.class, Query.select()
            .where("SESSION_ID = ?", issueKey));
        Arrays.stream(votes).forEach(activeObjects::delete);
        activeObjects.delete(byIssueKey(issueKey, userKey));
        return byIssueKey(issueKey, userKey);
    }

    @Override
    public ScrumPokerSession cancel(String issueKey, String userKey) {
        ScrumPokerSession scrumPokerSession = byIssueKey(issueKey, userKey);
        if (scrumPokerSession.getCreatorUserKey().equals(userKey)) {
            scrumPokerSession.setCancelled(true);
        }
        scrumPokerSession.save();
        return scrumPokerSession;
    }

    @Override
    public List<ScrumPokerSession> references(String userKey, Integer estimation) {
        return Arrays.asList(activeObjects.find(ScrumPokerSession.class, Query.select()
            .alias(ScrumPokerSession.class, "sps")
            .alias(ScrumPokerVote.class, "spv")
            .join(ScrumPokerVote.class, "spv.SESSION_ID = sps.ISSUE_KEY")
            .where("spv.USER_KEY = ? and sps.CONFIRMED_VOTE = ?", userKey, estimation)
            .order("sps.CREATE_DATE DESC")
            .limit(3)));
    }

    private ScrumPokerSession create(String issueKey, String userKey) {
        MutableIssue issue = issueManager.getIssueObject(issueKey);
        if (issue == null) {
            String message = "Unable to create session for non-existing issue " + issueKey + ".";
            scrumPokerErrorService.logError(message, null);
            throw new IllegalStateException(message);
        }
        if (!scrumPokerForIssueCondition.isEstimable(issue)) {
            String message = "Unable to create session for non-estimable issue " + issueKey + ".";
            scrumPokerErrorService.logError(message, null);
            throw new IllegalStateException(message);
        }
        ScrumPokerSession scrumPokerSession = activeObjects.create(ScrumPokerSession.class,
            new DBParam("ISSUE_KEY", issueKey));
        scrumPokerSession.setCreateDate(new Date());
        scrumPokerSession.setCreatorUserKey(userKey);
        scrumPokerSession.setVisible(false);
        scrumPokerSession.setConfirmedVote(null);
        scrumPokerSession.save();
        return scrumPokerSession;
    }

}
