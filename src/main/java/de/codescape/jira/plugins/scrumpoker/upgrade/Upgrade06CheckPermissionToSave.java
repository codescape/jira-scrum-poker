package de.codescape.jira.plugins.scrumpoker.upgrade;

import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.sal.api.upgrade.PluginUpgradeTask;
import de.codescape.jira.plugins.scrumpoker.model.GlobalSettings;
import de.codescape.jira.plugins.scrumpoker.service.ScrumPokerSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Set the default setting for configuration option Estimation Permission Check and do not check for permission.
 *
 * @since 4.2.0
 */
@Component
@ExportAsService(PluginUpgradeTask.class)
public class Upgrade06CheckPermissionToSave extends AbstractUpgradeTask {

    private final ScrumPokerSettingService scrumPokerSettingService;

    @Autowired
    public Upgrade06CheckPermissionToSave(ScrumPokerSettingService scrumPokerSettingService) {
        this.scrumPokerSettingService = scrumPokerSettingService;
    }

    @Override
    public int getBuildNumber() {
        return 6;
    }

    @Override
    public String getShortDescription() {
        return "Set default value for check permission to save.";
    }

    @Override
    protected void performUpgrade() {
        GlobalSettings globalSettings = scrumPokerSettingService.load();
        globalSettings.setCheckPermissionToSaveEstimate(false);
        scrumPokerSettingService.persist(globalSettings);
    }

}
