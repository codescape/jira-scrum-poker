package net.congstar.jira.plugins.planningpoker.action;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.*;
import com.atlassian.jira.issue.fields.layout.field.FieldLayout;
import com.atlassian.jira.issue.fields.layout.field.FieldLayoutItem;
import com.atlassian.jira.issue.fields.layout.field.FieldLayoutManager;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.user.util.UserManager;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.atlassian.velocity.htmlsafe.HtmlSafe;
import net.congstar.jira.plugins.planningpoker.data.PlanningPokerStorage;
import net.congstar.jira.plugins.planningpoker.data.StoryPointFieldSupport;
import net.congstar.jira.plugins.planningpoker.model.PokerCard;
import webwork.action.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public final class StartPlanningPoker extends JiraWebActionSupport {

    private static final long serialVersionUID = 1L;

    private final IssueManager issueManager;

    private final JiraAuthenticationContext context;

    private final CustomFieldManager customFieldManager;

    private final PluginSettingsFactory settingsFactory;

    private final PlanningPokerStorage planningPokerStorage;

    private final StoryPointFieldSupport storyPointFieldSupport;

    private String issueSummary;

    private Double issueStoryPoints;

    private String issueKey;

    private String issueProjectName;

    private String issueProjectKey;

    @HtmlSafe
    public String getIssueDescription() {
        MutableIssue issue = issueManager.getIssueObject(issueKey);
        FieldLayout fieldLayout = fieldLayoutManager.getFieldLayout(issue);
        FieldLayoutItem fieldLayoutItem = fieldLayout
                .getFieldLayoutItem(IssueFieldConstants.DESCRIPTION);
        String rendererType = (fieldLayoutItem != null) ? fieldLayoutItem
                .getRendererType() : null;
        return rendererManager.getRenderedContent(rendererType,
                issue.getDescription(), issue.getIssueRenderContext());
    }

    private Map<String, String> cardsForIssue;

    public Double getIssueStoryPoints() {
        return issueStoryPoints;
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

    public String getIssueSummary() {
        return issueSummary;
    }

    public boolean isDeckVisible() {
        return planningPokerStorage.isVisible(issueKey);
    }

    private PokerCard[] cards = {new PokerCard("q", "q.jpg", "q_.jpg"),
            new PokerCard("0", "0.jpg", "0_.jpg"),
            new PokerCard("1", "1.jpg", "1_.jpg"),
            new PokerCard("2", "2.jpg", "2_.jpg"),
            new PokerCard("3", "3.jpg", "3_.jpg"),
            new PokerCard("5", "5.jpg", "5_.jpg"),
            new PokerCard("8", "8.jpg", "8_.jpg"),
            new PokerCard("13", "13.jpg", "13_.jpg"),
            new PokerCard("20", "20.jpg", "20_.jpg"),
            new PokerCard("40", "40.jpg", "40_.jpg"),
            new PokerCard("100", "100.jpg", "100_.jpg")};

    private Map<String, PokerCard> cardDeck = new HashMap<String, PokerCard>();

    public Map<String, PokerCard> getCardDeck() {
        return cardDeck;
    }

    private String chosenCard;

    private FieldLayoutManager fieldLayoutManager;

    private RendererManager rendererManager;

    private UserManager userManager;

    public String getChosenCard() {
        return chosenCard;
    }

    public PokerCard[] getCards() {
        return cards;
    }

    public StartPlanningPoker(IssueManager issueManager,
                              CustomFieldManager customFieldManager,
                              JiraAuthenticationContext context,
                              PluginSettingsFactory settingsFactory,
                              PlanningPokerStorage planningPokerStorage, StoryPointFieldSupport storyPointFieldSupport, UserManager userManager) {
        this.issueManager = issueManager;
        this.customFieldManager = customFieldManager;
        this.context = context;
        this.settingsFactory = settingsFactory;
        this.planningPokerStorage = planningPokerStorage;
        this.storyPointFieldSupport = storyPointFieldSupport;
        this.userManager = userManager;

        fieldLayoutManager = ComponentAccessor
                .getComponent(FieldLayoutManager.class);
        rendererManager = ComponentAccessor.getComponent(RendererManager.class);
        for (PokerCard card : cards) {
            cardDeck.put(card.getName(), card);
        }
    }

    @Override
    protected String doExecute() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        issueKey = request.getParameter("issueKey");

        ApplicationUser user = context.getUser();
        if (user == null)
            return "error";

        MutableIssue issue = issueManager.getIssueObject(issueKey);

        if (issue == null) {
            addErrorMessage("Issue Key" + issueKey + " not found.");
            return "error";
        }

        cardsForIssue = planningPokerStorage.chosenCardsForIssue(issueKey);
        chosenCard = cardsForIssue.get(user.getKey());

        issueSummary = issue.getSummary();
        issueProjectName = issue.getProjectObject().getName();
        issueProjectKey = issue.getProjectObject().getKey();
        issueStoryPoints = storyPointFieldSupport.getValue(issueKey);

        return "start";
    }

    private Set<Integer> getSortedVotes(Map<String, String> votes) {
        Collection<String> votedValues = votes.values();
        Set<Integer> uniqueValues = new TreeSet<Integer>();
        for (String value : votedValues) {
            if (!value.equals("q")) {
                uniqueValues.add(new Integer(value));
            }
        }
        return uniqueValues;
    }

    public Collection<String> getBoundedVotes() {
        ArrayList<String> votes = new ArrayList<String>();
        for (Integer value : getSortedVotes(cardsForIssue)) {
            votes.add(value.toString());
        }
        ArrayList<String> boundedVotes = new ArrayList<String>();

        String first = votes.get(0);
        String last = votes.get(votes.size() - 1);

        if (votes.size() > 0) {

            int index = 0;
            while (!cards[index].getName().equals(first)) {
                index++;
            }
            boundedVotes.add(cards[index].getName());
            if (votes.size() > 1) {
                index++;
                while (!cards[index].getName().equals(last)) {
                    boundedVotes.add(cards[index].getName());
                    index++;
                }
                boundedVotes.add(cards[index].getName());
            }
        }
        return boundedVotes;
    }

    public Map<String, String> getCardsForIssue() {
        return cardsForIssue;
    }

    public String getUsername(String key) {
        return userManager.getUserByKey(key).getDisplayName();
    }

}
