package de.codescape.jira.plugins.scrumpoker.helper;

import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.permission.ProjectPermissions;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import jakarta.inject.Inject;
import org.springframework.stereotype.Component;

/**
 * This helper provides all necessary permission checks to other classes and works as the single source of truth for
 * permissions defined by business rules.
 */
@Component
public class ScrumPokerPermissions {

    private final JiraAuthenticationContext jiraAuthenticationContext;
    private final PermissionManager permissionManager;
    private final IssueManager issueManager;

    @Inject
    public ScrumPokerPermissions(@ComponentImport JiraAuthenticationContext jiraAuthenticationContext,
                                 @ComponentImport PermissionManager permissionManager,
                                 @ComponentImport IssueManager issueManager) {
        this.jiraAuthenticationContext = jiraAuthenticationContext;
        this.permissionManager = permissionManager;
        this.issueManager = issueManager;
    }

    /**
     * A user needs to be allowed to see the issue to participate in Scrum Poker sessions associated with this issue.
     */
    public boolean currentUserIsAllowedToSeeIssue(String issueKey) {
        return permissionManager.hasPermission(
            ProjectPermissions.BROWSE_PROJECTS, issueManager.getIssueObject(issueKey), jiraAuthenticationContext.getLoggedInUser());
    }

    /**
     * A user needs to be allowed to see the issue to participate in Scrum Poker sessions associated with this issue.
     */
    public boolean currentUserIsAllowedToEditIssue(String issueKey) {
        return permissionManager.hasPermission(
            ProjectPermissions.EDIT_ISSUES, issueManager.getIssueObject(issueKey), jiraAuthenticationContext.getLoggedInUser());
    }

}
