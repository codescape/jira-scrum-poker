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
import com.atlassian.velocity.htmlsafe.HtmlSafe;

import static com.atlassian.jira.permission.ProjectPermissions.BROWSE_PROJECTS;

/**
 * Show the Scrum Poker session page for a given issue.
 * <p>
 * This page verifies that the current user is allowed to see the issue and displays an error page in case the user is
 * not allowed to see the issue in question.
 */
public class ShowScrumPokerAction extends ScrumPokerAction {

    private static final long serialVersionUID = 1L;

    private final IssueManager issueManager;
    private final RendererManager rendererManager;
    private final FieldLayoutManager fieldLayoutManager;
    private final PermissionManager permissionManager;
    private final JiraAuthenticationContext jiraAuthenticationContext;
    private final HttpServletVariables httpServletVariables;

    private String issueKey;

    public ShowScrumPokerAction(IssueManager issueManager, RendererManager rendererManager, FieldLayoutManager fieldLayoutManager,
                                PermissionManager permissionManager, JiraAuthenticationContext jiraAuthenticationContext,
                                HttpServletVariables httpServletVariables) {
        this.issueManager = issueManager;
        this.rendererManager = rendererManager;
        this.fieldLayoutManager = fieldLayoutManager;
        this.permissionManager = permissionManager;
        this.jiraAuthenticationContext = jiraAuthenticationContext;
        this.httpServletVariables = httpServletVariables;
    }

    @Override
    protected String doExecute() {
        issueKey = httpServletVariables.getHttpRequest().getParameter(PARAM_ISSUE_KEY);
        MutableIssue issue = issueManager.getIssueObject(issueKey);
        if (issue == null || !permissionManager.hasPermission(BROWSE_PROJECTS, issue, jiraAuthenticationContext.getLoggedInUser())) {
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

}
