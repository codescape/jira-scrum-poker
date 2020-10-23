package de.codescape.jira.plugins.scrumpoker.service;

import com.atlassian.jira.issue.*;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.permission.ProjectPermissions;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerProject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of {@link EstimateFieldService} with access to Jira custom fields.
 */
@Component
public class EstimateFieldServiceImpl implements EstimateFieldService {

    static final String CUSTOM_FIELD_TYPE_NUMBER = "com.atlassian.jira.plugin.system.customfieldtypes:float";
    static final String CUSTOM_FIELD_TYPE_TEXT = "com.atlassian.jira.plugin.system.customfieldtypes:textfield";
    static final String CUSTOM_FIELD_TYPE_TEXTAREA = "com.atlassian.jira.plugin.system.customfieldtypes:textarea";

    static final List<String> SUPPORTED_FIELD_TYPES = Arrays.asList(
        CUSTOM_FIELD_TYPE_NUMBER, CUSTOM_FIELD_TYPE_TEXT, CUSTOM_FIELD_TYPE_TEXTAREA);

    private final GlobalSettingsService globalSettingsService;
    private final ProjectSettingsService projectSettingsService;
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
                                    ProjectSettingsService projectSettingsService,
                                    ErrorLogService errorLogService) {
        this.projectSettingsService = projectSettingsService;
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
                " is missing permissions to save estimation for issue " + issueKey + ".");
            return false;
        }
        // based on the type of the custom field apply the estimate in the correct data type
        CustomField estimateField = estimateFieldForIssue(issue);
        String estimateFieldKey = estimateField.getCustomFieldType().getKey();
        switch (estimateFieldKey) {
            case CUSTOM_FIELD_TYPE_NUMBER:
                try {
                    issue.setCustomFieldValue(estimateField, Double.valueOf(estimate));
                    break;
                } catch (NumberFormatException numberFormatException) {
                    errorLogService.logError("Unable to save estimate of " + estimate + " because field "
                        + estimateField.getFieldName() + " does not support value.", numberFormatException);
                    return false;
                }
            case CUSTOM_FIELD_TYPE_TEXT:
            case CUSTOM_FIELD_TYPE_TEXTAREA:
                issue.setCustomFieldValue(estimateField, estimate);
                break;
            default:
                errorLogService.logError("Unable to save estimate because field type "
                    + estimateFieldKey + " is not supported.");
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
    public CustomField estimateFieldForIssue(Issue issue) {
        Long projectId = issue.getProjectId();
        ScrumPokerProject scrumPokerProject = projectSettingsService.loadSettings(projectId);
        // use the project specific field if configured otherwise use the globally configured estimate field
        if (scrumPokerProject.getEstimateField() != null) {
            return customFieldManager.getCustomFieldObject(scrumPokerProject.getEstimateField());
        } else {
            return customFieldManager.getCustomFieldObject(globalSettingsService.load().getEstimateField());
        }
    }

    @Override
    public boolean isEstimable(Issue issue) {
        return issue.isEditable() && hasEstimateField(issue) && hasScrumPokerEnabled(issue.getProjectObject());
    }

    private boolean hasEstimateField(Issue issue) {
        CustomField estimateField = estimateFieldForIssue(issue);
        return estimateField != null && customFieldManager.getCustomFieldObjects(issue).contains(estimateField);
    }

    private boolean hasScrumPokerEnabled(Project project) {
        return globalSettingsService.load().isActivateScrumPoker()
            || projectSettingsService.loadSettings(project.getId()).isScrumPokerEnabled();
    }

    @Override
    public List<CustomField> supportedCustomFields() {
        return customFieldManager.getCustomFieldObjects().stream()
            .filter(customField -> SUPPORTED_FIELD_TYPES.contains(customField.getCustomFieldType().getKey()))
            .collect(Collectors.toList());
    }

}
