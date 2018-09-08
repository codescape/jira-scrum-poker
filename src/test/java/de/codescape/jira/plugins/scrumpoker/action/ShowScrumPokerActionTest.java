package de.codescape.jira.plugins.scrumpoker.action;

import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.web.HttpServletVariables;
import de.codescape.jira.plugins.scrumpoker.condition.ScrumPokerForIssueCondition;
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

public class ShowScrumPokerActionTest {

    private static final String ISSUE_KEY = "ISSUE-1";

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private IssueManager issueManager;

    @Mock
    private HttpServletVariables httpServletVariables;

    @Mock
    private PermissionManager permissionManager;

    @Mock
    private JiraAuthenticationContext jiraAuthenticationContext;

    @Mock
    private ScrumPokerForIssueCondition scrumPokerForIssueCondition;

    @InjectMocks
    private ShowScrumPokerAction showScrumPokerAction;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private MutableIssue issue;

    @Mock
    private ApplicationUser user;

    @Before
    public void before() {
        when(httpServletVariables.getHttpRequest()).thenReturn(httpServletRequest);
        when(jiraAuthenticationContext.getLoggedInUser()).thenReturn(user);
        when(user.getKey()).thenReturn("someUserKey");
    }

    @Test
    public void shouldDisplayStartPageWhenUserHasPermissionForIssueAndIssueIsEstimable() {
        whenRequestedIssueExists();
        whenUserIsAllowedToSeeIssue();
        whenIssueIsEstimable();

        assertThat(showScrumPokerAction.doExecute(), is(equalTo("success")));
    }

    @Test
    public void shouldDisplayErrorPageWhenUserHasNoPermissionForIssue() {
        whenRequestedIssueExists();
        whenUserIsNotAllowedToSeeIssue();

        assertThat(showScrumPokerAction.doExecute(), is(equalTo("error")));
    }

    @Test
    public void shouldDisplayErrorPageWhenIssueIsNotEstimable() {
        whenRequestedIssueExists();
        whenUserIsAllowedToSeeIssue();
        whenIssueIsNotEstimable();

        assertThat(showScrumPokerAction.doExecute(), is(equalTo("error")));
    }

    private void whenIssueIsNotEstimable() {
        when(scrumPokerForIssueCondition.isEstimable(issue)).thenReturn(false);
    }

    private void whenIssueIsEstimable() {
        when(scrumPokerForIssueCondition.isEstimable(issue)).thenReturn(true);
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
