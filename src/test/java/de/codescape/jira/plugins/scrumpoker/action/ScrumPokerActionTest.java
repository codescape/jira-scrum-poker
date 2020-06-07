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
import de.codescape.jira.plugins.scrumpoker.model.DisplayCommentsForIssue;
import de.codescape.jira.plugins.scrumpoker.model.GlobalSettings;
import de.codescape.jira.plugins.scrumpoker.service.ErrorLogService;
import de.codescape.jira.plugins.scrumpoker.service.EstimateFieldService;
import de.codescape.jira.plugins.scrumpoker.service.GlobalSettingsService;
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

public class ScrumPokerActionTest {

    private static final String ISSUE_KEY = "ISSUE-1";

    @Rule
    public MockitoContainer mockitoContainer = MockitoMocksInContainer.rule(this);

    @Mock
    private IssueManager issueManager;

    @Mock
    private EstimateFieldService estimateFieldService;

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
    private GlobalSettingsService globalSettingsService;

    @Mock
    private ErrorLogService errorLogService;

    @InjectMocks
    private ScrumPokerAction scrumPokerAction;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private MutableIssue issue;

    @Mock
    private ApplicationUser user;

    @Mock
    private GlobalSettings globalSettings;

    @Before
    public void before() {
        when(httpServletVariables.getHttpRequest()).thenReturn(httpServletRequest);
        when(jiraAuthenticationContext.getLoggedInUser()).thenReturn(user);
    }

    /* tests for doExecute() */

    @Test
    public void shouldDisplayStartPageWhenUserHasPermissionForIssueAndIssueIsEstimable() {
        whenLicenseIsValid();
        whenRequestedIssueExists();
        whenUserIsAllowedToSeeIssue();
        whenIssueIsEstimable();

        assertThat(scrumPokerAction.doExecute(), is(equalTo("success")));
    }

    @Test
    public void shouldDisplayErrorPageWhenUserHasNoPermissionForIssue() {
        whenLicenseIsValid();
        whenRequestedIssueExists();
        whenUserIsNotAllowedToSeeIssue();

        assertThat(scrumPokerAction.doExecute(), is(equalTo("error")));
    }

    @Test
    public void shouldDisplayErrorPageWhenIssueIsNotEstimable() {
        whenLicenseIsValid();
        whenRequestedIssueExists();
        whenUserIsAllowedToSeeIssue();
        whenIssueIsNotEstimable();

        assertThat(scrumPokerAction.doExecute(), is(equalTo("error")));
    }

    @Test
    public void shouldDisplayErrorPageWhenLicenseIsMissing() {
        whenLicenseIsMissing();

        assertThat(scrumPokerAction.doExecute(), is(equalTo("error")));
        assertThat(scrumPokerAction.getErrorMessages(), hasItem("Scrum Poker for Jira is missing a valid license!"));
    }

    @Test
    public void shouldDisplayErrorPageWhenLicenseIsInvalid() {
        whenLicenseIsInvalid();

        assertThat(scrumPokerAction.doExecute(), is(equalTo("error")));
        assertThat(scrumPokerAction.getErrorMessages(), hasItem("Scrum Poker for Jira has license errors: EXPIRED"));
    }

    /* tests for getComments() */

    @Test
    public void shouldReturnCommentsForIssue() {
        whenRequestedIssueExists();
        whenUserIsAllowedToSeeIssue();
        whenDisplayCommentsForIssueSetTo(DisplayCommentsForIssue.ALL);

        ArrayList<Comment> comments = new ArrayList<>();
        when(commentManager.getCommentsForUser(issue, user)).thenReturn(comments);

        assertThat(scrumPokerAction.getComments(), is(equalTo(comments)));
    }

    /* tests for isDisplayCommentsForIssue() */

    @Test
    public void shouldNotDisplayCommentsIfDisplayCommentsForIssueIsSetToNone() {
        whenDisplayCommentsForIssueSetTo(DisplayCommentsForIssue.NONE);

        assertThat(scrumPokerAction.isDisplayCommentsForIssue(), is(false));
    }

    @Test
    public void shouldDisplayCommentsIfDisplayCommentsForIssueIsSetToAll() {
        whenDisplayCommentsForIssueSetTo(DisplayCommentsForIssue.ALL);

        assertThat(scrumPokerAction.isDisplayCommentsForIssue(), is(true));
    }

    @Test
    public void shouldDisplayCommentsIfDisplayCommentsForIssueIsSetToLatest() {
        whenDisplayCommentsForIssueSetTo(DisplayCommentsForIssue.LATEST);

        assertThat(scrumPokerAction.isDisplayCommentsForIssue(), is(true));
    }

    /* tests for getScrumPokerSessionUrl() */

    @Test
    public void shouldReturnExpectedScrumPokerSessionUrlForCurrentIssue() {
        // use the "happy path test" to allow further method calls
        shouldDisplayStartPageWhenUserHasPermissionForIssueAndIssueIsEstimable();

        // mock url construction of com.atlassian.jira.util.http.JiraUrl class
        when(httpServletRequest.getScheme()).thenReturn("https");
        when(httpServletRequest.getServerName()).thenReturn("apps.codescape.de");
        when(httpServletRequest.getContextPath()).thenReturn("/jira");
        when(httpServletRequest.getServerPort()).thenReturn(443);

        assertThat(scrumPokerAction.getScrumPokerSessionUrl(), is(equalTo("https://apps.codescape.de/jira/secure/ScrumPoker.jspa?issueKey=" + ISSUE_KEY)));
    }

    /* supporting methods */

    private void whenDisplayCommentsForIssueSetTo(DisplayCommentsForIssue displayCommentsForIssue) {
        when(globalSettingsService.load()).thenReturn(globalSettings);
        when(globalSettings.getDisplayCommentsForIssue()).thenReturn(displayCommentsForIssue);
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
        when(estimateFieldService.isEstimable(issue)).thenReturn(false);
    }

    private void whenIssueIsEstimable() {
        when(estimateFieldService.isEstimable(issue)).thenReturn(true);
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
