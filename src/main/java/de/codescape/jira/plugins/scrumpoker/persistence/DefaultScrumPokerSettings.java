package de.codescape.jira.plugins.scrumpoker.persistence;

import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;

public class DefaultScrumPokerSettings implements ScrumPokerSettings {

    static final String SETTINGS_NAMESPACE = "de.codescape.jira.plugins.scrum-poker";
    static final String STORY_POINT_FIELD = "storyPointField";

    private final PluginSettingsFactory pluginSettingsFactory;

    public DefaultScrumPokerSettings(PluginSettingsFactory pluginSettingsFactory) {
        this.pluginSettingsFactory = pluginSettingsFactory;
    }

    @Override
    public String loadStoryPointFieldId() {
        return (String) pluginSettingsFactory.createGlobalSettings().get(settingsKey(STORY_POINT_FIELD));
    }

    @Override
    public void persistStoryPointFieldId(String storyPointFieldId) {
        if (storyPointFieldId != null && !storyPointFieldId.isEmpty()) {
            pluginSettingsFactory.createGlobalSettings().put(settingsKey(STORY_POINT_FIELD), storyPointFieldId);
        } else {
            pluginSettingsFactory.createGlobalSettings().remove(settingsKey(STORY_POINT_FIELD));
        }
    }

    private String settingsKey(String key) {
        return SETTINGS_NAMESPACE + "." + key;
    }

}
