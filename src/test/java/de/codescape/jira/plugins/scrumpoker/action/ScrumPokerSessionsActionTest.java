package de.codescape.jira.plugins.scrumpoker.action;

import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.user.ApplicationUser;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerSession;
import de.codescape.jira.plugins.scrumpoker.helper.ScrumPokerPermissions;
import de.codescape.jira.plugins.scrumpoker.rest.entities.SessionEntity;
import de.codescape.jira.plugins.scrumpoker.rest.mapper.SessionEntityMapper;
import de.codescape.jira.plugins.scrumpoker.service.ScrumPokerLicenseService;
import de.codescape.jira.plugins.scrumpoker.service.ScrumPokerSessionService;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

public class ScrumPokerSessionsActionTest {

    private static final String LICENSE_ERROR = "license.error";
    private static final String SECRET_ISSUE_KEY = "SECRET-1";
    private static final String PUBLIC_ISSUE_KEY = "PUBLIC-1";

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private ScrumPokerPermissions scrumPokerPermissions;

    @Mock
    private JiraAuthenticationContext jiraAuthenticationContext;

    @Mock
    private ScrumPokerSessionService scrumPokerSessionService;

    @Mock
    private IssueManager issueManager;

    @Mock
    private SessionEntityMapper sessionEntityMapper;

    @Mock
    private ScrumPokerLicenseService scrumPokerLicenseService;

    @InjectMocks
    private ScrumPokerSessionsAction action;

    @Mock
    private MutableIssue secretIssue;

    @Mock
    private MutableIssue publicIssue;

    @Mock
    private ApplicationUser applicationUser;

    @Mock
    private ScrumPokerSession secretScrumPokerSession;

    @Mock
    private ScrumPokerSession publicScrumPokerSession;

    @Mock
    private SessionEntity publicSessionEntity;

    /* tests for getOpenSessions() */

    @Test
    public void openSessionsShouldOnlyReturnIssuesTheUserIsAllowedToSee() {
        expectOneVisibleAndOneSecretIssue();
        when(publicScrumPokerSession.getConfirmedEstimate()).thenReturn(null);
        when(secretScrumPokerSession.getConfirmedEstimate()).thenReturn(null);

        List<SessionEntity> openSessions = action.getOpenSessions();
        assertThat(openSessions, hasSize(1));
        assertThat(openSessions.getFirst(), is(equalTo(publicSessionEntity)));
        verify(scrumPokerSessionService).recent();
    }

    /* tests for getClosedSessions() */

    @Test
    public void closedSessionsShouldOnlyReturnIssuesTheUserIsAllowedToSee() {
        expectOneVisibleAndOneSecretIssue();
        when(publicScrumPokerSession.getConfirmedEstimate()).thenReturn("5");
        when(secretScrumPokerSession.getConfirmedEstimate()).thenReturn("8");

        List<SessionEntity> closedSessions = action.getClosedSessions();
        assertThat(closedSessions, hasSize(1));
        assertThat(closedSessions.getFirst(), is(equalTo(publicSessionEntity)));
        verify(scrumPokerSessionService).recent();
    }

    /* tests for doExecute() */

    @Test
    public void shouldAlwaysShowSessionsPage() {
        assertThat(action.doExecute(), is(equalTo("success")));
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

    /* supporting methods */

    private void expectOneVisibleAndOneSecretIssue() {
        when(secretScrumPokerSession.getIssueKey()).thenReturn(SECRET_ISSUE_KEY);
        when(publicScrumPokerSession.getIssueKey()).thenReturn(PUBLIC_ISSUE_KEY);

        ArrayList<ScrumPokerSession> scrumPokerSessions = new ArrayList<>();
        scrumPokerSessions.add(secretScrumPokerSession);
        scrumPokerSessions.add(publicScrumPokerSession);

        when(scrumPokerSessionService.recent()).thenReturn(scrumPokerSessions);

        when(jiraAuthenticationContext.getLoggedInUser()).thenReturn(applicationUser);
        when(applicationUser.getKey()).thenReturn("USER-1");

        when(sessionEntityMapper.build(publicScrumPokerSession, "USER-1")).thenReturn(publicSessionEntity);

        when(issueManager.getIssueObject(SECRET_ISSUE_KEY)).thenReturn(secretIssue);
        when(scrumPokerPermissions.currentUserIsAllowedToSeeIssue(SECRET_ISSUE_KEY)).thenReturn(false);

        when(issueManager.getIssueObject(PUBLIC_ISSUE_KEY)).thenReturn(publicIssue);
        when(scrumPokerPermissions.currentUserIsAllowedToSeeIssue(PUBLIC_ISSUE_KEY)).thenReturn(true);
    }

}
