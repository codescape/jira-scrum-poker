package de.codescape.jira.plugins.scrumpoker.upgrade;

import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.sal.api.upgrade.PluginUpgradeTask;
import de.codescape.jira.plugins.scrumpoker.model.GlobalSettings;
import de.codescape.jira.plugins.scrumpoker.service.GlobalSettingsService;
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

    private final GlobalSettingsService globalSettingsService;

    @Autowired
    public Upgrade06CheckPermissionToSave(GlobalSettingsService globalSettingsService) {
        this.globalSettingsService = globalSettingsService;
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
        GlobalSettings globalSettings = globalSettingsService.load();
        globalSettings.setCheckPermissionToSaveEstimate(false);
        globalSettingsService.persist(globalSettings);
    }

}
