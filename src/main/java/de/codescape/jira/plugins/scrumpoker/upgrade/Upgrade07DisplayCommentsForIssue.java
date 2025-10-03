package de.codescape.jira.plugins.scrumpoker.upgrade;

import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.sal.api.upgrade.PluginUpgradeTask;
import de.codescape.jira.plugins.scrumpoker.model.GlobalSettings;
import de.codescape.jira.plugins.scrumpoker.service.GlobalSettingsService;
import jakarta.inject.Inject;
import org.springframework.stereotype.Component;

/**
 * Set the default setting for configuration option Display Comments for Issue.
 *
 * @since 4.6.0
 */
@Component
@ExportAsService(PluginUpgradeTask.class)
public class Upgrade07DisplayCommentsForIssue extends AbstractUpgradeTask {

    private final GlobalSettingsService globalSettingsService;

    @Inject
    public Upgrade07DisplayCommentsForIssue(GlobalSettingsService globalSettingsService) {
        this.globalSettingsService = globalSettingsService;
    }

    @Override
    public int getBuildNumber() {
        return 7;
    }

    @Override
    public String getShortDescription() {
        return "Set default value for display comments for issue.";
    }

    @Override
    protected void performUpgrade() {
        GlobalSettings globalSettings = globalSettingsService.load();
        globalSettings.setDisplayCommentsForIssue(GlobalSettings.DISPLAY_COMMENTS_FOR_ISSUE_DEFAULT);
        globalSettingsService.persist(globalSettings);
    }

}
