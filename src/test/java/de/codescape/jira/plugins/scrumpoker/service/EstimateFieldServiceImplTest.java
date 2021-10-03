package de.codescape.jira.plugins.scrumpoker.service;

import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.customfields.CustomFieldType;
import com.atlassian.jira.issue.customfields.manager.OptionsManager;
import com.atlassian.jira.issue.customfields.option.Option;
import com.atlassian.jira.issue.customfields.option.Options;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.fields.config.FieldConfig;
import com.atlassian.jira.permission.ProjectPermissions;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.jira.user.ApplicationUser;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerProject;
import de.codescape.jira.plugins.scrumpoker.model.GlobalSettings;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static de.codescape.jira.plugins.scrumpoker.service.EstimateFieldServiceImpl.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

public class EstimateFieldServiceImplTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private JiraAuthenticationContext jiraAuthenticationContext;

    @Mock
    private IssueManager issueManager;

    @Mock
    private CustomFieldManager customFieldManager;

    @Mock
    private PermissionManager permissionManager;

    @Mock
    private GlobalSettingsService globalSettingsService;

    @Mock
    private ErrorLogService errorLogService;

    @Mock
    private ProjectSettingsService projectSettingsService;

    @Mock
    private OptionsManager optionsManager;

    @InjectMocks
    private EstimateFieldServiceImpl estimateFieldService;

    /* tests for save(..) */

    @Test
    public void saveChecksUserPermissionIfRequired() {
        ApplicationUser applicationUser = expectApplicationUserExists();
        MutableIssue issue = expectIssueExists("ISSUE");

        GlobalSettings globalSettings = mock(GlobalSettings.class);
        when(globalSettingsService.load()).thenReturn(globalSettings);
        when(globalSettings.isCheckPermissionToSaveEstimate()).thenReturn(true);

        when(permissionManager.hasPermission(ProjectPermissions.EDIT_ISSUES, issue, applicationUser)).thenReturn(false);

        assertThat(estimateFieldService.save("ISSUE", "8"), is(false));

        verify(permissionManager).hasPermission(ProjectPermissions.EDIT_ISSUES, issue, applicationUser);
        verify(errorLogService).logError(anyString());
    }

    @Test
    public void saveUpdatesIssueForSupportedEstimateTextField() {
        ApplicationUser applicationUser = expectApplicationUserExists();
        MutableIssue issue = expectIssueExists("ISSUE");

        // global settings has estimate field set
        GlobalSettings globalSettings = mock(GlobalSettings.class);
        when(globalSettingsService.load()).thenReturn(globalSettings);
        when(globalSettings.getEstimateField()).thenReturn("VALID_FIELD");

        // estimate field is of supported type
        CustomField estimateField = expectCustomFieldOfType(CUSTOM_FIELD_TYPE_TEXT);
        when(customFieldManager.getCustomFieldObject("VALID_FIELD")).thenReturn(estimateField);

        assertThat(estimateFieldService.save("ISSUE", "8"), is(true));
        verify(issueManager).updateIssue(eq(applicationUser), eq(issue), any());
    }

    @Test
    public void saveUpdatesIssueForSupportedEstimateRadioButtonsWithValidOption() {
        ApplicationUser applicationUser = expectApplicationUserExists();
        MutableIssue issue = expectIssueExists("ISSUE");

        // global settings has estimate field set
        GlobalSettings globalSettings = mock(GlobalSettings.class);
        when(globalSettingsService.load()).thenReturn(globalSettings);
        when(globalSettings.getEstimateField()).thenReturn("VALID_FIELD");

        // estimate field is of supported type
        CustomField estimateField = expectCustomFieldOfType(CUSTOM_FIELD_TYPE_RADIO);
        when(customFieldManager.getCustomFieldObject("VALID_FIELD")).thenReturn(estimateField);

        FieldConfig fieldConfig = mock(FieldConfig.class);
        when(estimateField.getRelevantConfig(issue)).thenReturn(fieldConfig);

        // option exists for issue
        Options options = mock(Options.class);
        when(optionsManager.getOptions(fieldConfig)).thenReturn(options);
        Option option = mock(Option.class);
        when(options.getOptionForValue("8", null)).thenReturn(option);

        assertThat(estimateFieldService.save("ISSUE", "8"), is(true));
        verify(issueManager).updateIssue(eq(applicationUser), eq(issue), any());
    }

    @Test
    public void saveFailsForSupportedEstimateRadioButtonsWithoutValidOption() {
        ApplicationUser applicationUser = expectApplicationUserExists();
        MutableIssue issue = expectIssueExists("ISSUE");

        // global settings has estimate field set
        GlobalSettings globalSettings = mock(GlobalSettings.class);
        when(globalSettingsService.load()).thenReturn(globalSettings);
        when(globalSettings.getEstimateField()).thenReturn("VALID_FIELD");

        // estimate field is of supported type
        CustomField estimateField = expectCustomFieldOfType(CUSTOM_FIELD_TYPE_RADIO);
        when(customFieldManager.getCustomFieldObject("VALID_FIELD")).thenReturn(estimateField);

        FieldConfig fieldConfig = mock(FieldConfig.class);
        when(estimateField.getRelevantConfig(issue)).thenReturn(fieldConfig);

        // option does not exist for issue
        Options options = mock(Options.class);
        when(optionsManager.getOptions(fieldConfig)).thenReturn(options);
        when(options.getOptionForValue("8", null)).thenReturn(null);

        assertThat(estimateFieldService.save("ISSUE", "8"), is(false));
        verify(issueManager, never()).updateIssue(eq(applicationUser), eq(issue), any());
    }

    @Test
    public void saveFailsForNotSupportedEstimateFields() {
        expectApplicationUserExists();
        expectIssueExists("ISSUE");

        // global settings has estimate field set
        GlobalSettings globalSettings = mock(GlobalSettings.class);
        when(globalSettingsService.load()).thenReturn(globalSettings);
        when(globalSettings.getEstimateField()).thenReturn("WEIRD_FIELD");

        // estimate field is not of supported type
        CustomField estimateField = expectCustomFieldOfType("unsupported-field");
        when(customFieldManager.getCustomFieldObject("WEIRD_FIELD")).thenReturn(estimateField);

        assertThat(estimateFieldService.save("ISSUE", "8"), is(false));
        verify(errorLogService).logError(anyString());
    }

    @Test
    public void saveFailsForNonNumericValueForNumericEstimateField() {
        expectApplicationUserExists();
        expectIssueExists("ISSUE");

        // global settings has estimate field set
        GlobalSettings globalSettings = mock(GlobalSettings.class);
        when(globalSettingsService.load()).thenReturn(globalSettings);
        when(globalSettings.getEstimateField()).thenReturn("VALID_FIELD");

        // estimate field is of supported number type
        CustomField estimateField = expectCustomFieldOfType(CUSTOM_FIELD_TYPE_NUMBER);
        when(customFieldManager.getCustomFieldObject("VALID_FIELD")).thenReturn(estimateField);

        assertThat(estimateFieldService.save("ISSUE", "ayayay"), is(false));
        verify(errorLogService).logError(anyString(), any(NumberFormatException.class));
    }

    @Test
    public void saveFailsForAnyErrorWhileSaving() {
        ApplicationUser applicationUser = expectApplicationUserExists();
        MutableIssue issue = expectIssueExists("ISSUE");

        // global settings has estimate field set
        GlobalSettings globalSettings = mock(GlobalSettings.class);
        when(globalSettingsService.load()).thenReturn(globalSettings);
        when(globalSettings.getEstimateField()).thenReturn("VALID_FIELD");

        // estimate field is of supported type
        CustomField estimateField = expectCustomFieldOfType(CUSTOM_FIELD_TYPE_NUMBER);
        when(customFieldManager.getCustomFieldObject("VALID_FIELD")).thenReturn(estimateField);

        // saving should result into an error
        RuntimeException expectedException = new RuntimeException("oops");
        when(issueManager.updateIssue(eq(applicationUser), eq(issue), any())).thenThrow(expectedException);

        assertThat(estimateFieldService.save("ISSUE", "8"), is(false));
        verify(errorLogService).logError(anyString(), eq(expectedException));
    }

    /* tests for estimateFieldForIssue() */

    @Test
    public void estimateFieldForIssuePrefersProjectConfigurationOverGlobalConfiguration() {
        Issue issue = expectIssue(42L);

        // the project has a custom estimate field configured
        ScrumPokerProject scrumPokerProject = mock(ScrumPokerProject.class);
        when(scrumPokerProject.getEstimateField()).thenReturn("PROJECT_FIELD");
        when(projectSettingsService.loadSettings(42L)).thenReturn(scrumPokerProject);

        // custom field returns
        CustomField projectField = mock(CustomField.class);
        when(customFieldManager.getCustomFieldObject(eq("PROJECT_FIELD"))).thenReturn(projectField);

        assertThat(estimateFieldService.estimateFieldForIssue(issue), is(equalTo(projectField)));
    }

    @Test
    public void estimateFieldForIssueFallsBackToGlobalConfigurationIfNoProjectConfigurationExists() {
        Issue issue = expectIssue(42L);

        // no project specific settings confgured
        when(projectSettingsService.loadSettings(42L)).thenReturn(null);

        // the global configuration returns the field
        GlobalSettings globalSettings = mock(GlobalSettings.class);
        when(globalSettingsService.load()).thenReturn(globalSettings);
        when(globalSettings.getEstimateField()).thenReturn("GLOBAL_FIELD");

        CustomField globalField = mock(CustomField.class);
        when(customFieldManager.getCustomFieldObject("GLOBAL_FIELD")).thenReturn(globalField);

        assertThat(estimateFieldService.estimateFieldForIssue(issue), is(equalTo(globalField)));
    }

    /* tests for isEstimable() */

    @Test
    public void isEstimableReturnsFalseIfIssueIsNotEditable() {
        // issue is not editable
        Issue nonEditableIssue = expectIssue(false);

        assertThat(estimateFieldService.isEstimable(nonEditableIssue), is(false));
    }

    @Test
    public void isEstimableReturnsFalseIfIssueIsMissingTheEstimateFieldConfiguredForProject() {
        // issue is editable
        Issue issueWithoutEstimate = expectIssue(true, 42L);

        // the project has a custom estimate field configured
        ScrumPokerProject scrumPokerProject = mock(ScrumPokerProject.class);
        when(scrumPokerProject.getEstimateField()).thenReturn("PROJECT_FIELD");
        when(projectSettingsService.loadSettings(42L)).thenReturn(scrumPokerProject);

        // the custom field exists but is not associated with the issue
        CustomField fieldNotOnIssue = mock(CustomField.class);
        when(customFieldManager.getCustomFieldObject("PROJECT_FIELD")).thenReturn(fieldNotOnIssue);
        when(customFieldManager.getCustomFieldObjects(issueWithoutEstimate)).thenReturn(new ArrayList<>());

        assertThat(estimateFieldService.isEstimable(issueWithoutEstimate), is(false));
    }

    @Test
    public void isEstimableReturnsFalseIfIssueIsMissingTheEstimateFieldConfiguredGlobally() {
        // issue is editable
        Issue issueWithoutEstimate = expectIssue(true, 42L);

        // the project has non project specific configuration
        when(projectSettingsService.loadSettings(42L)).thenReturn(null);

        // the global configuration returns the field
        GlobalSettings globalSettings = mock(GlobalSettings.class);
        when(globalSettingsService.load()).thenReturn(globalSettings);
        when(globalSettings.getEstimateField()).thenReturn("GLOBAL_FIELD");

        // the globally configured field does not exist for the issue
        CustomField fieldNotOnIssue = mock(CustomField.class);
        when(customFieldManager.getCustomFieldObject("GLOBAL_FIELD")).thenReturn(fieldNotOnIssue);
        when(customFieldManager.getCustomFieldObjects(issueWithoutEstimate)).thenReturn(new ArrayList<>());

        assertThat(estimateFieldService.isEstimable(issueWithoutEstimate), is(false));
    }

    @Test
    public void isEstimableReturnsTrueForEditableIssueWithScrumPokerEnabledAndExistingEstimateField() {
        // issue is editable
        Issue issue = expectIssue(true, 42L);

        // the project has non project specific configuration
        when(projectSettingsService.loadSettings(42L)).thenReturn(null);

        // the global configuration returns the field
        GlobalSettings globalSettings = mock(GlobalSettings.class);
        when(globalSettingsService.load()).thenReturn(globalSettings);
        when(globalSettings.getEstimateField()).thenReturn("GLOBAL_FIELD");

        // the globally configured field exists for the issue
        CustomField estimateField = mock(CustomField.class);
        when(customFieldManager.getCustomFieldObject("GLOBAL_FIELD")).thenReturn(estimateField);
        when(customFieldManager.getCustomFieldObjects(issue)).thenReturn(Collections.singletonList(estimateField));

        // Scrum Poker is enabled globally
        when(globalSettings.isActivateScrumPoker()).thenReturn(true);

        assertThat(estimateFieldService.isEstimable(issue), is(true));
    }

    @Test
    public void isEstimableReturnsTrueForEditableIssueWithScrumPokerEnabledOnProjectLevelAndExistingEstimateField() {
        // issue is editable
        Issue issue = expectIssue(true, 42L);

        // the project has Scrum Poker locally enabled
        ScrumPokerProject scrumPokerProject = mock(ScrumPokerProject.class);
        when(scrumPokerProject.isScrumPokerEnabled()).thenReturn(true);
        when(projectSettingsService.loadSettings(42L)).thenReturn(scrumPokerProject);

        // the global configuration returns the field
        GlobalSettings globalSettings = mock(GlobalSettings.class);
        when(globalSettingsService.load()).thenReturn(globalSettings);
        when(globalSettings.getEstimateField()).thenReturn("GLOBAL_FIELD");

        // the globally configured field exists for the issue
        CustomField estimateField = mock(CustomField.class);
        when(customFieldManager.getCustomFieldObject("GLOBAL_FIELD")).thenReturn(estimateField);
        when(customFieldManager.getCustomFieldObjects(issue)).thenReturn(Collections.singletonList(estimateField));

        // Scrum Poker is not enabled globally
        when(globalSettings.isActivateScrumPoker()).thenReturn(false);

        assertThat(estimateFieldService.isEstimable(issue), is(true));
    }

    /* tests for supportedCustomFields() */

    @Test
    public void supportedCustomFieldsOnlyReturnsFieldsOfSupportedType() {
        List<CustomField> existingCustomFields = new ArrayList<>();
        existingCustomFields.add(expectCustomFieldOfType(CUSTOM_FIELD_TYPE_NUMBER));
        existingCustomFields.add(expectCustomFieldOfType("some.unsupported.type"));
        when(customFieldManager.getCustomFieldObjects()).thenReturn(existingCustomFields);

        List<CustomField> customFields = estimateFieldService.supportedCustomFields();

        assertThat(customFields.size(), is(equalTo(1)));
        assertThat(customFields.get(0).getCustomFieldType().getKey(), is(equalTo(CUSTOM_FIELD_TYPE_NUMBER)));
    }

    /* supporting methods */

    private CustomField expectCustomFieldOfType(String customFieldType) {
        CustomField supportedCustomField = mock(CustomField.class);
        CustomFieldType supportedCustomFieldType = mock(CustomFieldType.class);

        when(supportedCustomField.getCustomFieldType()).thenReturn(supportedCustomFieldType);
        when(supportedCustomFieldType.getKey()).thenReturn(customFieldType);

        return supportedCustomField;
    }

    private Issue expectIssue(boolean editable) {
        Issue issueWithoutEstimate = mock(Issue.class);
        when(issueWithoutEstimate.isEditable()).thenReturn(editable);
        return issueWithoutEstimate;
    }

    private Issue expectIssue(long projectId) {
        Issue issueWithoutEstimate = mock(Issue.class);
        when(issueWithoutEstimate.getProjectId()).thenReturn(projectId);
        return issueWithoutEstimate;
    }

    private Issue expectIssue(boolean editable, long projectId) {
        Issue issueWithoutEstimate = mock(Issue.class);
        when(issueWithoutEstimate.isEditable()).thenReturn(editable);
        when(issueWithoutEstimate.getProjectId()).thenReturn(projectId);
        return issueWithoutEstimate;
    }

    private ApplicationUser expectApplicationUserExists() {
        ApplicationUser applicationUser = mock(ApplicationUser.class);
        when(jiraAuthenticationContext.getLoggedInUser()).thenReturn(applicationUser);
        return applicationUser;
    }

    private MutableIssue expectIssueExists(String issueKey) {
        MutableIssue issue = mock(MutableIssue.class);
        when(issueManager.getIssueByCurrentKey(issueKey)).thenReturn(issue);
        return issue;
    }

}
