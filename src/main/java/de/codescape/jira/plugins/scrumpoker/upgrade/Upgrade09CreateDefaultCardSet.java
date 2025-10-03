package de.codescape.jira.plugins.scrumpoker.upgrade;

import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.sal.api.upgrade.PluginUpgradeTask;
import de.codescape.jira.plugins.scrumpoker.model.GlobalSettings;
import de.codescape.jira.plugins.scrumpoker.service.GlobalSettingsService;
import jakarta.inject.Inject;
import org.springframework.stereotype.Component;

/**
 * Create the simplified Fibonacci card set as the default card set.
 *
 * @since 4.10.0
 */
@Component
@ExportAsService(PluginUpgradeTask.class)
public class Upgrade09CreateDefaultCardSet extends AbstractUpgradeTask {

    private final GlobalSettingsService globalSettingsService;

    @Inject
    public Upgrade09CreateDefaultCardSet(GlobalSettingsService globalSettingsService) {
        this.globalSettingsService = globalSettingsService;
    }

    @Override
    public int getBuildNumber() {
        return 9;
    }

    @Override
    public String getShortDescription() {
        return "Create the simplified Fibonacci card set.";
    }

    @Override
    protected void performUpgrade() {
        GlobalSettings globalSettings = globalSettingsService.load();
        globalSettings.setCardSet(GlobalSettings.CARD_SET_DEFAULT);
        globalSettingsService.persist(globalSettings);
    }

}
