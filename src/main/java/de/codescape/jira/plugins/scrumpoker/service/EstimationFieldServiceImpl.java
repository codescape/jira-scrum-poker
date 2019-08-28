package de.codescape.jira.plugins.scrumpoker.service;

import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.UpdateIssueRequest;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.permission.ProjectPermissions;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Implementation of {@link EstimationFieldService} with access to Jira custom fields.
 */
@Component
public class EstimationFieldServiceImpl implements EstimationFieldService {

    private final ScrumPokerSettingService scrumPokerSettingService;
    private final CustomFieldManager customFieldManager;
    private final JiraAuthenticationContext jiraAuthenticationContext;
    private final PermissionManager permissionManager;
    private final IssueManager issueManager;
    private final ScrumPokerErrorService scrumPokerErrorService;

    @Autowired
    public EstimationFieldServiceImpl(@ComponentImport JiraAuthenticationContext jiraAuthenticationContext,
                                      ScrumPokerSettingService scrumPokerSettingService,
                                      @ComponentImport IssueManager issueManager,
                                      @ComponentImport CustomFieldManager customFieldManager,
                                      @ComponentImport PermissionManager permissionManager,
                                      ScrumPokerErrorService scrumPokerErrorService) {
        this.scrumPokerSettingService = scrumPokerSettingService;
        this.issueManager = issueManager;
        this.customFieldManager = customFieldManager;
        this.jiraAuthenticationContext = jiraAuthenticationContext;
        this.permissionManager = permissionManager;
        this.scrumPokerErrorService = scrumPokerErrorService;
    }

    @Override
    public boolean save(String issueKey, Integer newValue) {
        ApplicationUser applicationUser = jiraAuthenticationContext.getLoggedInUser();
        MutableIssue issue = issueManager.getIssueByCurrentKey(issueKey);
        if (scrumPokerSettingService.load().isCheckPermissionToSaveEstimate() &&
            !permissionManager.hasPermission(ProjectPermissions.EDIT_ISSUES, issue, applicationUser)) {
            scrumPokerErrorService.logError("User " + applicationUser.getUsername() +
                " is missing permissions to save estimation for issue " + issueKey + ".", null);
            return false;
        }
        issue.setCustomFieldValue(findStoryPointField(), newValue.doubleValue());
        try {
            issueManager.updateIssue(applicationUser, issue, UpdateIssueRequest.builder().build());
            return true;
        } catch (RuntimeException e) {
            scrumPokerErrorService.logError("Unable to save estimation " + newValue + " for issue "
                + issueKey + ". See stacktrace for further details.", e);
            return false;
        }
    }

    @Override
    public CustomField findStoryPointField() {
        return customFieldManager.getCustomFieldObject(scrumPokerSettingService.load().getStoryPointField());
    }

}
