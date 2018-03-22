package de.codescape.jira.plugins.scrumpoker.action;

import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.web.HttpServletVariables;
import de.codescape.jira.plugins.scrumpoker.data.ScrumPokerStorage;
import de.codescape.jira.plugins.scrumpoker.model.ScrumPokerSession;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import javax.servlet.http.HttpServletRequest;

import static com.atlassian.jira.permission.ProjectPermissions.BROWSE_PROJECTS;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

public class StartScrumPokerActionTest {

    private static final String ISSUE_KEY = "ISSUE-1";

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private IssueManager issueManager;

    @Mock
    private HttpServletVariables httpServletVariables;

    @Mock
    private ScrumPokerStorage scrumPokerStorage;

    @Mock
    private PermissionManager permissionManager;

    @Mock
    private JiraAuthenticationContext jiraAuthenticationContext;

    @InjectMocks
    private StartScrumPokerAction startScrumPokerAction;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private MutableIssue issue;

    @Mock
    private ApplicationUser user;

    @Mock
    private ScrumPokerSession scrumPokerSession;

    @Before
    public void before() {
        when(httpServletVariables.getHttpRequest()).thenReturn(httpServletRequest);
        when(jiraAuthenticationContext.getLoggedInUser()).thenReturn(user);
        when(user.getKey()).thenReturn("someUserKey");
        when(scrumPokerStorage.sessionForIssue(ISSUE_KEY, user.getKey())).thenReturn(scrumPokerSession);
    }

    @Test
    public void shouldDisplayErrorPageWhenUserHasNoPermissionForIssue() {
        when(httpServletRequest.getParameter("action")).thenReturn("start");
        whenRequestedIssueExists();
        whenUserIsNotAllowedToSeeIssue();

        assertThat(startScrumPokerAction.doExecute(), is(equalTo("error")));
    }

    @Test
    public void shouldDisplayStartPageWhenUserHasPermissionForIssue() {
        when(httpServletRequest.getParameter("action")).thenReturn("start");
        whenRequestedIssueExists();
        whenUserIsAllowedToSeeIssue();

        assertThat(startScrumPokerAction.doExecute(), is(equalTo("start")));
    }

    private void whenUserIsAllowedToSeeIssue() {
        when(permissionManager.hasPermission(BROWSE_PROJECTS, issue, user)).thenReturn(true);
    }

    private void whenUserIsNotAllowedToSeeIssue() {
        when(permissionManager.hasPermission(BROWSE_PROJECTS, issue, user)).thenReturn(false);
    }

    private void whenRequestedIssueExists() {
        when(httpServletRequest.getParameter("issueKey")).thenReturn(ISSUE_KEY);
        when(issueManager.getIssueObject(ISSUE_KEY)).thenReturn(issue);
    }

}
