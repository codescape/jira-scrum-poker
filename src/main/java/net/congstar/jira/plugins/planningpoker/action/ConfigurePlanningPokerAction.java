package net.congstar.jira.plugins.planningpoker.action;

import com.atlassian.jira.web.action.JiraWebActionSupport;
import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;

public class ConfigurePlanningPokerAction extends JiraWebActionSupport {

    private static final long serialVersionUID = 1L;

    public static final String STORY_POINT_FIELD_NAME = "storyPointFieldName";
    public static final String DEFAULT_FIELD_FOR_STORY_POINTS = "Story-Punkte";

    private final PluginSettingsFactory settingsFactory;

    public ConfigurePlanningPokerAction(PluginSettingsFactory settingsFactory) {
        this.settingsFactory = settingsFactory;
    }

    public String getStoryPointFieldName() {
        PluginSettings settings = settingsFactory.createGlobalSettings();
        return (String) settings.get(STORY_POINT_FIELD_NAME);
    }

    @Override
    protected String doExecute() throws Exception {
        PluginSettings settings = settingsFactory.createGlobalSettings();
        String name = (String) settings.get(STORY_POINT_FIELD_NAME);
        settings.put(STORY_POINT_FIELD_NAME, (name != null) ? name : DEFAULT_FIELD_FOR_STORY_POINTS);
        return "success";
    }

}
