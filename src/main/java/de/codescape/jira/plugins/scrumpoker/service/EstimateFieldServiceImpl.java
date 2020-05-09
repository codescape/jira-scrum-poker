package de.codescape.jira.plugins.scrumpoker.service;

import com.atlassian.jira.issue.*;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.permission.ProjectPermissions;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Implementation of {@link EstimateFieldService} with access to Jira custom fields.
 */
@Component
public class EstimateFieldServiceImpl implements EstimateFieldService {

    static final String CUSTOM_FIELD_TYPE_NUMBER = "com.atlassian.jira.plugin.system.customfieldtypes:float";
    static final String CUSTOM_FIELD_TYPE_TEXT = "com.atlassian.jira.plugin.system.customfieldtypes:textfield";
    static final String CUSTOM_FIELD_TYPE_TEXTAREA = "com.atlassian.jira.plugin.system.customfieldtypes:textarea";

    private final GlobalSettingsService globalSettingsService;
    private final CustomFieldManager customFieldManager;
    private final JiraAuthenticationContext jiraAuthenticationContext;
    private final PermissionManager permissionManager;
    private final IssueManager issueManager;
    private final ErrorLogService errorLogService;

    @Autowired
    public EstimateFieldServiceImpl(@ComponentImport JiraAuthenticationContext jiraAuthenticationContext,
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
    public boolean save(String issueKey, String estimate) {
        ApplicationUser applicationUser = jiraAuthenticationContext.getLoggedInUser();
        MutableIssue issue = issueManager.getIssueByCurrentKey(issueKey);
        // check permission to save the estimate by the current user
        if (globalSettingsService.load().isCheckPermissionToSaveEstimate() &&
            !permissionManager.hasPermission(ProjectPermissions.EDIT_ISSUES, issue, applicationUser)) {
            errorLogService.logError("User " + applicationUser.getUsername() +
                " is missing permissions to save estimation for issue " + issueKey + ".", null);
            return false;
        }
        // based on the type of the custom field apply the estimate in the correct data type
        CustomField estimateField = findEstimateField();
        String estimateFieldKey = estimateField.getCustomFieldType().getKey();
        switch (estimateFieldKey) {
            case CUSTOM_FIELD_TYPE_NUMBER:
                issue.setCustomFieldValue(estimateField, Double.valueOf(estimate));
                break;
            case CUSTOM_FIELD_TYPE_TEXT:
            case CUSTOM_FIELD_TYPE_TEXTAREA:
                issue.setCustomFieldValue(estimateField, estimate);
                break;
            default:
                errorLogService.logError("Unable to save estimate because field type "
                    + estimateFieldKey + " is not supported.", null);
                return false;
        }
        // finally update the issue
        try {
            issueManager.updateIssue(applicationUser, issue, UpdateIssueRequest.builder().build());
            return true;
        } catch (RuntimeException e) {
            errorLogService.logError("Unable to save estimate " + estimate + " for issue "
                + issueKey + ". See stacktrace for further details.", e);
            return false;
        }
    }

    @Override
    public CustomField findEstimateField() {
        return customFieldManager.getCustomFieldObject(globalSettingsService.load().getEstimateField());
    }

    @Override
    public boolean hasEstimateField(Issue issue) {
        CustomField estimateField = findEstimateField();
        return estimateField != null && customFieldManager.getCustomFieldObjects(issue).contains(estimateField);
    }

}
