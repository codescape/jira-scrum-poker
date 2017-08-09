package net.congstar.jira.plugins.scrumpoker.action;

import com.atlassian.jira.issue.IssueFieldConstants;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.RendererManager;
import com.atlassian.jira.issue.fields.layout.field.FieldLayout;
import com.atlassian.jira.issue.fields.layout.field.FieldLayoutItem;
import com.atlassian.jira.issue.fields.layout.field.FieldLayoutManager;
import com.atlassian.jira.user.util.UserManager;
import com.atlassian.velocity.htmlsafe.HtmlSafe;
import net.congstar.jira.plugins.scrumpoker.data.ScrumPokerStorage;
import net.congstar.jira.plugins.scrumpoker.model.ScrumPokerCard;
import net.congstar.jira.plugins.scrumpoker.model.ScrumPokerCards;
import net.congstar.jira.plugins.scrumpoker.model.ScrumPokerSession;

import java.util.Map;

public class StartScrumPokerAction extends ScrumPokerAction {

    private static final long serialVersionUID = 1L;

    /* components */

    private final IssueManager issueManager;

    private final ScrumPokerStorage scrumPokerStorage;

    private final UserManager userManager;

    private final RendererManager rendererManager;

    private final FieldLayoutManager fieldLayoutManager;

    /* view model */

    private String issueKey;

    private String issueProjectName;

    private String issueProjectKey;

    private Map<String, ScrumPokerCard> cardDeck = ScrumPokerCards.asMap();

    private ScrumPokerSession pokerSession;

    public StartScrumPokerAction(IssueManager issueManager, UserManager userManager, RendererManager rendererManager,
                                 ScrumPokerStorage scrumPokerStorage, FieldLayoutManager fieldLayoutManager) {
        this.issueManager = issueManager;
        this.scrumPokerStorage = scrumPokerStorage;
        this.userManager = userManager;
        this.fieldLayoutManager = fieldLayoutManager;
        this.rendererManager = rendererManager;
    }

    @Override
    protected String doExecute() throws Exception {
        String action = getHttpRequest().getParameter("action");

        if (getLoggedInUser() == null) {
            return "error";
        }

        issueKey = getHttpRequest().getParameter(PARAM_ISSUE_KEY);
        MutableIssue issue = issueManager.getIssueObject(issueKey);
        if (issue == null) {
            addErrorMessage("Issue Key" + issueKey + " not found.");
            return "error";
        }

        pokerSession = scrumPokerStorage.sessionForIssue(issueKey);
        pokerSession.setIssueSummary(issue.getSummary());

        issueProjectName = issue.getProjectObject().getName();
        issueProjectKey = issue.getProjectObject().getKey();

        if (action != null && action.equals("update")) {
            return "update";
        } else
            return "start";
    }

    @HtmlSafe
    public String getIssueDescription() {
        MutableIssue issue = issueManager.getIssueObject(issueKey);
        FieldLayout fieldLayout = fieldLayoutManager.getFieldLayout(issue);
        FieldLayoutItem fieldLayoutItem = fieldLayout.getFieldLayoutItem(IssueFieldConstants.DESCRIPTION);
        String rendererType = (fieldLayoutItem != null) ? fieldLayoutItem.getRendererType() : null;
        return rendererManager.getRenderedContent(rendererType, issue.getDescription(), issue.getIssueRenderContext());
    }

    public String getIssueProjectKey() {
        return issueProjectKey;
    }

    public String getIssueKey() {
        return issueKey;
    }

    public String getIssueProjectName() {
        return issueProjectName;
    }

    public Map<String, ScrumPokerCard> getCardDeck() {
        return cardDeck;
    }

    public String getChosenCard() {
        return pokerSession.getCards().get(getLoggedInUser().getKey());
    }

    public ScrumPokerCard[] getCards() {
        return ScrumPokerCards.pokerDeck;
    }

    public ScrumPokerSession getPokerSession() {
        return pokerSession;
    }

    public String getUsername(String key) {
        return userManager.getUserByKey(key).getDisplayName();
    }

}
