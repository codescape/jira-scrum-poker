package de.codescape.jira.plugins.scrumpoker.upgrade;

import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.sal.api.upgrade.PluginUpgradeTask;
import de.codescape.jira.plugins.scrumpoker.model.GlobalSettings;
import de.codescape.jira.plugins.scrumpoker.service.ScrumPokerSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Set the default setting for configuration option Display Dropdown on Boards and hide it by default.
 *
 * @since 4.0.0
 */
@Component
@ExportAsService(PluginUpgradeTask.class)
public class Upgrade05DropdownOnBoards extends AbstractUpgradeTask {

    private final ScrumPokerSettingService scrumPokerSettingService;

    @Autowired
    public Upgrade05DropdownOnBoards(ScrumPokerSettingService scrumPokerSettingService) {
        this.scrumPokerSettingService = scrumPokerSettingService;
    }

    @Override
    public int getBuildNumber() {
        return 5;
    }

    @Override
    public String getShortDescription() {
        return "Set default value for display dropdown on boards.";
    }

    @Override
    protected void performUpgrade() {
        GlobalSettings globalSettings = scrumPokerSettingService.load();
        globalSettings.setDisplayDropdownOnBoards(false);
        scrumPokerSettingService.persist(globalSettings);
    }

}
