package de.codescape.jira.plugins.scrumpoker.action;

import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.jira.user.ApplicationUser;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerSession;
import de.codescape.jira.plugins.scrumpoker.service.SessionEntityTransformer;
import de.codescape.jira.plugins.scrumpoker.rest.entities.SessionEntity;
import de.codescape.jira.plugins.scrumpoker.service.ScrumPokerSessionService;
import org.junit.Before;
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

public class ShowSessionsActionTest {

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
    private  SessionEntityTransformer sessionEntityTransformer;

    @InjectMocks
    private ShowSessionsAction showSessionsAction;

    @Mock
    private MutableIssue secretIssue;

    @Mock
    private MutableIssue publicIssue;

    @Mock
    private ApplicationUser applicationUser;

    @Mock
    private ScrumPokerSession secretScrumPokerSession;

    @Mock
    private SessionEntity secretSessionEntity;

    @Mock
    private ScrumPokerSession publicScrumPokerSession;

    @Mock
    private SessionEntity publicSessionEntity;

    @Before
    public void before() {
        when(secretScrumPokerSession.getIssueKey()).thenReturn("SECRET");
        when(secretSessionEntity.getIssueKey()).thenReturn("SECRET");

        when(publicScrumPokerSession.getIssueKey()).thenReturn("PUBLIC");
        when(publicSessionEntity.getIssueKey()).thenReturn("PUBLIC");

        ArrayList<ScrumPokerSession> scrumPokerSessions = new ArrayList<>();
        scrumPokerSessions.add(secretScrumPokerSession);
        scrumPokerSessions.add(publicScrumPokerSession);

        when(scrumPokerSessionService.recent()).thenReturn(scrumPokerSessions);

        when(jiraAuthenticationContext.getLoggedInUser()).thenReturn(applicationUser);
        when(applicationUser.getKey()).thenReturn("USER-1");

        when(sessionEntityTransformer.build(secretScrumPokerSession, "USER-1")).thenReturn(secretSessionEntity);
        when(sessionEntityTransformer.build(publicScrumPokerSession, "USER-1")).thenReturn(publicSessionEntity);

        when(issueManager.getIssueObject("SECRET")).thenReturn(secretIssue);
        when(issueManager.getIssueObject("PUBLIC")).thenReturn(publicIssue);

        when(permissionManager.hasPermission(BROWSE_PROJECTS, secretIssue, applicationUser)).thenReturn(false);
        when(permissionManager.hasPermission(BROWSE_PROJECTS, publicIssue, applicationUser)).thenReturn(true);
    }

    @Test
    public void openSessionsShouldOnlyReturnIssuesTheUserIsAllowedToSee() {
        when(publicScrumPokerSession.getConfirmedVote()).thenReturn(null);
        when(secretScrumPokerSession.getConfirmedVote()).thenReturn(null);

        List<SessionEntity> openSessions = showSessionsAction.getOpenSessions();
        assertThat(openSessions, hasSize(1));
        assertThat(openSessions.get(0).getIssueKey(), is(equalTo("PUBLIC")));
        verify(scrumPokerSessionService).recent();
    }

    @Test
    public void closedSessionsShouldOnlyReturnIssuesTheUserIsAllowedToSee() {
        when(publicScrumPokerSession.getConfirmedVote()).thenReturn(5);
        when(secretScrumPokerSession.getConfirmedVote()).thenReturn(8);

        List<SessionEntity> closedSessions = showSessionsAction.getClosedSessions();
        assertThat(closedSessions, hasSize(1));
        assertThat(closedSessions.get(0).getIssueKey(), is(equalTo("PUBLIC")));
        verify(scrumPokerSessionService).recent();
    }

    @Test
    public void shouldAlwaysShowSessionsPage() {
        assertThat(showSessionsAction.doExecute(), is(equalTo("success")));
    }

}
