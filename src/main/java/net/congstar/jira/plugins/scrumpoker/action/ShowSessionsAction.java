package net.congstar.jira.plugins.scrumpoker.action;

import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.permission.ProjectPermissions;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.jira.user.util.UserManager;
import net.congstar.jira.plugins.scrumpoker.data.ScrumPokerStorage;
import net.congstar.jira.plugins.scrumpoker.model.ScrumPokerSession;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Show a list of all running and recently finished Scrum poker sessions.
 */
public class ShowSessionsAction extends ScrumPokerAction {

    private static final long serialVersionUID = 1L;

    private final ScrumPokerStorage scrumPokerStorage;
    private final UserManager userManager;
    private final IssueManager issueManager;
    private final PermissionManager permissionManager;
    private final JiraAuthenticationContext jiraAuthenticationContext;

    public ShowSessionsAction(ScrumPokerStorage scrumPokerStorage, UserManager userManager, IssueManager issueManager,
                              PermissionManager permissionManager, JiraAuthenticationContext jiraAuthenticationContext) {
        this.scrumPokerStorage = scrumPokerStorage;
        this.userManager = userManager;
        this.issueManager = issueManager;
        this.permissionManager = permissionManager;
        this.jiraAuthenticationContext = jiraAuthenticationContext;
    }

    @Override
    protected String doExecute() throws Exception {
        return "start";
    }

    public List<ScrumPokerSession> getOpenSessions() {
        return scrumPokerStorage.getOpenSessions().stream()
                .filter(session -> permissionManager.hasPermission(ProjectPermissions.BROWSE_PROJECTS,
                        issueManager.getIssueObject(session.getIssueKey()), jiraAuthenticationContext.getLoggedInUser()))
                .collect(Collectors.toList());
    }

    public List<ScrumPokerSession> getClosedSessions() {
        return scrumPokerStorage.getClosedSessions().stream()
                .filter(session -> permissionManager.hasPermission(ProjectPermissions.BROWSE_PROJECTS,
                        issueManager.getIssueObject(session.getIssueKey()), jiraAuthenticationContext.getLoggedInUser()))
                .collect(Collectors.toList());
    }

    public String getUsername(String key) {
        return userManager.getUserByKey(key).getDisplayName();
    }

    public MutableIssue getIssue(String issueKey) {
        return issueManager.getIssueObject(issueKey);
    }

}