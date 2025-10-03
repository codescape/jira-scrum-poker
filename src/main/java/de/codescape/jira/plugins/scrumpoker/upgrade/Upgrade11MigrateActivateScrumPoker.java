package de.codescape.jira.plugins.scrumpoker.upgrade;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.upgrade.PluginUpgradeTask;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerSetting;
import de.codescape.jira.plugins.scrumpoker.service.GlobalSettingsServiceImpl;
import jakarta.inject.Inject;
import net.java.ao.Query;
import org.springframework.stereotype.Component;

/**
 * Migration task to rename the setting on the persistence layer and have a more expressive name for the settings
 * whether Scrum Poker is activated globally or only for selected projects. The old key 'defaultProjectActivation' is
 * renamed to the better name 'activateScrumPoker' by this upgrade task.
 *
 * @since 20.05.3
 */
@Component
@ExportAsService(PluginUpgradeTask.class)
public class Upgrade11MigrateActivateScrumPoker extends AbstractUpgradeTask {

    public static final String DEFAULT_PROJECT_ACTIVATION = "defaultProjectActivation";

    private final ActiveObjects activeObjects;

    @Inject
    public Upgrade11MigrateActivateScrumPoker(@ComponentImport ActiveObjects activeObjects) {
        this.activeObjects = activeObjects;
    }

    @Override
    public int getBuildNumber() {
        return 11;
    }

    @Override
    public String getShortDescription() {
        return "Migrate flag to globally activate Scrum Poker.";
    }

    @Override
    protected void performUpgrade() {
        ScrumPokerSetting[] results = activeObjects.find(ScrumPokerSetting.class, Query.select()
            .where("KEY = ?", DEFAULT_PROJECT_ACTIVATION));
        if (results.length == 1) {
            ScrumPokerSetting scrumPokerSetting = results[0];
            scrumPokerSetting.setKey(GlobalSettingsServiceImpl.ACTIVATE_SCRUM_POKER);
            scrumPokerSetting.save();
        }
    }

}
