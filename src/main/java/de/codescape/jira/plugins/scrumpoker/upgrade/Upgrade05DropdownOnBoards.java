package de.codescape.jira.plugins.scrumpoker.upgrade;

import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.sal.api.upgrade.PluginUpgradeTask;
import de.codescape.jira.plugins.scrumpoker.model.GlobalSettings;
import de.codescape.jira.plugins.scrumpoker.service.GlobalSettingsService;
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

    private final GlobalSettingsService globalSettingsService;

    @Autowired
    public Upgrade05DropdownOnBoards(GlobalSettingsService globalSettingsService) {
        this.globalSettingsService = globalSettingsService;
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
        GlobalSettings globalSettings = globalSettingsService.load();
        globalSettings.setDisplayDropdownOnBoards(false);
        globalSettingsService.persist(globalSettings);
    }

}
