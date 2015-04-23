package net.congstar.jira.plugins.planningpoker.action;

import com.atlassian.jira.web.action.JiraWebActionSupport;
import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;

public class ConfigurePlanningPoker extends JiraWebActionSupport {

    private static final long serialVersionUID = 1L;

    public static final String STORY_POINT_FIELD_NAME = "storyPointFieldName";

    private final PluginSettingsFactory settingsFactory;

    public ConfigurePlanningPoker(PluginSettingsFactory settingsFactory) {
        this.settingsFactory = settingsFactory;
    }

    public String getStoryPointFieldName() {
        PluginSettings settings = settingsFactory.createGlobalSettings();
        return (String) settings.get(STORY_POINT_FIELD_NAME);
    }

    public void setStoryPointFieldName(String storyPointFieldName) {
        PluginSettings settings = settingsFactory.createGlobalSettings();
        settings.put(STORY_POINT_FIELD_NAME, storyPointFieldName);
    }

    @Override
    protected String doExecute() throws Exception {
        PluginSettings settings = settingsFactory.createGlobalSettings();
        String name = (String) settings.get(STORY_POINT_FIELD_NAME);
        settings.put(STORY_POINT_FIELD_NAME, (name!=null) ? name: "Story-Punkte");
        name = (String) settings.get(STORY_POINT_FIELD_NAME);
        return "success";
    }

}
