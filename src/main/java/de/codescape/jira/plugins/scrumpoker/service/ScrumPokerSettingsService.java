package de.codescape.jira.plugins.scrumpoker.service;

import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Component to save and load settings for the Scrum poker plugin using {@link PluginSettingsFactory}.
 */
@Named
@Scanned
public class ScrumPokerSettingsService {

    static final String SETTINGS_NAMESPACE = "de.codescape.jira.plugins.scrum-poker";
    static final String STORY_POINT_FIELD = "storyPointField";

    @ComponentImport
    private final PluginSettingsFactory pluginSettingsFactory;

    @Inject
    public ScrumPokerSettingsService(PluginSettingsFactory pluginSettingsFactory) {
        this.pluginSettingsFactory = pluginSettingsFactory;
    }

    /**
     * Loads the currently saved story point field from the settings.
     *
     * @return story point field id
     */
    public String loadStoryPointFieldId() {
        return (String) pluginSettingsFactory.createGlobalSettings().get(settingsKey(STORY_POINT_FIELD));
    }

    /**
     * Persists the new story point field id or deletes it if empty or null.
     *
     * @param storyPointFieldId new story point field id
     */
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
