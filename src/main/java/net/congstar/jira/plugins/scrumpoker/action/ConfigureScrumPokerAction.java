package net.congstar.jira.plugins.scrumpoker.action;

import java.util.ArrayList;
import java.util.List;

import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;

public class ConfigureScrumPokerAction extends JiraWebActionSupport {

    private static final long serialVersionUID = 1L;

    public static final String STORY_POINT_FIELD_NAME = "storyPointFieldName";

    public static final String DEFAULT_FIELD_FOR_STORY_POINTS = "Story-Punkte";

    private final PluginSettingsFactory settingsFactory;

    private final CustomFieldManager customFieldManager;

    public ConfigureScrumPokerAction(PluginSettingsFactory settingsFactory, CustomFieldManager customFieldManager) {
        this.settingsFactory = settingsFactory;
        this.customFieldManager = customFieldManager;
    }

    /**
     * List of all custom field names currently available in this JIRA instance.
     */
    public List<String> getCustomFieldNames() {
        List<String> customFieldNames = new ArrayList<String>();
        for (CustomField customField : customFieldManager.getCustomFieldObjects()) {
            customFieldNames.add(customField.getFieldName());
        }
        return customFieldNames;
    }

    /**
     * Current configured name of the story point field in this JIRA instance.
     */
    public String getStoryPointFieldName() {
        PluginSettings settings = settingsFactory.createGlobalSettings();
        return (String) settings.get(STORY_POINT_FIELD_NAME);
    }

    @Override
    protected String doExecute() throws Exception {
        String newStoryPointFieldName = getHttpRequest().getParameter(STORY_POINT_FIELD_NAME);
        if (newStoryPointFieldName != null) {
            PluginSettings settings = settingsFactory.createGlobalSettings();
            settings.put(STORY_POINT_FIELD_NAME, newStoryPointFieldName);
        }
        return "success";
    }

}
