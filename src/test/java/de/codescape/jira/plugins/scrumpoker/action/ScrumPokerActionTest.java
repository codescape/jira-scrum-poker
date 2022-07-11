package de.codescape.jira.plugins.scrumpoker.action;

import com.atlassian.jira.issue.IssueFieldConstants;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.RendererManager;
import com.atlassian.jira.issue.comments.Comment;
import com.atlassian.jira.issue.comments.CommentManager;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.fields.layout.field.FieldLayout;
import com.atlassian.jira.issue.fields.layout.field.FieldLayoutItem;
import com.atlassian.jira.issue.fields.layout.field.FieldLayoutManager;
import com.atlassian.jira.issue.fields.renderer.IssueRenderContext;
import com.atlassian.jira.issue.fields.renderer.JiraRendererPlugin;
import com.atlassian.jira.junit.rules.AvailableInContainer;
import com.atlassian.jira.junit.rules.MockitoContainer;
import com.atlassian.jira.junit.rules.MockitoMocksInContainer;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.web.HttpServletVariables;
import com.atlassian.sal.api.message.I18nResolver;
import com.atlassian.upm.api.license.entity.LicenseError;
import de.codescape.jira.plugins.scrumpoker.model.DisplayCommentsForIssue;
import de.codescape.jira.plugins.scrumpoker.model.GlobalSettings;
import de.codescape.jira.plugins.scrumpoker.service.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

import static com.atlassian.jira.permission.ProjectPermissions.BROWSE_PROJECTS;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assume.assumeThat;
import static org.mockito.Mockito.*;

public class ScrumPokerActionTest {

    private static final String ISSUE_KEY = "ISSUE-1";

    @Rule
    public MockitoContainer mockitoContainer = MockitoMocksInContainer.rule(this);

    @Mock
    private IssueManager issueManager;

    @Mock
    private EstimateFieldService estimateFieldService;

    @Mock
    private ScrumPokerLicenseService scrumPokerLicenseService;

    @Mock
    private I18nResolver i18nResolver;

    @Mock
    private RendererManager rendererManager;

    @Mock
    private FieldLayoutManager fieldLayoutManager;

    @Mock
    @AvailableInContainer
    private HttpServletVariables httpServletVariables;

    @Mock
    private PermissionManager permissionManager;

    @Mock
    private JiraAuthenticationContext jiraAuthenticationContext;

    @Mock
    private CommentManager commentManager;

    @Mock
    private GlobalSettingsService globalSettingsService;

    @Mock
    private ErrorLogService errorLogService;

    @Mock
    private AdditionalFieldService additionalFieldService;

    @InjectMocks
    private ScrumPokerAction action;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private MutableIssue issue;

    @Mock
    private ApplicationUser user;

    @Mock
    private GlobalSettings globalSettings;

    @Mock
    private CustomField customField;

    @Before
    public void before() {
        when(httpServletVariables.getHttpRequest()).thenReturn(httpServletRequest);
        when(jiraAuthenticationContext.getLoggedInUser()).thenReturn(user);
        when(httpServletRequest.getParameterValues("returnUrl")).thenReturn(new String[]{"/browse/" + ISSUE_KEY});
    }

    /* tests for doExecute() */

    @Test
    public void shouldDisplayStartPageWhenUserHasPermissionForIssueAndIssueIsEstimable() {
        whenLicenseIsValid();
        whenRequestedIssueExists();
        whenUserIsAllowedToSeeIssue();
        whenIssueIsEstimable();

        assertThat(action.doExecute(), is(equalTo("success")));
    }

    @Test
    public void shouldDisplayErrorPageWhenUserHasNoPermissionForIssue() {
        whenLicenseIsValid();
        whenRequestedIssueExists();
        whenUserIsNotAllowedToSeeIssue();

        assertThat(action.doExecute(), is(equalTo("error")));
    }

    @Test
    public void shouldDisplayErrorPageWhenIssueIsNotEstimable() {
        whenLicenseIsValid();
        whenRequestedIssueExists();
        whenUserIsAllowedToSeeIssue();
        whenIssueIsNotEstimable();

        assertThat(action.doExecute(), is(equalTo("error")));
    }

    @Test
    public void shouldDisplayErrorPageWhenLicenseIsInvalid() {
        whenLicenseIsInvalid();
        when(i18nResolver.getText(anyString())).thenReturn("License is invalid.");

        assertThat(action.doExecute(), is(equalTo("error")));
        assertThat(action.getErrorMessages(), hasItem("License is invalid."));
    }

    /* tests for getComments() */

    @Test
    public void shouldReturnCommentsForIssue() {
        whenRequestedIssueExists();
        whenUserIsAllowedToSeeIssue();
        whenDisplayCommentsForIssueSetTo(DisplayCommentsForIssue.ALL);

        ArrayList<Comment> comments = new ArrayList<>();
        when(commentManager.getCommentsForUser(issue, user)).thenReturn(comments);

        assertThat(action.getComments(), is(equalTo(comments)));
    }

    /* tests for isDisplayCommentsForIssue() */

