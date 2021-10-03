package de.codescape.jira.plugins.scrumpoker.action;

import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.junit.rules.AvailableInContainer;
import com.atlassian.jira.junit.rules.MockitoContainer;
import com.atlassian.jira.junit.rules.MockitoMocksInContainer;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.project.ProjectManager;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.web.HttpServletVariables;
import de.codescape.jira.plugins.scrumpoker.ScrumPokerConstants;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerProject;
import de.codescape.jira.plugins.scrumpoker.condition.ProjectAdministrationCondition;
import de.codescape.jira.plugins.scrumpoker.model.GlobalSettings;
import de.codescape.jira.plugins.scrumpoker.service.*;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

import static de.codescape.jira.plugins.scrumpoker.action.ScrumPokerProjectConfigurationAction.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class ScrumPokerProjectConfigurationActionTest {

    private static final String LICENSE_ERROR = "license.error";

    @Rule
    public MockitoContainer mockitoContainer = MockitoMocksInContainer.rule(this);

    @Mock
    private GlobalSettingsService globalSettingsService;

    @Mock
    private ProjectSettingsService projectSettingsService;

    @Mock
    private EstimateFieldService estimateFieldService;

    @Mock
    private ProjectManager projectManager;

    @Mock
    private ErrorLogService errorLogService;

    @Mock
    private ScrumPokerLicenseService scrumPokerLicenseService;

    @Mock
    private JiraAuthenticationContext jiraAuthenticationContext;

    @Mock
    private ProjectAdministrationCondition projectAdministrationCondition;

    @Mock
    @AvailableInContainer
    private HttpServletVariables httpServletVariables;

    @InjectMocks
    private ScrumPokerProjectConfigurationAction action;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private GlobalSettings globalSettings;

    @Mock
    private Project project;

    @Mock
    private ScrumPokerProject scrumPokerProject;

    @Mock
    private ApplicationUser applicationUser;

    /* tests for getProjectKey() */

    @Test
    public void shouldExposeTheProjectKeyWhenCalled() {
        when(httpServletVariables.getHttpRequest()).thenReturn(httpServletRequest);
        when(httpServletRequest.getParameterValues(Parameters.PROJECT_KEY)).thenReturn(new String[]{"ABC"});
        when(projectManager.getProjectByCurrentKey(eq("ABC"))).thenReturn(project);
        expectCurrentUserToBeAllowedToAdministrateTheProject(true);

        action.doExecute();

        assertThat(action.getProjectKey(), is(equalTo("ABC")));
    }

    /* tests for isActivateScrumPokerGlobally() */

    @Test
    public void shouldReturnGlobalActivateScrumPokerFlag() {
        when(globalSettingsService.load()).thenReturn(globalSettings);
        when(globalSettings.isActivateScrumPoker()).thenReturn(true);

        assertThat(action.isActivateScrumPokerGlobally(), is(true));
    }

    /* tests for getProjectSettings() */

    @Test
    public void getProjectSettingsShouldReturnProjectSpecificConfigurationIfExists() {
        expectHttpParametersToContainProjectKey("ABC");
        expectProjectManagerToFindAndReturnProject("ABC");
        expectCurrentUserToBeAllowedToAdministrateTheProject(true);

        action.doExecute();

        when(project.getId()).thenReturn(42L);
        when(projectSettingsService.loadSettings(eq(42L))).thenReturn(scrumPokerProject);

        assertThat(action.getProjectSettings(), is(equalTo(scrumPokerProject)));
    }

    @Test
    public void getProjectSettingsShouldReturnNullIfNoneExists() {
        expectHttpParametersToContainProjectKey("PROJECT");
        expectProjectManagerToFindAndReturnProject("PROJECT");
        expectCurrentUserToBeAllowedToAdministrateTheProject(true);

        action.doExecute();

        when(project.getId()).thenReturn(19L);
        when(projectSettingsService.loadSettings(19L)).thenReturn(null);

        assertThat(action.getProjectSettings(), is(nullValue()));
    }

    /* tests for getLicenseError() */

    @Test
    public void getLicenseErrorExposesLicenseErrorIfExists() {
        when(scrumPokerLicenseService.getLicenseError()).thenReturn(LICENSE_ERROR);
        assertThat(action.getLicenseError(), is(equalTo(LICENSE_ERROR)));
        verify(scrumPokerLicenseService, times(1)).getLicenseError();
        verifyNoMoreInteractions(scrumPokerLicenseService);
    }

    @Test
    public void getLicenseErrorReturnsNullIfNoLicenseErrorExists() {
        when(scrumPokerLicenseService.getLicenseError()).thenReturn(null);
        assertThat(action.getLicenseError(), is(nullValue()));
        verify(scrumPokerLicenseService, times(1)).getLicenseError();
        verifyNoMoreInteractions(scrumPokerLicenseService);
    }

    /* tests for doDefault() */

    @Test
    public void doDefaultShouldReturnErrorIfProjectIsMissing() {
        expectHttpParametersToContainProjectKey("PRJ");
        expectProjectManagerToNotFindTheProject("PRJ");

        assertThat(action.doDefault(), is(equalTo(ERROR)));
        verify(errorLogService, times(1)).logError(anyString());
        verifyNoMoreInteractions(errorLogService);
    }

    @Test
    public void doDefaultShouldReturnErrorIfUserIsMissingProjectAdministrationPermission() {
        expectHttpParametersToContainProjectKey("YAY");
        expectProjectManagerToFindAndReturnProject("YAY");
        expectCurrentUserToBeAllowedToAdministrateTheProject(false);

        assertThat(action.doDefault(), is(equalTo(ERROR)));
        verify(errorLogService, times(1)).logError(anyString());
        verifyNoMoreInteractions(errorLogService);
    }

    /* tests for doExecute() */

    @Test
    public void doExecuteShouldReturnErrorIfProjectIsMissing() {
        expectHttpParametersToContainProjectKey("PRJ");
        expectProjectManagerToNotFindTheProject("PRJ");

        assertThat(action.doExecute(), is(equalTo(ERROR)));
        verify(errorLogService, times(1)).logError(anyString());
        verifyNoMoreInteractions(errorLogService);
    }

    @Test
    public void doExecuteShouldReturnErrorIfUserIsMissingProjectAdministrationPermission() {
        expectHttpParametersToContainProjectKey("YAY");
        expectProjectManagerToFindAndReturnProject("YAY");
        expectCurrentUserToBeAllowedToAdministrateTheProject(false);

        assertThat(action.doExecute(), is(equalTo(ERROR)));
        verify(errorLogService, times(1)).logError(anyString());
        verifyNoMoreInteractions(errorLogService);
    }

    @Test
    public void doExecuteShouldReturnSuccessIfProjectExistsAndNoActionIsGiven() {
        expectHttpParametersToContainProjectKey("PRJ");
        expectProjectManagerToFindAndReturnProject("PRJ");
        expectProjectToHaveProjectId(18L);
        expectCurrentUserToBeAllowedToAdministrateTheProject(true);

        assertThat(action.doExecute(), is(equalTo(SUCCESS)));
    }

    @Test
    public void doExecuteShouldResetProjectSpecificSettings() {
        expectHttpParametersToContainProjectKey("PRJ");
        expectProjectManagerToFindAndReturnProject("PRJ");
        expectHttpParametersToContainAction(Actions.DEFAULTS);
        expectProjectToHaveProjectId(18L);
        expectCurrentUserToBeAllowedToAdministrateTheProject(true);

        assertThat(action.doExecute(), is(equalTo(SUCCESS)));
        verify(projectSettingsService, times(1)).removeSettings(18L);
        verifyNoMoreInteractions(projectSettingsService);
    }

    @Test
    public void doExecuteShouldSaveProjectSpecificSettings() {
        expectHttpParametersToContainProjectKey("YAY");
        expectProjectManagerToFindAndReturnProject("YAY");
        expectProjectToHaveProjectId(18L);
        expectHttpParametersToContainAction(Actions.SAVE);
        expectConfigurationParametersToBeSubmitted(false, "POINTS", "5,8,13");
        expectCurrentUserToBeAllowedToAdministrateTheProject(true);

        assertThat(action.doExecute(), is(equalTo(SUCCESS)));
        verify(projectSettingsService, times(1)).persistSettings(18L, false, "POINTS", "5,8,13");
        verifyNoMoreInteractions(projectSettingsService);
    }

    @Test
    public void doExecuteShouldNotSaveEmptyStringsForOptionalSettings() {
        expectHttpParametersToContainProjectKey("YAY");
        expectProjectManagerToFindAndReturnProject("YAY");
        expectProjectToHaveProjectId(18L);
        expectHttpParametersToContainAction(Actions.SAVE);
        expectConfigurationParametersToBeSubmitted(true, "", "");
        expectCurrentUserToBeAllowedToAdministrateTheProject(true);

        assertThat(action.doExecute(), is(equalTo(SUCCESS)));
        verify(projectSettingsService, times(1)).persistSettings(18L, true, null, null);
        verifyNoMoreInteractions(projectSettingsService);
    }

    /* tests for getEstimateFields() */

    @Test
    public void getEstimateFieldsShouldDelegateToEstimateService() {
        ArrayList<CustomField> expectedCustomFields = new ArrayList<>();
        when(estimateFieldService.supportedCustomFields()).thenReturn(expectedCustomFields);

        assertThat(action.getEstimateFields(), is(equalTo(expectedCustomFields)));
        verify(estimateFieldService, times(1)).supportedCustomFields();
        verifyNoMoreInteractions(estimateFieldService);
    }

    /* tests for getDocumentationUrl() */

    @Test
    public void getDocumentationUrlShouldExposeLink() {
        assertThat(action.getDocumentationUrl(), is(equalTo(ScrumPokerConstants.CONFIGURATION_DOCUMENTATION)));
    }

    /* supporting methods */

    private void expectHttpParametersToContainProjectKey(String projectKey) {
        when(httpServletVariables.getHttpRequest()).thenReturn(httpServletRequest);
        when(httpServletRequest.getParameterValues(Parameters.PROJECT_KEY)).thenReturn(new String[]{projectKey});
    }

    private void expectHttpParametersToContainAction(String action) {
        when(httpServletVariables.getHttpRequest()).thenReturn(httpServletRequest);
        when(httpServletRequest.getParameterValues(Parameters.ACTION)).thenReturn(new String[]{action});
    }

    private void expectConfigurationParametersToBeSubmitted(boolean scrumPokerActivated, String estimateField, String cardSet) {
        when(httpServletVariables.getHttpRequest()).thenReturn(httpServletRequest);
        when(httpServletRequest.getParameterValues(Parameters.ACTIVATE_SCRUM_POKER)).thenReturn(new String[]{Boolean.toString(scrumPokerActivated)});
        when(httpServletRequest.getParameterValues(Parameters.ESTIMATE_FIELD)).thenReturn(new String[]{estimateField});
        when(httpServletRequest.getParameterValues(Parameters.CARD_SET)).thenReturn(new String[]{cardSet});
    }

    private void expectProjectManagerToFindAndReturnProject(String projectKey) {
        when(projectManager.getProjectByCurrentKey(eq(projectKey))).thenReturn(project);
    }

    private void expectProjectManagerToNotFindTheProject(String projectKey) {
        when(projectManager.getProjectByCurrentKey(eq(projectKey))).thenReturn(null);
    }

    private void expectProjectToHaveProjectId(long projectId) {
        when(project.getId()).thenReturn(projectId);
    }

    private void expectCurrentUserToBeAllowedToAdministrateTheProject(boolean allowedToAdministrate) {
        when(jiraAuthenticationContext.getLoggedInUser()).thenReturn(applicationUser);
        when(projectAdministrationCondition.userIsAllowedToAdministrateProject(applicationUser, project)).thenReturn(allowedToAdministrate);
    }

}
