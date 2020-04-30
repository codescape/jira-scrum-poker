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
import de.codescape.jira.plugins.scrumpoker.model.GlobalSettings;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class EstimateFieldServiceImplTest {

    private static final String ISSUE_KEY = "ISSUE-0815";
    private static final Integer ESTIMATION = 5;
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
    private EstimateFieldServiceImpl estimationFieldService;

    @Mock
    private ApplicationUser applicationUser;

    @Mock
    private CustomField customField;

    @Mock
    private MutableIssue issue;

    @Mock
    private GlobalSettings globalSettings;

    @Before
    public void before() {
        when(jiraAuthenticationContext.getLoggedInUser()).thenReturn(applicationUser);
        when(issueManager.getIssueByCurrentKey(ISSUE_KEY)).thenReturn(issue);
        when(globalSettingsService.load()).thenReturn(globalSettings);
    }

    @Test
    public void withErrorsDuringUpdateItShouldReturnFalse() {
        expectCustomFieldFound();
        expectUpdatingIssueFails();
        assertThat(estimationFieldService.save(ISSUE_KEY, ESTIMATION), is(false));
    }

    @Test
    public void withSuccessfulUpdateItShouldReturnTrue() {
        expectCustomFieldFound();
        expectUpdatingIssueSuccessful();
        assertThat(estimationFieldService.save(ISSUE_KEY, ESTIMATION), is(true));
    }

    @Test
    public void withPermissionCheckEnabledAndNoPermission() {
        expectPermissionCheckEnabled();
        expectPermissionCheckFails();
        assertThat(estimationFieldService.save(ISSUE_KEY, ESTIMATION), is(false));
    }

    @Test
    public void withPermissionCheckEnabledAndPermissionExists() {
        expectPermissionCheckEnabled();
        expectPermissionCheckSuccessful();
        expectCustomFieldFound();
        expectUpdatingIssueSuccessful();
        assertThat(estimationFieldService.save(ISSUE_KEY, ESTIMATION), is(true));
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

}
