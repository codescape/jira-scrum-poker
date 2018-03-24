package de.codescape.jira.plugins.scrumpoker.action;

import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.user.util.UserManager;
import de.codescape.jira.plugins.scrumpoker.data.ScrumPokerStorage;
import de.codescape.jira.plugins.scrumpoker.model.ScrumPokerSession;
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

    private static final String UNKNOWN_USER_KEY = "unknownUserKey";

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private PermissionManager permissionManager;

    @Mock
    private ScrumPokerStorage scrumPokerStorage;

    @Mock
    private JiraAuthenticationContext jiraAuthenticationContext;

    @Mock
    private UserManager userManager;

    @Mock
    private IssueManager issueManager;

    @InjectMocks
    private ShowSessionsAction showSessionsAction;

    @Mock
    private MutableIssue secretIssue;

    @Mock
    private MutableIssue publicIssue;

    @Mock
    private ApplicationUser applicationUser;

    @Before
    public void before() {
        ArrayList<ScrumPokerSession> scrumPokerSessions = new ArrayList<>();
        scrumPokerSessions.add(new ScrumPokerSession("SECRET", "SomePerson"));
        scrumPokerSessions.add(new ScrumPokerSession("PUBLIC", "SomePerson"));

        when(showSessionsAction.getOpenSessions()).thenReturn(scrumPokerSessions);
        when(showSessionsAction.getClosedSessions()).thenReturn(scrumPokerSessions);

        when(issueManager.getIssueObject("SECRET")).thenReturn(secretIssue);
        when(issueManager.getIssueObject("PUBLIC")).thenReturn(publicIssue);

        when(jiraAuthenticationContext.getLoggedInUser()).thenReturn(applicationUser);

        when(permissionManager.hasPermission(BROWSE_PROJECTS, secretIssue, applicationUser)).thenReturn(false);
        when(permissionManager.hasPermission(BROWSE_PROJECTS, publicIssue, applicationUser)).thenReturn(true);
    }

    @Test
    public void openSessionsShouldOnlyReturnIssuesTheUserIsAllowedToSee() {
        List<ScrumPokerSession> openSessions = showSessionsAction.getOpenSessions();
        assertThat(openSessions, hasSize(1));
        assertThat(openSessions.get(0).getIssueKey(), is(equalTo("PUBLIC")));
        verify(scrumPokerStorage).getOpenSessions();
    }

    @Test
    public void closedSessionsShouldOnlyReturnIssuesTheUserIsAllowedToSee() {
        List<ScrumPokerSession> closedSessions = showSessionsAction.getClosedSessions();
        assertThat(closedSessions, hasSize(1));
        assertThat(closedSessions.get(0).getIssueKey(), is(equalTo("PUBLIC")));
        verify(scrumPokerStorage).getClosedSessions();
    }

    @Test
    public void shouldAlwaysShowSessionsPage() {
        assertThat(showSessionsAction.doExecute(), is(equalTo("success")));
    }

    @Test
    public void returnsUserKeyIfUserCannotBeFound() {
        when(userManager.getUserByKey(UNKNOWN_USER_KEY)).thenReturn(null);
        assertThat(showSessionsAction.getUsername(UNKNOWN_USER_KEY), is(equalTo(UNKNOWN_USER_KEY)));
    }

}
