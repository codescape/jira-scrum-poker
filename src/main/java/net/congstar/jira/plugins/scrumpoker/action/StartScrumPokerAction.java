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

    private static final String PARAM_ACTION = "action";

    /* components */

    private final IssueManager issueManager;

    private final ScrumPokerStorage scrumPokerStorage;

    private final UserManager userManager;

    private final RendererManager rendererManager;

    private final FieldLayoutManager fieldLayoutManager;

    /* view model */

    private String issueKey;

    private final Map<String, ScrumPokerCard> cardDeck = ScrumPokerCards.asMap();

    private ScrumPokerSession pokerSession;

    public StartScrumPokerAction(IssueManager issueManager, UserManager userManager, RendererManager rendererManager,
                                 ScrumPokerStorage scrumPokerStorage, FieldLayoutManager fieldLayoutManager) {
        this.issueManager = issueManager;
        this.rendererManager = rendererManager;
        this.scrumPokerStorage = scrumPokerStorage;
        this.userManager = userManager;
        this.fieldLayoutManager = fieldLayoutManager;
    }

    @Override
    protected String doExecute() throws Exception {
        String action = getHttpRequest().getParameter(PARAM_ACTION);
        issueKey = getHttpRequest().getParameter(PARAM_ISSUE_KEY);

        MutableIssue issue = issueManager.getIssueObject(issueKey);
        if (issue == null) {
            addErrorMessage("Issue Key" + issueKey + " not found.");
            return "error";
        }

        pokerSession = scrumPokerStorage.sessionForIssue(issueKey, currentUserKey());

        if (action != null && action.equals("update")) {
            return "update";
        } else {
            return "start";
        }
    }

    public MutableIssue getIssue() {
        return issueManager.getIssueObject(issueKey);
    }

    @HtmlSafe
    public String getIssueDescription() {
        MutableIssue issue = issueManager.getIssueObject(issueKey);
        FieldLayout fieldLayout = fieldLayoutManager.getFieldLayout(issue);
        FieldLayoutItem fieldLayoutItem = fieldLayout.getFieldLayoutItem(IssueFieldConstants.DESCRIPTION);
        String rendererType = (fieldLayoutItem != null) ? fieldLayoutItem.getRendererType() : null;
        return rendererManager.getRenderedContent(rendererType, issue.getDescription(), issue.getIssueRenderContext());
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
