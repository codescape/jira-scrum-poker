package de.codescape.jira.plugins.scrumpoker.action;

import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.permission.ProjectPermissions;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.user.util.UserManager;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import de.codescape.jira.plugins.scrumpoker.model.ScrumPokerSession;
import de.codescape.jira.plugins.scrumpoker.persistence.ScrumPokerStorage;

import java.util.List;
import java.util.stream.Collectors;

import static com.atlassian.jira.permission.ProjectPermissions.BROWSE_PROJECTS;

/**
 * Show a list of all running and recently finished Scrum poker sessions.
 */
public class ShowSessionsAction extends JiraWebActionSupport {

    private static final long serialVersionUID = 1L;

    private final ScrumPokerStorage scrumPokerStorage;
    private final UserManager userManager;
    private final IssueManager issueManager;
    private final PermissionManager permissionManager;
    private final JiraAuthenticationContext jiraAuthenticationContext;

    public ShowSessionsAction(ScrumPokerStorage scrumPokerStorage, JiraAuthenticationContext jiraAuthenticationContext,
                              UserManager userManager, IssueManager issueManager, PermissionManager permissionManager) {
        this.scrumPokerStorage = scrumPokerStorage;
        this.userManager = userManager;
        this.issueManager = issueManager;
        this.permissionManager = permissionManager;
        this.jiraAuthenticationContext = jiraAuthenticationContext;
    }

    @Override
    protected String doExecute() {
        return "success";
    }

    public List<ScrumPokerSession> getOpenSessions() {
        return scrumPokerStorage.getOpenSessions().stream()
            .filter(session -> currentUserIsAllowedToSeeIssue(getIssue(session.getIssueKey())))
            .collect(Collectors.toList());
    }

    public List<ScrumPokerSession> getClosedSessions() {
        return scrumPokerStorage.getClosedSessions().stream()
            .filter(session -> currentUserIsAllowedToSeeIssue(getIssue(session.getIssueKey())))
            .collect(Collectors.toList());
    }

    public String getUsername(String key) {
        ApplicationUser user = userManager.getUserByKey(key);
        return user != null ? user.getDisplayName() : key;
    }

    public MutableIssue getIssue(String issueKey) {
        return issueManager.getIssueObject(issueKey);
    }

    private boolean currentUserIsAllowedToSeeIssue(MutableIssue issue) {
        return permissionManager.hasPermission(BROWSE_PROJECTS, issue, jiraAuthenticationContext.getLoggedInUser());
    }

}
