package de.codescape.jira.plugins.scrumpoker.service;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerSession;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerVote;
import net.java.ao.DBParam;
import net.java.ao.Query;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

@Scanned
@Named
public class DefaultScrumPokerSessionService implements ScrumPokerSessionService {

    private static final int POKER_SESSION_TIMEOUT_HOURS = 12;

    @ComponentImport
    private final ActiveObjects ao;

    @Inject
    public DefaultScrumPokerSessionService(ActiveObjects ao) {
        this.ao = checkNotNull(ao);
    }

    @Override
    public List<ScrumPokerSession> recent() {
        Date date = new Date(System.currentTimeMillis() - 3600 * 1000 * POKER_SESSION_TIMEOUT_HOURS);
        return Arrays.asList(ao.find(ScrumPokerSession.class,
            Query.select().where("create_date > ?", date).order("create_date DESC")));
    }

    @Override
    public ScrumPokerSession byIssueKey(String issueKey, String userKey) {
        ScrumPokerSession scrumPokerSession = ao.get(ScrumPokerSession.class, issueKey);
        if (scrumPokerSession == null)
            scrumPokerSession = create(issueKey, userKey);
        return scrumPokerSession;
    }

    @Override
    public ScrumPokerSession addVote(String issueKey, String userKey, String vote) {
        ScrumPokerSession scrumPokerSession = byIssueKey(issueKey, userKey);
        scrumPokerSession.setVisible(false);
        scrumPokerSession.save();

        ScrumPokerVote[] scrumPokerVotes = ao.find(ScrumPokerVote.class,
            Query.select().where("session_id = ? and user_key = ?", issueKey, userKey));
        ScrumPokerVote scrumPokerVote;
        if (scrumPokerVotes.length == 0) {
            scrumPokerVote = ao.create(ScrumPokerVote.class);
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
        scrumPokerSession.save();
        return scrumPokerSession;
    }

    @Override
    public ScrumPokerSession reset(String issueKey, String userKey) {
        ScrumPokerVote[] votes = ao.find(ScrumPokerVote.class, Query.select().where("session_id = ?", issueKey));
        Arrays.stream(votes).forEach(ao::delete);
        ao.delete(byIssueKey(issueKey, userKey));
        return byIssueKey(issueKey, userKey);
    }

    private ScrumPokerSession create(String issueKey, String userKey) {
        ScrumPokerSession scrumPokerSession = ao.create(ScrumPokerSession.class,
            new DBParam("ISSUE_KEY", issueKey));
        scrumPokerSession.setCreateDate(new Date());
        scrumPokerSession.setCreatorUserKey(userKey);
        scrumPokerSession.setVisible(false);
        scrumPokerSession.setConfirmedVote(null);
        scrumPokerSession.save();
        return scrumPokerSession;
    }

}
