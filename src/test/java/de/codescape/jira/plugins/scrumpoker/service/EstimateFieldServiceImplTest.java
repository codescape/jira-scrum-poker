package de.codescape.jira.plugins.scrumpoker.service;

import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.UpdateIssueRequest;
import com.atlassian.jira.issue.customfields.CustomFieldType;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.permission.ProjectPermissions;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.jira.user.ApplicationUser;
import de.codescape.jira.plugins.scrumpoker.model.GlobalSettings;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Arrays;
import java.util.Collections;

import static de.codescape.jira.plugins.scrumpoker.service.EstimateFieldServiceImpl.CUSTOM_FIELD_TYPE_NUMBER;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

public class EstimateFieldServiceImplTest {

    private static final String ISSUE_KEY = "ISSUE-0815";
    private static final String ESTIMATE = "5";
    private static final String CUSTOM_FIELD_ID = "11045";

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

    @InjectMocks
    private EstimateFieldServiceImpl estimateFieldService;

    @Mock
    private ApplicationUser applicationUser;

    @Mock
    private CustomField customField;

    @Mock
    private CustomFieldType customFieldType;

    @Mock
    private CustomField anotherField;

    @Mock
    private MutableIssue issue;

    @Mock
    private GlobalSettings globalSettings;

    @Before
    public void before() {
        when(globalSettingsService.load()).thenReturn(globalSettings);
    }

    /* tests for save(..) */

    @Test
    public void saveFailsWithoutPermissionCheckAndWithErrorsDuringUpdate() {
        expectUserLoggedIn();
        expectIssueWithIssueKeyExists();
        expectPermissionCheckDisabled();
        expectCustomFieldFound();
        expectCustomFieldTypeSupported();
        expectUpdatingIssueFails();
        assertThat(estimateFieldService.save(ISSUE_KEY, ESTIMATE), is(false));
    }

    @Test
    public void saveSucceedsWithoutPermissionCheckAndSuccessfulUpdate() {
        expectUserLoggedIn();
        expectIssueWithIssueKeyExists();
        expectPermissionCheckDisabled();
        expectCustomFieldFound();
        expectCustomFieldTypeSupported();
        expectUpdatingIssueSuccessful();
        assertThat(estimateFieldService.save(ISSUE_KEY, ESTIMATE), is(true));
    }

    @Test
    public void saveFailsWithPermissionCheckEnabledAndNoPermission() {
        expectUserLoggedIn();
        expectIssueWithIssueKeyExists();
        expectPermissionCheckEnabled();
        expectPermissionCheckFails();
        assertThat(estimateFieldService.save(ISSUE_KEY, ESTIMATE), is(false));
    }

    @Test
    public void saveSucceedsWithPermissionCheckEnabledAndPermissionExistsAndSuccessfulUpdate() {
        expectUserLoggedIn();
        expectIssueWithIssueKeyExists();
        expectPermissionCheckEnabled();
        expectPermissionCheckSuccessful();
        expectCustomFieldFound();
        expectCustomFieldTypeSupported();
        expectUpdatingIssueSuccessful();
        assertThat(estimateFieldService.save(ISSUE_KEY, ESTIMATE), is(true));
    }

    @Test
    public void saveFailsWithCustomFieldNotSupported() {
        expectUserLoggedIn();
        expectIssueWithIssueKeyExists();
        expectPermissionCheckDisabled();
        expectCustomFieldFound();
        expectCustomFieldTypeNotSupported();
        assertThat(estimateFieldService.save(ISSUE_KEY, ESTIMATE), is(false));
    }

    @Test
    public void saveFailsWithNumericFieldNotSupportingEstimate() {
        expectUserLoggedIn();
        expectIssueWithIssueKeyExists();
        expectPermissionCheckDisabled();
        expectCustomFieldFound();
        expectCustomFieldType(CUSTOM_FIELD_TYPE_NUMBER);
        assertThat(estimateFieldService.save(ISSUE_KEY, "S"), is(false));
        verify(errorLogService).logError(anyString(), any(NumberFormatException.class));
    }

    /* tests for isEstimable(..) */

    @Test
    public void isEstimableReturnsTrueForEditableIssueWithCustomFieldAndScrumPokerActivated() {
        when(issue.isEditable()).thenReturn(true);
        when(globalSettings.isActivateScrumPoker()).thenReturn(true);
        when(globalSettings.getEstimateField()).thenReturn(CUSTOM_FIELD_ID);
        when(customFieldManager.getCustomFieldObject(CUSTOM_FIELD_ID)).thenReturn(customField);
        when(customFieldManager.getCustomFieldObjects(issue)).thenReturn(Arrays.asList(customField, anotherField));

        assertThat(estimateFieldService.isEstimable(issue), is(true));
    }

    @Test
    public void isEstimableReturnsFalseForIssueWithoutCustomFieldAndScrumPokerActivated() {
        when(issue.isEditable()).thenReturn(true);
        when(globalSettings.getEstimateField()).thenReturn(CUSTOM_FIELD_ID);
        when(customFieldManager.getCustomFieldObject(CUSTOM_FIELD_ID)).thenReturn(customField);
        when(customFieldManager.getCustomFieldObjects(issue)).thenReturn(Collections.singletonList(anotherField));

        assertThat(estimateFieldService.isEstimable(issue), is(false));
    }

    /* supporting methods */

    private void expectCustomFieldTypeNotSupported() {
        when(customField.getCustomFieldType()).thenReturn(customFieldType);
        when(customFieldType.getKey()).thenReturn("some.crazy.unknown::customfield");
    }

    private void expectCustomFieldTypeSupported() {
        expectCustomFieldType(CUSTOM_FIELD_TYPE_NUMBER);
    }

    private void expectCustomFieldType(String customFieldTypeKey) {
        when(customField.getCustomFieldType()).thenReturn(customFieldType);
        when(customFieldType.getKey()).thenReturn(customFieldTypeKey);
    }

    private void expectIssueWithIssueKeyExists() {
        when(issueManager.getIssueByCurrentKey(ISSUE_KEY)).thenReturn(issue);
    }

    private void expectUserLoggedIn() {
        when(jiraAuthenticationContext.getLoggedInUser()).thenReturn(applicationUser);
    }

    private void expectCustomFieldFound() {
        when(globalSettings.getEstimateField()).thenReturn(CUSTOM_FIELD_ID);
        when(customFieldManager.getCustomFieldObject(CUSTOM_FIELD_ID)).thenReturn(customField);
    }

    private void expectPermissionCheckFails() {
        when(permissionManager.hasPermission(ProjectPermissions.EDIT_ISSUES, issue, applicationUser)).thenReturn(false);
    }

    private void expectPermissionCheckSuccessful() {
        when(permissionManager.hasPermission(ProjectPermissions.EDIT_ISSUES, issue, applicationUser)).thenReturn(true);
    }

    private void expectUpdatingIssueSuccessful() {
        when(issueManager.updateIssue(eq(applicationUser), eq(issue), any(UpdateIssueRequest.class)))
            .thenReturn(issue);
    }

    private void expectUpdatingIssueFails() {
        when(issueManager.updateIssue(eq(applicationUser), eq(issue), any(UpdateIssueRequest.class)))
            .thenThrow(new RuntimeException());
    }

    private void expectPermissionCheckEnabled() {
        when(globalSettings.isCheckPermissionToSaveEstimate()).thenReturn(true);
    }

    private void expectPermissionCheckDisabled() {
        when(globalSettings.isCheckPermissionToSaveEstimate()).thenReturn(false);
    }

}
