package de.codescape.jira.plugins.scrumpoker.upgrade;

import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.sal.api.upgrade.PluginUpgradeTask;
import de.codescape.jira.plugins.scrumpoker.service.ScrumPokerSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Set the default setting for project activation for Scrum Poker to true to have the same behavior as before.
 *
 * @since 3.8.0
 */
@Component
@ExportAsService(PluginUpgradeTask.class)
public class Upgrade02DefaultProjectActivation extends AbstractUpgradeTask {

    private final ScrumPokerSettingService scrumPokerSettingService;

    @Autowired
    public Upgrade02DefaultProjectActivation(ScrumPokerSettingService scrumPokerSettingService) {
        this.scrumPokerSettingService = scrumPokerSettingService;
    }

    @Override
    public int getBuildNumber() {
        return 2;
    }

    @Override
    public String getShortDescription() {
        return "Set default value for project activation";
    }

    @Override
    protected void performUpgrade() {
        scrumPokerSettingService.persistDefaultProjectActivation(true);
    }

}
