package de.codescape.jira.plugins.scrumpoker.action;

import com.atlassian.jira.junit.rules.AvailableInContainer;
import com.atlassian.jira.junit.rules.MockitoContainer;
import com.atlassian.jira.junit.rules.MockitoMocksInContainer;
import com.atlassian.jira.web.HttpServletVariables;
import de.codescape.jira.plugins.scrumpoker.ScrumPokerConstants;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerError;
import de.codescape.jira.plugins.scrumpoker.service.ErrorLogService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

public class ScrumPokerErrorLogActionTest {

    @Rule
    public MockitoContainer mockitoContainer = MockitoMocksInContainer.rule(this);

    @Mock
    @AvailableInContainer
    private HttpServletVariables httpServletVariables;

    @Mock
    private ErrorLogService errorLogService;

    @InjectMocks
    private ScrumPokerErrorLogAction action;

    @Mock
    private HttpServletRequest httpServletRequest;

    /* tests for getErrorList() */

    @Test
    public void shouldExposeTheErrorList() {
        List<ScrumPokerError> scrumPokerErrors = new ArrayList<>();
        when(errorLogService.listAll()).thenReturn(scrumPokerErrors);
        assertThat(action.getErrorList(), is(equalTo(scrumPokerErrors)));
    }

    /* tests for doDefault() */

    @Test
    public void shouldAlwaysReturnThePageForViewing() {
        assertThat(action.doDefault(), is(equalTo(ScrumPokerErrorLogAction.SUCCESS)));
    }

    /* tests for doExecute() */

    @Test
    public void shouldEmptyTheErrorLogWhenRequested() {
        when(httpServletVariables.getHttpRequest()).thenReturn(httpServletRequest);
        when(httpServletRequest.getParameterValues(ScrumPokerErrorLogAction.Parameters.ACTION)).thenReturn(new String[]{"empty"});
        assertThat(action.doExecute(), is(equalTo(ScrumPokerErrorLogAction.SUCCESS)));
        verify(errorLogService, times(1)).emptyErrorLog();
    }

    @Test
    public void shouldNotEmptyTheErrorWhenNotRequested() {
        when(httpServletVariables.getHttpRequest()).thenReturn(httpServletRequest);
        when(httpServletRequest.getParameter(ScrumPokerErrorLogAction.Parameters.ACTION)).thenReturn(null);
        assertThat(action.doExecute(), is(equalTo(ScrumPokerErrorLogAction.SUCCESS)));
        verify(errorLogService, never()).emptyErrorLog();
    }

    /* tests for getDocumentationUrl() */

    @Test
    public void getDocumentationUrlShouldExposeLink() {
        assertThat(action.getDocumentationUrl(), is(equalTo(ScrumPokerConstants.ERROR_LOG_DOCUMENTATION)));
    }

}
