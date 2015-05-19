package net.congstar.jira.plugins.scrumpoker.action;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.net.URL;

import net.congstar.jira.plugins.scrumpoker.data.PlanningPokerStorage;
import net.congstar.jira.plugins.scrumpoker.data.StoryPointFieldSupport;
import net.congstar.jira.plugins.scrumpoker.model.PokerCard;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.IssueFieldConstants;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.RendererManager;
import com.atlassian.jira.issue.fields.layout.field.FieldLayout;
import com.atlassian.jira.issue.fields.layout.field.FieldLayoutItem;
import com.atlassian.jira.issue.fields.layout.field.FieldLayoutManager;
import com.atlassian.jira.user.util.UserManager;
import com.atlassian.velocity.htmlsafe.HtmlSafe;

public final class StartPlanningPoker extends ScrumPokerAction {

    private static final long serialVersionUID = 1L;

    private final IssueManager issueManager;

    private final PlanningPokerStorage planningPokerStorage;

    private final StoryPointFieldSupport storyPointFieldSupport;

    private String issueSummary;

    private Double issueStoryPoints;

    private String issueKey;

    private String issueProjectName;

    private String issueProjectKey;

    private String issueReturnUrl;

    private Map<String, String> cardsForIssue;

    private Map<String, PokerCard> cardDeck = new HashMap<String, PokerCard>();
    
    private String chosenCard;

    private FieldLayoutManager fieldLayoutManager;

    private RendererManager rendererManager;

    private UserManager userManager;
    
    @HtmlSafe
    public String getIssueDescription() {
        MutableIssue issue = issueManager.getIssueObject(issueKey);
        FieldLayout fieldLayout = fieldLayoutManager.getFieldLayout(issue);
        FieldLayoutItem fieldLayoutItem = fieldLayout.getFieldLayoutItem(IssueFieldConstants.DESCRIPTION);
        String rendererType = (fieldLayoutItem != null) ? fieldLayoutItem.getRendererType() : null;
        return rendererManager.getRenderedContent(rendererType, issue.getDescription(), issue.getIssueRenderContext());
    }

    public Double getIssueStoryPoints() {
        return issueStoryPoints;
    }

    public String getIssueProjectKey() {
        return issueProjectKey;
    }

    public String getIssueReturnUrl() {
        return issueReturnUrl;
    }

    public String getIssueKey() {
        return issueKey;
    }

    public String getIssueProjectName() {
        return issueProjectName;
    }

    public String getIssueSummary() {
        return issueSummary;
    }

    public boolean isDeckVisible() {
        return planningPokerStorage.isVisible(issueKey);
    }

    public Map<String, PokerCard> getCardDeck() {
        return cardDeck;
    }

    public String getChosenCard() {
        return chosenCard;
    }

    public PokerCard[] getCards() {
        return PokerUtil.pokerDeck;
    }

    public StartPlanningPoker(IssueManager issueManager, PlanningPokerStorage planningPokerStorage, StoryPointFieldSupport storyPointFieldSupport, UserManager userManager) {
        this.issueManager = issueManager;
        this.planningPokerStorage = planningPokerStorage;
        this.storyPointFieldSupport = storyPointFieldSupport;
        this.userManager = userManager;

        fieldLayoutManager = ComponentAccessor.getComponent(FieldLayoutManager.class);
        rendererManager = ComponentAccessor.getComponent(RendererManager.class);

        for (PokerCard card : PokerUtil.pokerDeck) {
            cardDeck.put(card.getName(), card);
        }
    }

    @Override
    protected String doExecute() throws Exception {
        String action = getHttpRequest().getParameter("action");

        issueKey = getHttpRequest().getParameter(PARAM_ISSUE_KEY);

        if (getLoggedInApplicationUser() == null) {
            return "error";
        }

        MutableIssue issue = issueManager.getIssueObject(issueKey);
        if (issue == null) {
            addErrorMessage("Issue Key" + issueKey + " not found.");
            return "error";
        }

        if (action!=null && !action.equals("update")) {
            // weird hack to check whether we have been called from "outside"
            Boolean outsideCall = true;
            URL referrerURL = new URL(getHttpRequest().getHeader(PARAM_REFERRER_HEADER));
            String selfAction = getActionName().toLowerCase();
            String referrerPath = referrerURL.getPath().toLowerCase();
            String regex = ".*/" + selfAction + "\\.?\\w*";
            if (referrerPath.matches(regex)) {
                outsideCall = false;
            }

            // remember the page we have to return to after finishing the poker round
            String sessionUrl = (String)getHttpSession().getAttribute(PARAM_RETURN_URL);
            issueReturnUrl = getReturnUrl();
            if (sessionUrl == null || outsideCall) {
                if (issueReturnUrl == null) {
                    issueReturnUrl = "/browse/" + issueKey;
                }
                getHttpSession().setAttribute(PARAM_RETURN_URL, issueReturnUrl);
            } else {
                issueReturnUrl = sessionUrl;
            }
        }

        cardsForIssue = planningPokerStorage.chosenCardsForIssue(issueKey);
        chosenCard = cardsForIssue.get(getLoggedInApplicationUser().getKey());

        issueSummary = issue.getSummary();
        issueProjectName = issue.getProjectObject().getName();
        issueProjectKey = issue.getProjectObject().getKey();
        issueStoryPoints = storyPointFieldSupport.getValue(issueKey);

        if (action!=null && action.equals("update")) {
            return "update";
        } else
            return "start";
    }

    public Collection<String> getBoundedVotes () {
            return PokerUtil.getBoundedVotes(cardsForIssue);
    }
    

    public String getMinVoted() {
        return PokerUtil.getMinVoted(getCardsForIssue());
    }

    public String getMaxVoted() {
        return PokerUtil.getMaxVoted(getCardsForIssue());
    }

    public Map<String, String> getCardsForIssue() {
        return cardsForIssue;
    }

    public String getUsername(String key) {
        return userManager.getUserByKey(key).getDisplayName();
    }

}
