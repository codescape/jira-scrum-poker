package de.codescape.jira.plugins.scrumpoker.action;

import com.atlassian.jira.junit.rules.AvailableInContainer;
import com.atlassian.jira.junit.rules.MockitoContainer;
import com.atlassian.jira.junit.rules.MockitoMocksInContainer;
import com.atlassian.jira.web.HttpServletVariables;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerError;
import de.codescape.jira.plugins.scrumpoker.service.ScrumPokerErrorService;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class ShowErrorLogActionTest {

    @Rule
    public MockitoContainer mockitoContainer = MockitoMocksInContainer.rule(this);

    @Mock
    @AvailableInContainer
    private HttpServletVariables httpServletVariables;

    @Mock
    private ScrumPokerErrorService scrumPokerErrorService;

    @InjectMocks
    private ShowErrorLogAction showErrorLogAction;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Test
    public void shouldExposeTheErrorList() {
        List<ScrumPokerError> scrumPokerErrors = new ArrayList<>();
        when(scrumPokerErrorService.listAll()).thenReturn(scrumPokerErrors);
        assertThat(showErrorLogAction.getErrorList(), is(equalTo(scrumPokerErrors)));
    }

    @Test
    public void shouldEmptyTheErrorLogWhenRequested() {
        when(httpServletVariables.getHttpRequest()).thenReturn(httpServletRequest);
        when(httpServletRequest.getParameter(ShowErrorLogAction.Parameters.ACTION)).thenReturn("empty");
        assertThat(showErrorLogAction.doExecute(), is(equalTo(showErrorLogAction.SUCCESS)));
        verify(scrumPokerErrorService, times(1)).emptyErrorLog();
    }

    @Test
    public void shouldNotEmptyTheErrorWhenNotRequested() {
        when(httpServletVariables.getHttpRequest()).thenReturn(httpServletRequest);
        when(httpServletRequest.getParameter(ShowErrorLogAction.Parameters.ACTION)).thenReturn(null);
        assertThat(showErrorLogAction.doExecute(), is(equalTo(showErrorLogAction.SUCCESS)));
        verify(scrumPokerErrorService, never()).emptyErrorLog();
    }

}
