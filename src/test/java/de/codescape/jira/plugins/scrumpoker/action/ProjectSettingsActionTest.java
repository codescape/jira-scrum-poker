package de.codescape.jira.plugins.scrumpoker.action;

import com.atlassian.jira.junit.rules.AvailableInContainer;
import com.atlassian.jira.junit.rules.MockitoContainer;
import com.atlassian.jira.junit.rules.MockitoMocksInContainer;
import com.atlassian.jira.web.HttpServletVariables;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import javax.servlet.http.HttpServletRequest;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

public class ProjectSettingsActionTest {

    @Rule
    public MockitoContainer mockitoContainer = MockitoMocksInContainer.rule(this);

    @Mock
    @AvailableInContainer
    private HttpServletVariables httpServletVariables;

    @InjectMocks
    private ProjectSettingsAction projectSettingsAction;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Test
    public void shouldExposeTheProjectKeyWhenCalled() {
        when(httpServletVariables.getHttpRequest()).thenReturn(httpServletRequest);
        when(httpServletRequest.getParameter(ProjectSettingsAction.Parameters.PROJECT_KEY)).thenReturn("ABC");
        projectSettingsAction.doExecute();
        assertThat(projectSettingsAction.getProjectKey(), is(equalTo("ABC")));
    }

}
