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
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
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

    @Test
    public void shouldExposeTheProjectKeyWhenCalled() {
        when(httpServletVariables.getHttpRequest()).thenReturn(httpServletRequest);
        when(httpServletRequest.getParameterValues(ScrumPokerProjectConfigurationAction.Parameters.PROJECT_KEY)).thenReturn(new String[]{"ABC"});
        when(projectManager.getProjectByCurrentKey(eq("ABC"))).thenReturn(project);

        scrumPokerProjectConfigurationAction.doExecute();

        assertThat(scrumPokerProjectConfigurationAction.getProjectKey(), is(equalTo("ABC")));
    }

    @Test
    public void shouldReturnGlobalActivateScrumPokerFlag() {
        when(globalSettingsService.load()).thenReturn(globalSettings);
        when(globalSettings.isActivateScrumPoker()).thenReturn(true);

        assertThat(scrumPokerProjectConfigurationAction.isActivateScrumPokerGlobally(), is(true));
    }

    @Test
    public void shouldReturnProjectSpecificConfiguration() {
        when(httpServletVariables.getHttpRequest()).thenReturn(httpServletRequest);
        when(httpServletRequest.getParameterValues(ScrumPokerProjectConfigurationAction.Parameters.PROJECT_KEY)).thenReturn(new String[]{"ABC"});
        when(projectManager.getProjectByCurrentKey(eq("ABC"))).thenReturn(project);

        scrumPokerProjectConfigurationAction.doExecute();

        when(project.getId()).thenReturn(42L);
        when(projectSettingsService.loadSettings(eq(42L))).thenReturn(scrumPokerProject);

        assertThat(scrumPokerProjectConfigurationAction.getProjectSettings(), is(equalTo(scrumPokerProject)));
    }

}
