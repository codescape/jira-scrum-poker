package de.codescape.jira.plugins.scrumpoker.action;

import com.atlassian.jira.junit.rules.AvailableInContainer;
import com.atlassian.jira.junit.rules.MockitoContainer;
import com.atlassian.jira.junit.rules.MockitoMocksInContainer;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.project.ProjectManager;
import com.atlassian.jira.web.HttpServletVariables;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerProject;
import de.codescape.jira.plugins.scrumpoker.model.GlobalSettings;
import de.codescape.jira.plugins.scrumpoker.service.GlobalSettingsService;
import de.codescape.jira.plugins.scrumpoker.service.ProjectSettingsService;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import javax.servlet.http.HttpServletRequest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class ScrumPokerProjectConfigurationActionTest {

    @Rule
    public MockitoContainer mockitoContainer = MockitoMocksInContainer.rule(this);

    @Mock
    private GlobalSettingsService globalSettingsService;

    @Mock
    private ProjectSettingsService projectSettingsService;

    @Mock
    private ProjectManager projectManager;

    @Mock
    @AvailableInContainer
    private HttpServletVariables httpServletVariables;

    @InjectMocks
    private ScrumPokerProjectConfigurationAction scrumPokerProjectConfigurationAction;

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
        when(httpServletRequest.getParameterValues(ScrumPokerProjectConfigurationAction.Parameters.PROJECT_KEY)).thenReturn(new String[]{"ABC"});
        when(projectManager.getProjectByCurrentKey(eq("ABC"))).thenReturn(project);

        scrumPokerProjectConfigurationAction.doExecute();

        assertThat(scrumPokerProjectConfigurationAction.getProjectKey(), is(equalTo("ABC")));
    }

    /* tests for isActivateScrumPokerGlobally() */

    @Test
    public void shouldReturnGlobalActivateScrumPokerFlag() {
        when(globalSettingsService.load()).thenReturn(globalSettings);
        when(globalSettings.isActivateScrumPoker()).thenReturn(true);

        assertThat(scrumPokerProjectConfigurationAction.isActivateScrumPokerGlobally(), is(true));
    }

    /* tests for getProjectSettings() */

    @Test
    public void getProjectSettingsShouldReturnProjectSpecificConfigurationIfExists() {
        expectHttpParametersToContainProjectKey("ABC");
        expectProjectManagerToFindAndReturnProject("ABC");

        scrumPokerProjectConfigurationAction.doExecute();

        when(project.getId()).thenReturn(42L);
        when(projectSettingsService.loadSettings(eq(42L))).thenReturn(scrumPokerProject);

        assertThat(scrumPokerProjectConfigurationAction.getProjectSettings(), is(equalTo(scrumPokerProject)));
    }

    @Test
    public void getProjectSettingsShouldReturnNullIfNoneExists() {
        expectHttpParametersToContainProjectKey("PROJECT");
        expectProjectManagerToFindAndReturnProject("PROJECT");

        scrumPokerProjectConfigurationAction.doExecute();

        when(project.getId()).thenReturn(19L);
        when(projectSettingsService.loadSettings(19L)).thenReturn(null);

        assertThat(scrumPokerProjectConfigurationAction.getProjectSettings(), is(nullValue()));
    }

    /* supporting methods */

    private void expectHttpParametersToContainProjectKey(String projectKey) {
        when(httpServletVariables.getHttpRequest()).thenReturn(httpServletRequest);
        when(httpServletRequest.getParameterValues(ScrumPokerProjectConfigurationAction.Parameters.PROJECT_KEY))
            .thenReturn(new String[]{projectKey});
    }

    private void expectProjectManagerToFindAndReturnProject(String projectKey) {
        when(projectManager.getProjectByCurrentKey(eq(projectKey))).thenReturn(project);
    }

}
