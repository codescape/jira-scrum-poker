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

    private final GlobalSettingsService globalSettingsService;
    private final CustomFieldManager customFieldManager;
    private final JiraAuthenticationContext jiraAuthenticationContext;
    private final PermissionManager permissionManager;
    private final IssueManager issueManager;
    private final ErrorLogService errorLogService;

    @Autowired
    public EstimationFieldServiceImpl(@ComponentImport JiraAuthenticationContext jiraAuthenticationContext,
                                      @ComponentImport IssueManager issueManager,
                                      @ComponentImport CustomFieldManager customFieldManager,
                                      @ComponentImport PermissionManager permissionManager,
                                      GlobalSettingsService globalSettingsService,
                                      ErrorLogService errorLogService) {
        this.jiraAuthenticationContext = jiraAuthenticationContext;
        this.issueManager = issueManager;
        this.customFieldManager = customFieldManager;
        this.permissionManager = permissionManager;
        this.globalSettingsService = globalSettingsService;
        this.errorLogService = errorLogService;
    }

    @Override
    public boolean save(String issueKey, Integer newValue) {
        ApplicationUser applicationUser = jiraAuthenticationContext.getLoggedInUser();
        MutableIssue issue = issueManager.getIssueByCurrentKey(issueKey);
        if (globalSettingsService.load().isCheckPermissionToSaveEstimate() &&
            !permissionManager.hasPermission(ProjectPermissions.EDIT_ISSUES, issue, applicationUser)) {
            errorLogService.logError("User " + applicationUser.getUsername() +
                " is missing permissions to save estimation for issue " + issueKey + ".", null);
            return false;
        }
        issue.setCustomFieldValue(findStoryPointField(), newValue.doubleValue());
        try {
            issueManager.updateIssue(applicationUser, issue, UpdateIssueRequest.builder().build());
            return true;
        } catch (RuntimeException e) {
            errorLogService.logError("Unable to save estimation " + newValue + " for issue "
                + issueKey + ". See stacktrace for further details.", e);
            return false;
        }
    }

    @Override
    public CustomField findStoryPointField() {
        return customFieldManager.getCustomFieldObject(globalSettingsService.load().getStoryPointField());
    }

}
