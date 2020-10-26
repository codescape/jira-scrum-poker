package de.codescape.jira.plugins.scrumpoker.action;

import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.junit.rules.AvailableInContainer;
import com.atlassian.jira.junit.rules.MockitoContainer;
import com.atlassian.jira.junit.rules.MockitoMocksInContainer;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.project.ProjectManager;
import com.atlassian.jira.web.HttpServletVariables;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerProject;
import de.codescape.jira.plugins.scrumpoker.model.GlobalSettings;
import de.codescape.jira.plugins.scrumpoker.service.ErrorLogService;
import de.codescape.jira.plugins.scrumpoker.service.EstimateFieldService;
import de.codescape.jira.plugins.scrumpoker.service.GlobalSettingsService;
import de.codescape.jira.plugins.scrumpoker.service.ProjectSettingsService;
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

    /* tests for getProjectKey() */

    @Test
    public void shouldExposeTheProjectKeyWhenCalled() {
        when(httpServletVariables.getHttpRequest()).thenReturn(httpServletRequest);
        when(httpServletRequest.getParameterValues(Parameters.PROJECT_KEY)).thenReturn(new String[]{"ABC"});
        when(projectManager.getProjectByCurrentKey(eq("ABC"))).thenReturn(project);

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

        action.doExecute();

        when(project.getId()).thenReturn(42L);
        when(projectSettingsService.loadSettings(eq(42L))).thenReturn(scrumPokerProject);

        assertThat(action.getProjectSettings(), is(equalTo(scrumPokerProject)));
    }

    @Test
    public void getProjectSettingsShouldReturnNullIfNoneExists() {
        expectHttpParametersToContainProjectKey("PROJECT");
        expectProjectManagerToFindAndReturnProject("PROJECT");

        action.doExecute();

        when(project.getId()).thenReturn(19L);
        when(projectSettingsService.loadSettings(19L)).thenReturn(null);

        assertThat(action.getProjectSettings(), is(nullValue()));
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
    public void doExecuteShouldReturnSuccessIfProjectExistsAndNoActionIsGiven() {
        expectHttpParametersToContainProjectKey("PRJ");
        expectProjectManagerToFindAndReturnProject("PRJ");
        expectProjectToHaveProjectId(18L);

        assertThat(action.doExecute(), is(equalTo(SUCCESS)));
    }

    @Test
    public void doExecuteShouldResetProjectSpecificSettings() {
        expectHttpParametersToContainProjectKey("PRJ");
        expectProjectManagerToFindAndReturnProject("PRJ");
        expectHttpParametersToContainAction(Actions.DEFAULTS);
        expectProjectToHaveProjectId(18L);

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

        assertThat(action.doExecute(), is(equalTo(SUCCESS)));
        verify(projectSettingsService, times(1))
            .persistSettings(18L, false, "POINTS", "5,8,13");
        verifyNoMoreInteractions(projectSettingsService);
    }

    @Test
    public void doExecuteShouldNotSaveEmptyStringsForOptionalSettings() {
        expectHttpParametersToContainProjectKey("YAY");
        expectProjectManagerToFindAndReturnProject("YAY");
        expectProjectToHaveProjectId(18L);
        expectHttpParametersToContainAction(Actions.SAVE);
        expectConfigurationParametersToBeSubmitted(true, "", "");

        assertThat(action.doExecute(), is(equalTo(SUCCESS)));
        verify(projectSettingsService, times(1))
            .persistSettings(18L, true, null, null);
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

    /* supporting methods */

    private void expectHttpParametersToContainProjectKey(String projectKey) {
        when(httpServletVariables.getHttpRequest()).thenReturn(httpServletRequest);
        when(httpServletRequest.getParameterValues(Parameters.PROJECT_KEY))
            .thenReturn(new String[]{projectKey});
    }

    private void expectHttpParametersToContainAction(String action) {
        when(httpServletVariables.getHttpRequest()).thenReturn(httpServletRequest);
        when(httpServletRequest.getParameterValues(Parameters.ACTION))
            .thenReturn(new String[]{action});
    }

    private void expectConfigurationParametersToBeSubmitted(boolean scrumPokerActivated, String estimateField,
                                                            String cardSet) {
        when(httpServletVariables.getHttpRequest()).thenReturn(httpServletRequest);
        when(httpServletRequest.getParameterValues(Parameters.ACTIVATE_SCRUM_POKER))
            .thenReturn(new String[]{Boolean.toString(scrumPokerActivated)});
        when(httpServletRequest.getParameterValues(Parameters.ESTIMATE_FIELD))
            .thenReturn(new String[]{estimateField});
        when(httpServletRequest.getParameterValues(Parameters.CARD_SET))
            .thenReturn(new String[]{cardSet});
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

}
