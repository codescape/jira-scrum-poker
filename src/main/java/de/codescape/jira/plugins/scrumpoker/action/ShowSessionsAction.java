package de.codescape.jira.plugins.scrumpoker.action;

import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import de.codescape.jira.plugins.scrumpoker.rest.entities.SessionEntity;
import de.codescape.jira.plugins.scrumpoker.rest.mapper.SessionEntityMapper;
import de.codescape.jira.plugins.scrumpoker.service.ScrumPokerSessionService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

import static com.atlassian.jira.permission.ProjectPermissions.BROWSE_PROJECTS;

/**
 * Show a list of all running and recently finished Scrum poker sessions.
 */
public class ShowSessionsAction extends AbstractScrumPokerAction {

    private static final long serialVersionUID = 1L;

    private final IssueManager issueManager;
    private final PermissionManager permissionManager;
    private final JiraAuthenticationContext jiraAuthenticationContext;
    private final ScrumPokerSessionService scrumPokerSessionService;
    private final SessionEntityMapper sessionEntityMapper;

    @Autowired
    public ShowSessionsAction(@ComponentImport JiraAuthenticationContext jiraAuthenticationContext,
                              @ComponentImport PermissionManager permissionManager,
                              ScrumPokerSessionService scrumPokerSessionService,
                              @ComponentImport IssueManager issueManager,
                              SessionEntityMapper sessionEntityMapper) {
        this.issueManager = issueManager;
        this.permissionManager = permissionManager;
        this.jiraAuthenticationContext = jiraAuthenticationContext;
        this.scrumPokerSessionService = scrumPokerSessionService;
        this.sessionEntityMapper = sessionEntityMapper;
    }

    @Override
    protected String doExecute() {
        return SUCCESS;
    }

    public List<SessionEntity> getOpenSessions() {
        return scrumPokerSessionService.recent().stream()
            .filter(session -> session.getConfirmedVote() == null)
            .filter(session -> !session.isCancelled())
            .filter(session -> getIssue(session.getIssueKey()) != null)
            .filter(session -> currentUserIsAllowedToSeeIssue(getIssue(session.getIssueKey())))
            .map(session -> sessionEntityMapper.build(session, currentUser().getKey()))
            .collect(Collectors.toList());
    }

    public List<SessionEntity> getClosedSessions() {
        return scrumPokerSessionService.recent().stream()
            .filter(session -> session.getConfirmedVote() != null || session.isCancelled())
            .filter(session -> getIssue(session.getIssueKey()) != null)
            .filter(session -> currentUserIsAllowedToSeeIssue(getIssue(session.getIssueKey())))
            .map(session -> sessionEntityMapper.build(session, currentUser().getKey()))
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
