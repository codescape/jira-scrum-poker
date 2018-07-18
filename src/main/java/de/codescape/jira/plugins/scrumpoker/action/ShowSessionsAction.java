package de.codescape.jira.plugins.scrumpoker.action;

import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import de.codescape.jira.plugins.scrumpoker.rest.entities.SessionEntity;
import de.codescape.jira.plugins.scrumpoker.service.ScrumPokerSessionService;
import de.codescape.jira.plugins.scrumpoker.service.SessionEntityTransformer;

import java.util.List;
import java.util.stream.Collectors;

import static com.atlassian.jira.permission.ProjectPermissions.BROWSE_PROJECTS;

/**
 * Show a list of all running and recently finished Scrum poker sessions.
 */
public class ShowSessionsAction extends JiraWebActionSupport {

    private static final long serialVersionUID = 1L;

    private final IssueManager issueManager;
    private final PermissionManager permissionManager;
    private final JiraAuthenticationContext jiraAuthenticationContext;
    private final ScrumPokerSessionService scrumPokerSessionService;
    private final SessionEntityTransformer sessionEntityTransformer;

    public ShowSessionsAction(JiraAuthenticationContext jiraAuthenticationContext, PermissionManager permissionManager,
                              ScrumPokerSessionService scrumPokerSessionService, IssueManager issueManager,
                              SessionEntityTransformer sessionEntityTransformer) {
        this.issueManager = issueManager;
        this.permissionManager = permissionManager;
        this.jiraAuthenticationContext = jiraAuthenticationContext;
        this.scrumPokerSessionService = scrumPokerSessionService;
        this.sessionEntityTransformer = sessionEntityTransformer;
    }

    @Override
    protected String doExecute() {
        return "success";
    }

    public List<SessionEntity> getOpenSessions() {
        return scrumPokerSessionService.recent().stream()
            .filter(session -> session.getConfirmedVote() == null)
            .filter(session -> getIssue(session.getIssueKey()) != null)
            .filter(session -> currentUserIsAllowedToSeeIssue(getIssue(session.getIssueKey())))
            .map(session -> sessionEntityTransformer.build(session, currentUser().getKey()))
            .collect(Collectors.toList());
    }

    public List<SessionEntity> getClosedSessions() {
        return scrumPokerSessionService.recent().stream()
            .filter(session -> session.getConfirmedVote() != null)
            .filter(session -> getIssue(session.getIssueKey()) != null)
            .filter(session -> currentUserIsAllowedToSeeIssue(getIssue(session.getIssueKey())))
            .map(session -> sessionEntityTransformer.build(session, currentUser().getKey()))
            .collect(Collectors.toList());
    }

    public MutableIssue getIssue(String issueKey) {
        return issueManager.getIssueObject(issueKey);
    }

    private boolean currentUserIsAllowedToSeeIssue(MutableIssue issue) {
        return permissionManager.hasPermission(BROWSE_PROJECTS, issue, currentUser());
    }

    private ApplicationUser currentUser() {
        return jiraAuthenticationContext.getLoggedInUser();
    }

}
