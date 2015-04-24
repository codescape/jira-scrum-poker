package net.congstar.jira.plugins.planningpoker.data;

import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.ModifiedValue;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.util.DefaultIssueChangeHolder;
import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import net.congstar.jira.plugins.planningpoker.action.ConfigurePlanningPokerAction;

public class DefaultStoryPointSupport implements StoryPointFieldSupport {

    private final IssueManager issueManager;

    private final PluginSettingsFactory pluginSettingsFactory;

    private final CustomFieldManager customFieldManager;

    public DefaultStoryPointSupport(IssueManager issueManager, PluginSettingsFactory pluginSettingsFactory, CustomFieldManager customFieldManager) {
        this.issueManager = issueManager;
        this.pluginSettingsFactory = pluginSettingsFactory;
        this.customFieldManager = customFieldManager;
    }

    @Override
    public void save(String issueKey, Double newValue) {
        MutableIssue issue = issueManager.getIssueByKeyIgnoreCase(issueKey);
        findStoryPointField().updateValue(null, issue, new ModifiedValue(getValue(issueKey), newValue), new DefaultIssueChangeHolder());
    }

    @Override
    public Double getValue(String issueKey) {
        MutableIssue issue = issueManager.getIssueByKeyIgnoreCase(issueKey);
        return (Double) issue.getCustomFieldValue(findStoryPointField());
    }

    private CustomField findStoryPointField() {
        PluginSettings settings = pluginSettingsFactory.createGlobalSettings();
        String field = (String) settings.get(ConfigurePlanningPokerAction.STORY_POINT_FIELD_NAME);
        String storyPointFieldName = field != null ? field : ConfigurePlanningPokerAction.DEFAULT_FIELD_FOR_STORY_POINTS;

        for (CustomField customField : customFieldManager.getCustomFieldObjects()) {
            if (customField.getNameKey().equalsIgnoreCase(storyPointFieldName)) {
                return customField;
            }
        }

        return null;
    }

}
