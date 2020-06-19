package de.codescape.jira.plugins.scrumpoker.action;

import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.jira.user.ApplicationUser;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerSession;
import de.codescape.jira.plugins.scrumpoker.rest.entities.SessionEntity;
import de.codescape.jira.plugins.scrumpoker.rest.mapper.SessionEntityMapper;
import de.codescape.jira.plugins.scrumpoker.service.ScrumPokerSessionService;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.List;

import static com.atlassian.jira.permission.ProjectPermissions.BROWSE_PROJECTS;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ScrumPokerSessionsActionTest {

    private static final String SECRET_ISSUE_KEY = "SECRET-1";
    private static final String PUBLIC_ISSUE_KEY = "PUBLIC-1";

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private PermissionManager permissionManager;

    @Mock
    private JiraAuthenticationContext jiraAuthenticationContext;

    @Mock
    private ScrumPokerSessionService scrumPokerSessionService;

    @Mock
    private IssueManager issueManager;

    @Mock
    private SessionEntityMapper sessionEntityMapper;

    @InjectMocks
    private ScrumPokerSessionsAction scrumPokerSessionsAction;

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

    @Test
    public void openSessionsShouldOnlyReturnIssuesTheUserIsAllowedToSee() {
        expectOneVisibleAndOneSecretIssue();
        when(publicScrumPokerSession.getConfirmedEstimate()).thenReturn(null);
        when(secretScrumPokerSession.getConfirmedEstimate()).thenReturn(null);

        List<SessionEntity> openSessions = scrumPokerSessionsAction.getOpenSessions();
        assertThat(openSessions, hasSize(1));
        assertThat(openSessions.get(0), is(equalTo(publicSessionEntity)));
        verify(scrumPokerSessionService).recent();
    }

    @Test
    public void closedSessionsShouldOnlyReturnIssuesTheUserIsAllowedToSee() {
        expectOneVisibleAndOneSecretIssue();
        when(publicScrumPokerSession.getConfirmedEstimate()).thenReturn("5");
        when(secretScrumPokerSession.getConfirmedEstimate()).thenReturn("8");

        List<SessionEntity> closedSessions = scrumPokerSessionsAction.getClosedSessions();
        assertThat(closedSessions, hasSize(1));
        assertThat(closedSessions.get(0), is(equalTo(publicSessionEntity)));
        verify(scrumPokerSessionService).recent();
    }

    @Test
    public void shouldAlwaysShowSessionsPage() {
        assertThat(scrumPokerSessionsAction.doExecute(), is(equalTo("success")));
    }

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
        when(permissionManager.hasPermission(BROWSE_PROJECTS, secretIssue, applicationUser)).thenReturn(false);

        when(issueManager.getIssueObject(PUBLIC_ISSUE_KEY)).thenReturn(publicIssue);
        when(permissionManager.hasPermission(BROWSE_PROJECTS, publicIssue, applicationUser)).thenReturn(true);
    }

}
