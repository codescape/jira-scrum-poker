package de.codescape.jira.plugins.scrumpoker.action;

import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.jira.security.request.RequestMethod;
import com.atlassian.jira.security.request.SupportedMethods;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import de.codescape.jira.plugins.scrumpoker.rest.entities.SessionEntity;
import de.codescape.jira.plugins.scrumpoker.rest.mapper.SessionEntityMapper;
import de.codescape.jira.plugins.scrumpoker.service.ErrorLogService;
import de.codescape.jira.plugins.scrumpoker.service.ScrumPokerLicenseService;
import de.codescape.jira.plugins.scrumpoker.service.ScrumPokerSessionService;
import jakarta.inject.Inject;

import java.util.List;
import java.util.stream.Collectors;

import static com.atlassian.jira.permission.ProjectPermissions.BROWSE_PROJECTS;

/**
 * Show a list of all running and recently finished Scrum Poker sessions.
 */
@SupportedMethods({RequestMethod.GET})
public class ScrumPokerSessionsAction extends AbstractScrumPokerAction {

    private static final long serialVersionUID = 1L;

    private final IssueManager issueManager;
    private final PermissionManager permissionManager;
    private final JiraAuthenticationContext jiraAuthenticationContext;
    private final ScrumPokerSessionService scrumPokerSessionService;
    private final SessionEntityMapper sessionEntityMapper;
    private final ScrumPokerLicenseService scrumPokerLicenseService;

    @Inject
    public ScrumPokerSessionsAction(@ComponentImport JiraAuthenticationContext jiraAuthenticationContext,
                                    @ComponentImport PermissionManager permissionManager,
                                    @ComponentImport IssueManager issueManager,
                                    ScrumPokerSessionService scrumPokerSessionService,
                                    SessionEntityMapper sessionEntityMapper,
                                    ScrumPokerLicenseService scrumPokerLicenseService,
                                    ErrorLogService errorLogService) {
        super(errorLogService);
        this.jiraAuthenticationContext = jiraAuthenticationContext;
        this.permissionManager = permissionManager;
        this.issueManager = issueManager;
        this.scrumPokerSessionService = scrumPokerSessionService;
        this.sessionEntityMapper = sessionEntityMapper;
        this.scrumPokerLicenseService = scrumPokerLicenseService;
    }

    /**
     * Returns the license error if it exists or <code>null</code> otherwise.
     */
    public String getLicenseError() {
        return scrumPokerLicenseService.getLicenseError();
    }

    /**
     * Just show the page.
     */
    @Override
    protected String doExecute() {
        return SUCCESS;
    }

    /**
     * Return all open sessions that are visible to the current user.
     */
    public List<SessionEntity> getOpenSessions() {
        return scrumPokerSessionService.recent().stream()
            .filter(session -> session.getConfirmedEstimate() == null)
            .filter(session -> !session.isCancelled())
            .filter(session -> getIssue(session.getIssueKey()) != null)
            .filter(session -> currentUserIsAllowedToSeeIssue(getIssue(session.getIssueKey())))
            .map(session -> sessionEntityMapper.build(session, currentUser().getKey()))
            .collect(Collectors.toList());
    }

    /**
     * Return all closed sessions that are visible to the current user.
     */
    public List<SessionEntity> getClosedSessions() {
        return scrumPokerSessionService.recent().stream()
            .filter(session -> session.getConfirmedEstimate() != null || session.isCancelled())
            .filter(session -> getIssue(session.getIssueKey()) != null)
            .filter(session -> currentUserIsAllowedToSeeIssue(getIssue(session.getIssueKey())))
            .map(session -> sessionEntityMapper.build(session, currentUser().getKey()))
            .collect(Collectors.toList());
    }

    /**
     * Return the issue for the given issue key.
     *
     * @param issueKey issue key
     * @return issue
     */
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
