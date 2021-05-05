package de.codescape.jira.plugins.scrumpoker.action;

import com.atlassian.jira.datetime.DateTimeFormatter;
import com.atlassian.jira.datetime.DateTimeStyle;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.RendererManager;
import com.atlassian.jira.issue.comments.Comment;
import com.atlassian.jira.issue.comments.CommentManager;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.fields.renderer.JiraRendererPlugin;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.jira.util.http.JiraUrl;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.message.I18nResolver;
import com.atlassian.velocity.htmlsafe.HtmlSafe;
import de.codescape.jira.plugins.scrumpoker.service.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.atlassian.jira.permission.ProjectPermissions.BROWSE_PROJECTS;

/**
 * Show the Scrum Poker session page for a given issue.
 * <p>
 * This page verifies that the current user is allowed to see the issue and displays an error page in case the user is
 * not allowed to see the issue in question.
 */
public class ScrumPokerAction extends AbstractScrumPokerAction {

    private static final long serialVersionUID = 1L;

    /**
     * Names of all parameters used on the Scrum Poker session page.
     */
    static final class Parameters {

        static final String ISSUE_KEY = "issueKey";

    }

    private final IssueManager issueManager;
    private final RendererManager rendererManager;
    private final PermissionManager permissionManager;
    private final CommentManager commentManager;
    private final JiraAuthenticationContext jiraAuthenticationContext;
    private final DateTimeFormatter dateTimeFormatter;
    private final GlobalSettingsService globalSettingsService;
    private final EstimateFieldService estimateFieldService;
    private final AdditionalFieldService additionalFieldService;
    private final ScrumPokerLicenseService scrumPokerLicenseService;
    private final I18nResolver i18nResolver;

    private String issueKey;

    @Autowired
    public ScrumPokerAction(@ComponentImport RendererManager rendererManager,
                            @ComponentImport IssueManager issueManager,
                            @ComponentImport JiraAuthenticationContext jiraAuthenticationContext,
                            @ComponentImport PermissionManager permissionManager,
                            @ComponentImport CommentManager commentManager,
                            @ComponentImport DateTimeFormatter dateTimeFormatter,
                            @ComponentImport I18nResolver i18nResolver,
                            GlobalSettingsService globalSettingsService,
                            EstimateFieldService estimateFieldService,
                            ErrorLogService errorLogService,
                            AdditionalFieldService additionalFieldService,
                            ScrumPokerLicenseService scrumPokerLicenseService) {
        super(errorLogService);
        this.rendererManager = rendererManager;
        this.issueManager = issueManager;
        this.jiraAuthenticationContext = jiraAuthenticationContext;
        this.permissionManager = permissionManager;
        this.commentManager = commentManager;
        this.dateTimeFormatter = dateTimeFormatter;
        this.globalSettingsService = globalSettingsService;
        this.estimateFieldService = estimateFieldService;
        this.additionalFieldService = additionalFieldService;
        this.scrumPokerLicenseService = scrumPokerLicenseService;
        this.i18nResolver = i18nResolver;
    }

    /**
     * Display the page if the current user is allowed to see the issue and a Scrum Poker session can be started.
     */
    @Override
    protected String doExecute() {
        // license check
        if (!scrumPokerLicenseService.hasValidLicense()) {
            errorMessage(i18nResolver.getText(scrumPokerLicenseService.getLicenseError()));
            return ERROR;
        }

        // issue check
        issueKey = getParameter(Parameters.ISSUE_KEY);
        MutableIssue issue = issueManager.getIssueObject(issueKey);
        if (issue == null || currentUserIsNotAllowedToSeeIssue(issue) || issueIsNotEstimable(issue)) {
            errorMessage("Issue Key " + issueKey + " not found.");
            return ERROR;
        }

        return SUCCESS;
    }

    /**
     * Return whether to display the comments for an issue.
     */
    public boolean isDisplayCommentsForIssue() {
        return globalSettingsService.load().getDisplayCommentsForIssue().shouldDisplay();
    }

    /**
     * All comments for that issue the currently logged in user may see.
     */
    public List<Comment> getComments() {
        List<Comment> comments = commentManager.getCommentsForUser(getIssue(), jiraAuthenticationContext.getLoggedInUser());
        switch (globalSettingsService.load().getDisplayCommentsForIssue()) {
            case ALL:
                return comments;
            case LATEST:
                return comments.subList(Math.max(comments.size() - 10, 0), comments.size());
            default:
                return null;
        }
    }

    /**
     * Returns the list of all configured fields to be additionally displayed on the session view.
     *
     * @return list of fields
     */
    public List<CustomField> getAdditionalFields() {
        return additionalFieldService.configuredCustomFields();
    }

    /**
     * Renders the field value for the given custom field.
     *
     * @param customField custom field to render for the current issue
     * @return html code including the field value
     */
    @HtmlSafe
    public String renderFieldValue(CustomField customField) {
        return additionalFieldService.renderFieldValue(customField, this, getIssue());
    }

    /**
     * Returns whether the custom field has a value for the current issue.
     *
     * @param customField custom field of the current issue
     * @return <code>true</code> if custom field has a value, otherwise <code>false</code>
     */
    public boolean hasFieldValue(CustomField customField) {
        return getIssue() != null && getIssue().getCustomFieldValue(customField) != null;
    }

    /**
     * The renderer to display text with wiki markup as in issue description and comments.
     */
    public JiraRendererPlugin getWikiRenderer() {
        return rendererManager.getRendererForType("atlassian-wiki-renderer");
    }

    /**
     * Return the date time formatter according to the settings of the current user.
     */
    public DateTimeFormatter getDateTimeFormatter() {
        return dateTimeFormatter.forLoggedInUser().withStyle(DateTimeStyle.COMPLETE);
    }

    /**
     * Current issue for that this Scrum Poker session is started.
     */
    public MutableIssue getIssue() {
        return issueManager.getIssueObject(issueKey);
    }

    /**
     * Url to this Scrum Poker session to be displayed and used for the client side QR code generation.
     */
    public String getScrumPokerSessionUrl() {
        return JiraUrl.constructBaseUrl(getHttpRequest()) + "/secure/ScrumPoker.jspa?issueKey=" + issueKey;
    }

    private boolean issueIsNotEstimable(MutableIssue issue) {
        return !estimateFieldService.isEstimable(issue);
    }

    private boolean currentUserIsNotAllowedToSeeIssue(MutableIssue issue) {
        return !permissionManager.hasPermission(BROWSE_PROJECTS, issue, jiraAuthenticationContext.getLoggedInUser());
    }

}
