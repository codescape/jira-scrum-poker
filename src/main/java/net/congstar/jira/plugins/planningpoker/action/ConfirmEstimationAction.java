package net.congstar.jira.plugins.planningpoker.action;

import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.ModifiedValue;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.util.DefaultIssueChangeHolder;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;

public class ConfirmEstimationAction extends JiraWebActionSupport {

    private final IssueManager issueManager;

    private final CustomFieldManager customFieldManager;

    private final PluginSettingsFactory settingsFactory;

    public ConfirmEstimationAction(IssueManager issueManager, CustomFieldManager customFieldManager, PluginSettingsFactory settingsFactory) {
        this.issueManager = issueManager;
        this.customFieldManager = customFieldManager;
        this.settingsFactory = settingsFactory;
    }

    @Override
    protected String doExecute() throws Exception {
        String issueKey = request.getParameter("issueKey");
        String finalVote = request.getParameter("finalVote");

        MutableIssue issue = issueManager.getIssueByKeyIgnoreCase(issueKey);

        CustomField storyPointsField = findStoryPointField();

        Double currentStoryPoints = (Double) issue.getCustomFieldValue(storyPointsField);

        storyPointsField.updateValue(null, issue, new ModifiedValue(currentStoryPoints, new Double(finalVote)),
                new DefaultIssueChangeHolder());

        return getRedirect("/browse/" + issueKey);
    }

    private CustomField findStoryPointField() {
        PluginSettings settings = settingsFactory.createGlobalSettings();
        String field = (String) settings.get(ConfigurePlanningPokerAction.STORY_POINT_FIELD_NAME);
        String storyPointFieldName = field == null ? "points" : field;

        for (CustomField customField : customFieldManager.getCustomFieldObjects()) {
            if (customField.getNameKey().equalsIgnoreCase(storyPointFieldName)) {
                return customField;
            }
        }
        return null;
    }

}
