package net.congstar.jira.plugins.scrumpoker.action;

import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.user.util.UserManager;
import net.congstar.jira.plugins.scrumpoker.data.ScrumPokerStorage;
import net.congstar.jira.plugins.scrumpoker.model.ScrumPokerSession;

import java.util.List;

public class ShowSessionsAction extends ScrumPokerAction {

    private static final long serialVersionUID = 1L;

    private final ScrumPokerStorage scrumPokerStorage;
    private final UserManager userManager;
    private final IssueManager issueManager;

    public ShowSessionsAction(ScrumPokerStorage scrumPokerStorage, UserManager userManager, IssueManager issueManager) {
        this.scrumPokerStorage = scrumPokerStorage;
        this.userManager = userManager;
        this.issueManager = issueManager;
    }

    @Override
    protected String doExecute() throws Exception {
        return "start";
    }

    public List<ScrumPokerSession> getOpenSessions() {
        return scrumPokerStorage.getOpenSessions();
    }

    public List<ScrumPokerSession> getClosedSessions() {
        return scrumPokerStorage.getClosedSessions();
    }

    public String getUsername(String key) {
        return userManager.getUserByKey(key).getDisplayName();
    }

    public MutableIssue getIssue(String issueKey) {
        return issueManager.getIssueObject(issueKey);
    }

}