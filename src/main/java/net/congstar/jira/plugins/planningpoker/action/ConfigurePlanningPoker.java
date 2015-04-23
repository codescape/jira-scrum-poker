package net.congstar.jira.plugins.planningpoker.action;

import com.atlassian.jira.web.action.JiraWebActionSupport;
import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;


public class ConfigurePlanningPoker extends JiraWebActionSupport {

    private static final long serialVersionUID = 1L;

    public static final String STORY_POINT_FIELD_NAME = "storyPointFieldName";

    private String storyPointFieldName = "points";

    private PluginSettingsFactory settingsFactory;

    public ConfigurePlanningPoker(PluginSettingsFactory settingsFactory) {
        this.settingsFactory = settingsFactory;
    }

    public String getStoryPointFieldName() {
        return storyPointFieldName;
    }

    public void setStoryPointFieldName(String storyPointFieldName) {
        this.storyPointFieldName = storyPointFieldName;
    }

    @Override
    protected String doExecute() throws Exception {
        PluginSettings settings = settingsFactory.createGlobalSettings();
        settings.put(STORY_POINT_FIELD_NAME, storyPointFieldName);
        System.out.println(getStoryPointFieldName());
        return "success";
    }
}
