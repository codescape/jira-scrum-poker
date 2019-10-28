package de.codescape.jira.plugins.scrumpoker.action;

import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.comments.Comment;
import com.atlassian.jira.issue.comments.CommentManager;
import com.atlassian.jira.junit.rules.AvailableInContainer;
import com.atlassian.jira.junit.rules.MockitoContainer;
import com.atlassian.jira.junit.rules.MockitoMocksInContainer;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.web.HttpServletVariables;
import com.atlassian.upm.api.license.PluginLicenseManager;
import com.atlassian.upm.api.license.entity.LicenseError;
import com.atlassian.upm.api.license.entity.PluginLicense;
import com.atlassian.upm.api.util.Option;
import de.codescape.jira.plugins.scrumpoker.condition.ScrumPokerForIssueCondition;
import de.codescape.jira.plugins.scrumpoker.service.ScrumPokerErrorService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

import static com.atlassian.jira.permission.ProjectPermissions.BROWSE_PROJECTS;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ShowScrumPokerActionTest {

    private static final String ISSUE_KEY = "ISSUE-1";

    @Rule
    public MockitoContainer mockitoContainer = MockitoMocksInContainer.rule(this);

    @Mock
    private IssueManager issueManager;

    @Mock
    @AvailableInContainer
    private HttpServletVariables httpServletVariables;

    @Mock
    private PermissionManager permissionManager;

    @Mock
    private JiraAuthenticationContext jiraAuthenticationContext;

    @Mock
    private PluginLicenseManager pluginLicenseManager;

    @Mock
    private CommentManager commentManager;

    @Mock
    private ScrumPokerForIssueCondition scrumPokerForIssueCondition;

    @Mock
    private ScrumPokerErrorService scrumPokerErrorService;

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
    }

    @Test
    public void shouldDisplayStartPageWhenUserHasPermissionForIssueAndIssueIsEstimable() {
        whenLicenseIsValid();
        whenRequestedIssueExists();
        whenUserIsAllowedToSeeIssue();
        whenIssueIsEstimable();

        assertThat(showScrumPokerAction.doExecute(), is(equalTo("success")));
    }

    @Test
    public void shouldDisplayErrorPageWhenUserHasNoPermissionForIssue() {
        whenLicenseIsValid();
        whenRequestedIssueExists();
        whenUserIsNotAllowedToSeeIssue();

        assertThat(showScrumPokerAction.doExecute(), is(equalTo("error")));
    }

    @Test
    public void shouldDisplayErrorPageWhenIssueIsNotEstimable() {
        whenLicenseIsValid();
        whenRequestedIssueExists();
        whenUserIsAllowedToSeeIssue();
        whenIssueIsNotEstimable();

        assertThat(showScrumPokerAction.doExecute(), is(equalTo("error")));
    }

    @Test
    public void shouldDisplayErrorPageWhenLicenseIsMissing() {
        whenLicenseIsMissing();

        assertThat(showScrumPokerAction.doExecute(), is(equalTo("error")));
        assertThat(showScrumPokerAction.getErrorMessages(), hasItem("Scrum Poker for Jira is missing a valid license!"));
    }

    @Test
    public void shouldDisplayErrorPageWhenLicenseIsInvalid() {
        whenLicenseIsInvalid();

        assertThat(showScrumPokerAction.doExecute(), is(equalTo("error")));
        assertThat(showScrumPokerAction.getErrorMessages(), hasItem("Scrum Poker for Jira has license errors: EXPIRED"));
    }

    @Test
    public void shouldReturnCommentsForIssue() {
        whenRequestedIssueExists();
        whenUserIsAllowedToSeeIssue();

        ArrayList<Comment> comments = new ArrayList<>();
        when(commentManager.getCommentsForUser(issue, user)).thenReturn(comments);

        assertThat(showScrumPokerAction.getComments(), is(equalTo(comments)));
    }

    private void whenLicenseIsInvalid() {
        PluginLicense pluginLicense = mock(PluginLicense.class);
        when(pluginLicense.getError()).thenReturn(Option.option(LicenseError.EXPIRED));
        when(pluginLicenseManager.getLicense()).thenReturn(Option.option(pluginLicense));
    }

    private void whenLicenseIsMissing() {
        when(pluginLicenseManager.getLicense()).thenReturn(Option.none());
    }

    private void whenLicenseIsValid() {
        PluginLicense pluginLicense = mock(PluginLicense.class);
        when(pluginLicense.getError()).thenReturn(Option.none());
        when(pluginLicenseManager.getLicense()).thenReturn(Option.option(pluginLicense));
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
