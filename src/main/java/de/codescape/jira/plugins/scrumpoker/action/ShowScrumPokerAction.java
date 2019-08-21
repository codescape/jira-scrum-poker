package de.codescape.jira.plugins.scrumpoker.action;

import com.atlassian.jira.issue.IssueFieldConstants;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.RendererManager;
import com.atlassian.jira.issue.fields.layout.field.FieldLayout;
import com.atlassian.jira.issue.fields.layout.field.FieldLayoutItem;
import com.atlassian.jira.issue.fields.layout.field.FieldLayoutManager;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.jira.util.http.JiraUrl;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.upm.api.license.PluginLicenseManager;
import com.atlassian.upm.api.license.entity.PluginLicense;
import com.atlassian.velocity.htmlsafe.HtmlSafe;
import de.codescape.jira.plugins.scrumpoker.condition.ScrumPokerForIssueCondition;
import de.codescape.jira.plugins.scrumpoker.service.ScrumPokerErrorService;
import org.springframework.beans.factory.annotation.Autowired;

import static com.atlassian.jira.permission.ProjectPermissions.BROWSE_PROJECTS;

/**
 * Show the Scrum Poker session page for a given issue.
 * <p>
 * This page verifies that the current user is allowed to see the issue and displays an error page in case the user is
 * not allowed to see the issue in question.
 */
public class ShowScrumPokerAction extends AbstractScrumPokerAction {

    private static final long serialVersionUID = 1L;

    /**
     * Names of all parameters used on the Scrum Poker session page.
     */
    static final class Parameters {

        static final String ISSUE_KEY = "issueKey";

    }

    private final IssueManager issueManager;
    private final RendererManager rendererManager;
    private final FieldLayoutManager fieldLayoutManager;
    private final PermissionManager permissionManager;
    private final JiraAuthenticationContext jiraAuthenticationContext;
    private final ScrumPokerForIssueCondition scrumPokerForIssueCondition;
    private final PluginLicenseManager pluginLicenseManager;
    private final ScrumPokerErrorService scrumPokerErrorService;

    private String issueKey;

    @Autowired
    public ShowScrumPokerAction(@ComponentImport FieldLayoutManager fieldLayoutManager,
                                @ComponentImport RendererManager rendererManager,
                                @ComponentImport IssueManager issueManager,
                                @ComponentImport JiraAuthenticationContext jiraAuthenticationContext,
                                @ComponentImport PermissionManager permissionManager,
                                @ComponentImport PluginLicenseManager pluginLicenseManager,
                                ScrumPokerForIssueCondition scrumPokerForIssueCondition,
                                ScrumPokerErrorService scrumPokerErrorService) {
        this.issueManager = issueManager;
        this.rendererManager = rendererManager;
        this.fieldLayoutManager = fieldLayoutManager;
        this.permissionManager = permissionManager;
        this.jiraAuthenticationContext = jiraAuthenticationContext;
        this.pluginLicenseManager = pluginLicenseManager;
        this.scrumPokerForIssueCondition = scrumPokerForIssueCondition;
        this.scrumPokerErrorService = scrumPokerErrorService;
    }

    /**
     * Display the page if the current user is allowed to see the issue and a Scrum Poker session can be started.
     */
    @Override
    protected String doExecute() {
        // license check
        if (pluginLicenseManager.getLicense().isDefined()) {
            PluginLicense license = pluginLicenseManager.getLicense().get();
            if (license.getError().isDefined()) {
                errorMessage("Scrum Poker for Jira has license errors: " + license.getError().get().name());
                return ERROR;
            }
        } else {
            errorMessage("Scrum Poker for Jira is missing a valid license!");
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

    private void errorMessage(String errorMessage) {
        scrumPokerErrorService.logError(errorMessage, null);
        addErrorMessage(errorMessage);
    }

    /**
     * Current issue for that this Scrum Poker session is started.
     */
    public MutableIssue getIssue() {
        return issueManager.getIssueObject(issueKey);
    }

    /**
     * Description of the current issue rendered with respect to the markup being used.
     */
    @HtmlSafe
    public String getIssueDescription() {
        MutableIssue issue = issueManager.getIssueObject(issueKey);
        FieldLayout fieldLayout = fieldLayoutManager.getFieldLayout(issue);
        FieldLayoutItem fieldLayoutItem = fieldLayout.getFieldLayoutItem(IssueFieldConstants.DESCRIPTION);
        String rendererType = fieldLayoutItem != null ? fieldLayoutItem.getRendererType() : null;
        return rendererManager.getRenderedContent(rendererType, issue.getDescription(), issue.getIssueRenderContext());
    }

    private boolean issueIsNotEstimable(MutableIssue issue) {
        return !scrumPokerForIssueCondition.isEstimable(issue);
    }

    private boolean currentUserIsNotAllowedToSeeIssue(MutableIssue issue) {
        return !permissionManager.hasPermission(BROWSE_PROJECTS, issue, jiraAuthenticationContext.getLoggedInUser());
    }

    /**
     * Url to this Scrum Poker session to be displayed and used for the client side QR code generation.
     */
    public String getScrumPokerSessionUrl() {
        return JiraUrl.constructBaseUrl(getHttpRequest()) + "/secure/ScrumPoker.jspa?issueKey=" + issueKey;
    }

}
