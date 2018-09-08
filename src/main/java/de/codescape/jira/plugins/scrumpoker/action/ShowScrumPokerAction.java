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
import com.atlassian.jira.web.HttpServletVariables;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import com.atlassian.velocity.htmlsafe.HtmlSafe;
import de.codescape.jira.plugins.scrumpoker.condition.ScrumPokerForIssueCondition;

import static com.atlassian.jira.permission.ProjectPermissions.BROWSE_PROJECTS;

/**
 * Show the Scrum Poker session page for a given issue.
 * <p>
 * This page verifies that the current user is allowed to see the issue and displays an error page in case the user is
 * not allowed to see the issue in question.
 */
public class ShowScrumPokerAction extends JiraWebActionSupport {

    private static final long serialVersionUID = 1L;
    private static final String PARAM_ISSUE_KEY = "issueKey";

    private final IssueManager issueManager;
    private final RendererManager rendererManager;
    private final FieldLayoutManager fieldLayoutManager;
    private final PermissionManager permissionManager;
    private final JiraAuthenticationContext jiraAuthenticationContext;
    private final HttpServletVariables httpServletVariables;
    private final ScrumPokerForIssueCondition scrumPokerForIssueCondition;

    private String issueKey;

    public ShowScrumPokerAction(FieldLayoutManager fieldLayoutManager, RendererManager rendererManager,
                                IssueManager issueManager, JiraAuthenticationContext jiraAuthenticationContext,
                                HttpServletVariables httpServletVariables, PermissionManager permissionManager,
                                ScrumPokerForIssueCondition scrumPokerForIssueCondition) {
        this.issueManager = issueManager;
        this.rendererManager = rendererManager;
        this.fieldLayoutManager = fieldLayoutManager;
        this.permissionManager = permissionManager;
        this.jiraAuthenticationContext = jiraAuthenticationContext;
        this.httpServletVariables = httpServletVariables;
        this.scrumPokerForIssueCondition = scrumPokerForIssueCondition;
    }

    @Override
    protected String doExecute() {
        issueKey = httpServletVariables.getHttpRequest().getParameter(PARAM_ISSUE_KEY);
        MutableIssue issue = issueManager.getIssueObject(issueKey);
        if (issue == null || currentUserIsNotAllowedToSeeIssue(issue) || issueIsNotEstimable(issue)) {
            addErrorMessage("Issue Key" + issueKey + " not found.");
            return "error";
        }
        return "success";
    }

    public MutableIssue getIssue() {
        return issueManager.getIssueObject(issueKey);
    }

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

}
