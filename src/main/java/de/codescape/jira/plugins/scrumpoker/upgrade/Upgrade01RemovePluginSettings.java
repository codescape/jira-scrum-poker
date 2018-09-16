package de.codescape.jira.plugins.scrumpoker.upgrade;

import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import de.codescape.jira.plugins.scrumpoker.service.ScrumPokerSettingService;

import javax.inject.Inject;

/**
 * Remove the configuration from PluginSettings and migrate information into the Active Objects based configuration.
 *
 * @since 3.5.0
 */
public class Upgrade01RemovePluginSettings extends AbstractUpgradeTask {

    private final PluginSettingsFactory pluginSettingsFactory;

    private final ScrumPokerSettingService scrumPokerSettingService;

    @Inject
    public Upgrade01RemovePluginSettings(PluginSettingsFactory pluginSettingsFactory,
                                         ScrumPokerSettingService scrumPokerSettingService) {
        this.pluginSettingsFactory = pluginSettingsFactory;
        this.scrumPokerSettingService = scrumPokerSettingService;
    }

    @Override
    public int getBuildNumber() {
        return 1;
    }

    @Override
    public String getShortDescription() {
        return "Remove old PluginSettings based configuration";
    }

    @Override
    protected void performUpgrade() {
        PluginSettings globalSettings = pluginSettingsFactory.createGlobalSettings();

        // Grab the Story Point field and remove the config if exists
        Object storyPointField = globalSettings.get(SCRUM_POKER_PLUGIN_KEY + ".storyPointField");
        String newStoryPointField = null;
        if (storyPointField != null) {
            globalSettings.remove(SCRUM_POKER_PLUGIN_KEY + ".storyPointField");
            newStoryPointField = (String) storyPointField;
        }

        // Grab the Session Timeout and remove the config if exists
        Object sessionTimeout = globalSettings.get(SCRUM_POKER_PLUGIN_KEY + ".sessionTimeout");
        String newSessionTimeout = null;
        if (sessionTimeout != null) {
            globalSettings.remove(SCRUM_POKER_PLUGIN_KEY + ".sessionTimeout");
            newSessionTimeout = (String) sessionTimeout;
        }

        // Persist the values if minimum the Story Point field is configured
        if (newStoryPointField != null) {
            scrumPokerSettingService.persistSettings(newStoryPointField, newSessionTimeout);
        }
    }

}
