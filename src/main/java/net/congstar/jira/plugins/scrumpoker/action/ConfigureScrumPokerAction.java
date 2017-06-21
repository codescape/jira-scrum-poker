package net.congstar.jira.plugins.scrumpoker.action;

import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;

import java.util.List;

public class ConfigureScrumPokerAction extends JiraWebActionSupport {

    private static final long serialVersionUID = 1L;

    // FIXME: Use fully qualified name as advised by com.atlassian.sal.api.pluginsettings.PluginSettingsFactory
    public static final String STORY_POINT_FIELD = "storyPointField";

    private static final String SAVE_ACTION = "action";

    private final PluginSettingsFactory settingsFactory;

    private final CustomFieldManager customFieldManager;

    public ConfigureScrumPokerAction(PluginSettingsFactory settingsFactory, CustomFieldManager customFieldManager) {
        this.settingsFactory = settingsFactory;
        this.customFieldManager = customFieldManager;
    }

    /**
     * List of all custom fields currently available in this JIRA instance.
     */
    public List<CustomField> getCustomFields() {
        return customFieldManager.getCustomFieldObjects();
    }

    /**
     * Current configured id of the story point field in this JIRA instance.
     */
    public String getStoryPointFieldId() {
        PluginSettings settings = settingsFactory.createGlobalSettings();
        return (String) settings.get(STORY_POINT_FIELD);
    }

    @Override
    protected String doExecute() throws Exception {
        String newStoryPointField = getHttpRequest().getParameter(STORY_POINT_FIELD);
        String action = getHttpRequest().getParameter(SAVE_ACTION);
        if (action != null && action.equals("save")) {
            PluginSettings settings = settingsFactory.createGlobalSettings();
            if (newStoryPointField != null && !newStoryPointField.isEmpty()) {
                settings.put(STORY_POINT_FIELD, newStoryPointField);
            } else {
                settings.remove(STORY_POINT_FIELD);
            }
        }
        return "success";
    }

}
