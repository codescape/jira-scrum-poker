package net.congstar.jira.plugins.scrumpoker.action;

import com.atlassian.jira.issue.IssueFieldConstants;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.RendererManager;
import com.atlassian.jira.issue.fields.layout.field.FieldLayout;
import com.atlassian.jira.issue.fields.layout.field.FieldLayoutItem;
import com.atlassian.jira.issue.fields.layout.field.FieldLayoutManager;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.user.util.UserManager;
import com.atlassian.jira.web.HttpServletVariables;
import com.atlassian.velocity.htmlsafe.HtmlSafe;
import net.congstar.jira.plugins.scrumpoker.data.ScrumPokerStorage;
import net.congstar.jira.plugins.scrumpoker.model.ScrumPokerCard;
import net.congstar.jira.plugins.scrumpoker.model.ScrumPokerCards;
import net.congstar.jira.plugins.scrumpoker.model.ScrumPokerSession;

import static com.atlassian.jira.permission.ProjectPermissions.BROWSE_PROJECTS;

/**
 * Start a new Scrum poker session or refresh a currently displayed Scrum poker session.
 */
public class StartScrumPokerAction extends ScrumPokerAction {

    private static final long serialVersionUID = 1L;
    private static final String PARAM_ACTION = "action";

    private final IssueManager issueManager;
    private final ScrumPokerStorage scrumPokerStorage;
    private final UserManager userManager;
    private final RendererManager rendererManager;
    private final FieldLayoutManager fieldLayoutManager;
    private final PermissionManager permissionManager;
    private final JiraAuthenticationContext jiraAuthenticationContext;
    private final HttpServletVariables httpServletVariables;

    private String issueKey;
    private ScrumPokerSession pokerSession;

    public StartScrumPokerAction(IssueManager issueManager, RendererManager rendererManager, UserManager userManager,
                                 ScrumPokerStorage scrumPokerStorage, FieldLayoutManager fieldLayoutManager,
                                 PermissionManager permissionManager, JiraAuthenticationContext jiraAuthenticationContext, HttpServletVariables httpServletVariables) {
        this.issueManager = issueManager;
        this.rendererManager = rendererManager;
        this.userManager = userManager;
        this.scrumPokerStorage = scrumPokerStorage;
        this.fieldLayoutManager = fieldLayoutManager;
        this.permissionManager = permissionManager;
        this.jiraAuthenticationContext = jiraAuthenticationContext;
        this.httpServletVariables = httpServletVariables;
    }

    @Override
    protected String doExecute() throws Exception {
        String action = httpServletVariables.getHttpRequest().getParameter(PARAM_ACTION);
        issueKey = httpServletVariables.getHttpRequest().getParameter(PARAM_ISSUE_KEY);
        MutableIssue issue = issueManager.getIssueObject(issueKey);
        if (issue == null || !permissionManager.hasPermission(BROWSE_PROJECTS, issue, jiraAuthenticationContext.getLoggedInUser())) {
            addErrorMessage("Issue Key" + issueKey + " not found.");
            return "error";
        }
        pokerSession = scrumPokerStorage.sessionForIssue(issueKey, jiraAuthenticationContext.getLoggedInUser().getKey());
        return "update".equals(action) ? "update" : "start";
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

    public String getChosenCard() {
        return pokerSession.getCards().get(jiraAuthenticationContext.getLoggedInUser().getKey());
    }

    public ScrumPokerCard[] getCards() {
        return ScrumPokerCards.pokerDeck;
    }

    public ScrumPokerSession getPokerSession() {
        return pokerSession;
    }

    public String getUsername(String key) {
        ApplicationUser user = userManager.getUserByKey(key);
        return user != null ? user.getDisplayName() : key;
    }

}
