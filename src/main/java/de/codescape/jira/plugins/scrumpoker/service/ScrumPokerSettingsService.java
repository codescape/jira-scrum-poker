package de.codescape.jira.plugins.scrumpoker.service;

import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import org.apache.commons.lang3.math.NumberUtils;

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
    static final String SESSION_TIMEOUT = "sessionTimeout";
    static final Integer SESSION_TIMEOUT_DEFAULT = 12;

    @ComponentImport
    private final PluginSettingsFactory pluginSettingsFactory;

    @Inject
    public ScrumPokerSettingsService(PluginSettingsFactory pluginSettingsFactory) {
        this.pluginSettingsFactory = pluginSettingsFactory;
    }

    /**
     * Loads the currently saved story point field from the global settings.
     *
     * @return story point field id
     */
    public String loadStoryPointFieldId() {
        return (String) pluginSettingsFactory.createGlobalSettings().get(settingsKey(STORY_POINT_FIELD));
    }

    /**
     * Loads the currently saved session timeout from the global settings.
     *
     * @return session timeout in hours
     */
    public Integer loadSessionTimeout() {
        String sessionTimeout = (String) pluginSettingsFactory.createGlobalSettings().get(settingsKey(SESSION_TIMEOUT));
        return NumberUtils.isNumber(sessionTimeout) ? Integer.valueOf(sessionTimeout) : SESSION_TIMEOUT_DEFAULT;
    }

    /**
     * Persist the global settings of the plugin.
     *
     * @param storyPointField story point field id
     * @param sessionTimeout  session timeout in hours
     */
    public void persistSettings(String storyPointField, String sessionTimeout) {
        persistSetting(STORY_POINT_FIELD, storyPointField);
        persistSetting(SESSION_TIMEOUT, sessionTimeout);
    }

    private void persistSetting(String settingsKey, String settingsValue) {
        PluginSettings pluginSettings = pluginSettingsFactory.createGlobalSettings();
        if (settingsValue != null && !settingsValue.isEmpty()) {
            pluginSettings.put(settingsKey(settingsKey), settingsValue);
        } else {
            pluginSettings.remove(settingsKey(settingsKey));
        }
    }

    private String settingsKey(String key) {
        return SETTINGS_NAMESPACE + "." + key;
    }

}
