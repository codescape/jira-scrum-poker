package net.congstar.jira.plugins.scrumpoker.action;

import com.atlassian.jira.junit.rules.AvailableInContainer;
import com.atlassian.jira.junit.rules.MockitoContainer;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.web.HttpServletVariables;
import com.atlassian.jira.web.action.RedirectSanitiser;
import net.congstar.jira.plugins.scrumpoker.data.ScrumPokerStorage;
import net.congstar.jira.plugins.scrumpoker.model.ScrumPokerSession;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import javax.servlet.http.HttpServletRequest;

import static net.congstar.jira.plugins.scrumpoker.action.ChooseCardAction.PARAM_CHOSEN_CARD;
import static net.congstar.jira.plugins.scrumpoker.action.ScrumPokerAction.PARAM_ISSUE_KEY;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class ChooseCardActionTest {

    private static final String ISSUE_KEY = "ISSUE-42";
    private static final String CHOSEN_CARD = "5";
    private static final String USER_KEY = "someUserKey";

    @Rule
    public MockitoContainer rule = new MockitoContainer(this);

    @InjectMocks
    private ChooseCardAction chooseCardAction;

    @Mock
    @AvailableInContainer
    private JiraAuthenticationContext jiraAuthenticationContext;

    @Mock
    @AvailableInContainer
    private RedirectSanitiser redirectSanitiser;

    @Mock
    private ScrumPokerStorage scrumPokerStorage;

    @Mock
    private HttpServletVariables httpServletVariables;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private ApplicationUser applicationUser;

    @Mock
    private ScrumPokerSession scrumPokerSession;

    @Test
    public void shouldUpdateSessionWithChosenCardForUser() throws Exception {
        when(httpServletVariables.getHttpRequest()).thenReturn(httpServletRequest);
        when(httpServletRequest.getParameter(PARAM_ISSUE_KEY)).thenReturn(ISSUE_KEY);
        when(httpServletRequest.getParameter(PARAM_CHOSEN_CARD)).thenReturn(CHOSEN_CARD);
        when(jiraAuthenticationContext.getLoggedInUser()).thenReturn(applicationUser);
        when(applicationUser.getKey()).thenReturn(USER_KEY);
        when(scrumPokerStorage.sessionForIssue(ISSUE_KEY, USER_KEY)).thenReturn(scrumPokerSession);
        when(redirectSanitiser.makeSafeRedirectUrl(anyString())).thenReturn("someURL");

        String returnValue = chooseCardAction.doExecute();

        verify(scrumPokerSession, times(1)).updateCard(USER_KEY, CHOSEN_CARD);
        assertThat(returnValue, is(equalTo("success")));
    }

}