    @Test
    public void shouldNotDisplayCommentsIfDisplayCommentsForIssueIsSetToNone() {
        whenDisplayCommentsForIssueSetTo(DisplayCommentsForIssue.NONE);

        assertThat(action.isDisplayCommentsForIssue(), is(false));
    }

    @Test
    public void shouldDisplayCommentsIfDisplayCommentsForIssueIsSetToAll() {
        whenDisplayCommentsForIssueSetTo(DisplayCommentsForIssue.ALL);

        assertThat(action.isDisplayCommentsForIssue(), is(true));
    }

    @Test
    public void shouldDisplayCommentsIfDisplayCommentsForIssueIsSetToLatest() {
        whenDisplayCommentsForIssueSetTo(DisplayCommentsForIssue.LATEST);

        assertThat(action.isDisplayCommentsForIssue(), is(true));
    }

    /* tests for renderFieldValue() */

    @Test
    public void shouldDelegateToAdditionalFieldService() {
        whenLicenseIsValid();
        whenRequestedIssueExists();
        action.doExecute();

        action.renderFieldValue(customField);

        verify(additionalFieldService, times(1)).renderFieldValue(customField, action, issue);
        verifyNoMoreInteractions(additionalFieldService);
    }

    /* tests for hasFieldValue() */

    @Test
    public void hasFieldValueShouldReturnFalseIfIssueDoesNotExist() {
        assumeThat(action.getIssue(), is(nullValue()));
        assertThat(action.hasFieldValue(customField), is(false));
    }

    @Test
    public void hasFieldValueShouldReturnTrueIfFieldHasValue() {
        whenLicenseIsValid();
        whenRequestedIssueExists();
        action.doExecute();
        when(issue.getCustomFieldValue(eq(customField))).thenReturn("Hello World");

        assertThat(action.hasFieldValue(customField), is(true));
    }

    @Test
    public void hasFieldValueShouldReturnFalseIfFieldHasNoValue() {
        whenLicenseIsValid();
        whenRequestedIssueExists();
        action.doExecute();
        when(issue.getCustomFieldValue(eq(customField))).thenReturn(null);

        assertThat(action.hasFieldValue(customField), is(false));
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

        assertThat(action.getScrumPokerSessionUrl(),
            is(equalTo("https://apps.codescape.de/jira/secure/ScrumPoker.jspa?issueKey=" + ISSUE_KEY)));
    }

    /* tests for getWikiRenderer() */

    @Test
    public void getWikiRendererShouldDelegate() {
        JiraRendererPlugin jiraRendererPlugin = mock(JiraRendererPlugin.class);
        when(rendererManager.getRendererForType(eq("atlassian-wiki-renderer"))).thenReturn(jiraRendererPlugin);

        JiraRendererPlugin wikiRenderer = action.getWikiRenderer();

        assertThat(wikiRenderer, is(equalTo(jiraRendererPlugin)));
        verify(rendererManager, times(1)).getRendererForType("atlassian-wiki-renderer");
        verifyNoMoreInteractions(rendererManager);
    }

    /* test for getIssueDescription */

    @Test
    public void getIssueDescription() {
        whenLicenseIsValid();
        whenRequestedIssueExists();
        action.doExecute();

        FieldLayout fieldLayout = mock(FieldLayout.class);
        when(fieldLayoutManager.getFieldLayout(issue)).thenReturn(fieldLayout);

        FieldLayoutItem fieldLayoutItem = mock(FieldLayoutItem.class);
        when(fieldLayout.getFieldLayoutItem(IssueFieldConstants.DESCRIPTION)).thenReturn(fieldLayoutItem);

        when(fieldLayoutItem.getRendererType()).thenReturn("atlassian-wiki-renderer");

        IssueRenderContext issueRenderContext = mock(IssueRenderContext.class);

        when(issue.getIssueRenderContext()).thenReturn(issueRenderContext);
        when(issue.getDescription()).thenReturn("it works!");
        when(rendererManager.getRenderedContent(anyString(), anyString(), eq(issueRenderContext))).thenReturn("<p>it works!</p>");

        assertThat(action.getIssueDescription(), is(equalTo("<p>it works!</p>")));
    }

    /* supporting methods */

    private void whenDisplayCommentsForIssueSetTo(DisplayCommentsForIssue displayCommentsForIssue) {
        when(globalSettingsService.load()).thenReturn(globalSettings);
        when(globalSettings.getDisplayCommentsForIssue()).thenReturn(displayCommentsForIssue);
    }

    private void whenLicenseIsInvalid() {
        when(scrumPokerLicenseService.hasValidLicense()).thenReturn(false);
        when(scrumPokerLicenseService.getLicenseError()).thenReturn(LicenseError.EXPIRED.name());
    }

    private void whenLicenseIsValid() {
        when(scrumPokerLicenseService.hasValidLicense()).thenReturn(true);
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
        when(httpServletRequest.getParameterValues("issueKey")).thenReturn(new String[]{ISSUE_KEY});
        when(issueManager.getIssueObject(ISSUE_KEY)).thenReturn(issue);
    }

}
