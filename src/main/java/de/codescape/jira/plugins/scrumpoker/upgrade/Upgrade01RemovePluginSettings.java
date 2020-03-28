package de.codescape.jira.plugins.scrumpoker.upgrade;

import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.atlassian.sal.api.upgrade.PluginUpgradeTask;
import de.codescape.jira.plugins.scrumpoker.model.GlobalSettings;
import de.codescape.jira.plugins.scrumpoker.service.GlobalSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static de.codescape.jira.plugins.scrumpoker.ScrumPokerConstants.SCRUM_POKER_PLUGIN_KEY;

/**
 * Remove the configuration from PluginSettings and migrate information into the Active Objects based configuration.
 *
 * @since 3.5.0
 */
@Component
@ExportAsService(PluginUpgradeTask.class)
public class Upgrade01RemovePluginSettings extends AbstractUpgradeTask {

    private static final String STORY_POINT_PLUGIN_SETTINGS = SCRUM_POKER_PLUGIN_KEY + ".storyPointField";
    private static final String SESSION_TIMEOUT_PLUGIN_SETTINGS = SCRUM_POKER_PLUGIN_KEY + ".sessionTimeout";

    private final PluginSettingsFactory pluginSettingsFactory;
    private final GlobalSettingsService globalSettingsService;

    @Autowired
    public Upgrade01RemovePluginSettings(@ComponentImport PluginSettingsFactory pluginSettingsFactory,
                                         GlobalSettingsService globalSettingsService) {
        this.pluginSettingsFactory = pluginSettingsFactory;
        this.globalSettingsService = globalSettingsService;
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
        PluginSettings pluginSettings = pluginSettingsFactory.createGlobalSettings();
        // Grab the Story Point field and remove the config if exists
        String storyPointField = (String) pluginSettings.get(STORY_POINT_PLUGIN_SETTINGS);
        if (storyPointField != null) {
            pluginSettings.remove(STORY_POINT_PLUGIN_SETTINGS);
            GlobalSettings globalSettings = globalSettingsService.load();
            globalSettings.setStoryPointField(storyPointField);
            globalSettingsService.persist(globalSettings);
        }
        // Grab the Session Timeout and remove the config if exists
        String sessionTimeout = (String) pluginSettings.get(SESSION_TIMEOUT_PLUGIN_SETTINGS);
        if (sessionTimeout != null) {
            pluginSettings.remove(SESSION_TIMEOUT_PLUGIN_SETTINGS);
            GlobalSettings globalSettings = globalSettingsService.load();
            globalSettings.setSessionTimeout(Integer.valueOf(sessionTimeout));
            globalSettingsService.persist(globalSettings);
        }
    }

}
