package de.codescape.jira.plugins.scrumpoker.upgrade;

import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.sal.api.upgrade.PluginUpgradeTask;
import de.codescape.jira.plugins.scrumpoker.model.AllowRevealDeck;
import de.codescape.jira.plugins.scrumpoker.model.GlobalSettings;
import de.codescape.jira.plugins.scrumpoker.service.GlobalSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Set the default setting for configuration option Allow Reveal Deck and allow everyone to reveal a deck.
 *
 * @since 3.9.0
 */
@Component
@ExportAsService(PluginUpgradeTask.class)
public class Upgrade03AllowRevealDeck extends AbstractUpgradeTask {

    private final GlobalSettingsService globalSettingsService;

    @Autowired
    public Upgrade03AllowRevealDeck(GlobalSettingsService globalSettingsService) {
        this.globalSettingsService = globalSettingsService;
    }

    @Override
    public int getBuildNumber() {
        return 3;
    }

    @Override
    public String getShortDescription() {
        return "Set default value for allow reveal deck.";
    }

    @Override
    protected void performUpgrade() {
        GlobalSettings globalSettings = globalSettingsService.load();
        globalSettings.setAllowRevealDeck(AllowRevealDeck.EVERYONE);
        globalSettingsService.persist(globalSettings);
    }

}
