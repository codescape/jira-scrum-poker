package net.congstar.jira.plugins.planningpoker.action;

import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.ModifiedValue;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.util.DefaultIssueChangeHolder;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;

import webwork.action.ServletActionContext;

import javax.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;


public final class StartPlanningPoker extends JiraWebActionSupport {

    private static final long serialVersionUID = 1L;

    private final IssueManager issueManager;

    private final JiraAuthenticationContext context;

    private final CustomFieldManager customFieldManager;

    private final PluginSettingsFactory settingsFactory;

    private String issueSummary;

    private Double issueStoryPoints;

    private String issueKey;

    private String issueProjectName;

    private String issueProjectKey;

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

	private PokerCard[] cards = {
			new PokerCard("Q", "q.jpg", "q_.jpg"),
			new PokerCard("0", "0.jpg", "0_.jpg"),
			new PokerCard("1", "1.jpg", "1_.jpg"),
			new PokerCard("2", "2.jpg", "2_.jpg"),
			new PokerCard("3", "3.jpg", "3_.jpg"),
			new PokerCard("5", "5.jpg", "5_.jpg"),
			new PokerCard("8", "8.jpg", "8_.jpg"),
			new PokerCard("13", "13.jpg", "13_.jpg"),
			new PokerCard("20", "20.jpg", "20_.jpg"),
			new PokerCard("40", "40.jpg", "40_.jpg"),
			new PokerCard("100", "100.jpg", "100_.jpg")
	};

	private String chosenCard;

	public String getChosenCard() {
		return chosenCard;
	}

	public PokerCard[] getCards() {
		return cards;
	}

    public StartPlanningPoker(IssueManager issueManager, CustomFieldManager customFieldManager, JiraAuthenticationContext context, PluginSettingsFactory settingsFactory) {
        this.issueManager = issueManager;
        this.customFieldManager = customFieldManager;
        this.context = context;
        this.settingsFactory = settingsFactory;
    }

    @Override
    protected String doExecute() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        issueKey = request.getParameter("issueKey");

        ApplicationUser user = context.getUser();
        CustomField storyPointsField = findStoryPointField();
        MutableIssue issue = issueManager.getIssueObject(issueKey);

        if (issue == null) {
            addErrorMessage("Issue Key" + issueKey + " not found.");
            return "error";
        }

        chosenCard = request.getParameter("choose");
        if (chosenCard != null) {
            PlanningPokerCardStorage.update(issueKey, user.getKey(), chosenCard);
        }
        cardsForIssue = PlanningPokerCardStorage.chosenCardsForIssue(issueKey);

        issueSummary = issue.getSummary();
        issueProjectName = issue.getProjectObject().getName();
        issueProjectKey = issue.getProjectObject().getKey();
        issueStoryPoints = (Double) issue.getCustomFieldValue(storyPointsField);
        storyPointsField.updateValue(null, issue, new ModifiedValue(issueStoryPoints, new Double(8)), new DefaultIssueChangeHolder());

        return "start";
    }

    private CustomField findStoryPointField() {
        PluginSettings settings = settingsFactory.createGlobalSettings();
        String storyPointFieldName = settings.get(ConfigurePlanningPoker.STORY_POINT_FIELD_NAME) != null ? (String) settings.get(ConfigurePlanningPoker.STORY_POINT_FIELD_NAME) : "points";
        List<CustomField> field = customFieldManager.getCustomFieldObjects();
        for (CustomField customField : field) {
            if (customField.getNameKey().toLowerCase().contains(storyPointFieldName)) {
                return customField;
            }
        }
        return null;
    }

    public Map<String, String> getCardsForIssue() {
        return cardsForIssue;
    }
}
