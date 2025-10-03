package de.codescape.jira.plugins.scrumpoker.upgrade;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.upgrade.PluginUpgradeTask;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerProject;
import de.codescape.jira.plugins.scrumpoker.model.ProjectActivation;
import jakarta.inject.Inject;
import net.java.ao.Query;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Migration task to migrate the old boolean settings on project configuration to new enum that gives three instead of
 * two options for project activation with the following rules:
 * <p>
 * The value <code>ProjectActivation.ACTIVATED</code> is the successor of the old <code>true</code> flag and
 * <code>ProjectActivation.INHERIT</code> is the successor of the old <code>false</code> flag on isScrumPokerEnabled.
 * <p>
 * The new enum value <code>ProjectActivation.DISABLED</code> was not possible in old configuration and thus there is no
 * need to migrate to it in this upgrade task.
 *
 * @since 22.01.0
 */
@Component
@ExportAsService(PluginUpgradeTask.class)
public class Upgrade12MigrateScrumPokerEnabled extends AbstractUpgradeTask {

    private final ActiveObjects activeObjects;

    @Inject
    public Upgrade12MigrateScrumPokerEnabled(@ComponentImport ActiveObjects activeObjects) {
        this.activeObjects = activeObjects;
    }

    @Override
    public int getBuildNumber() {
        return 12;
    }

    @Override
    public String getShortDescription() {
        return "Migrate boolean project activation to new enum.";
    }

    @Override
    @SuppressWarnings("deprecation")
    protected void performUpgrade() {
        ScrumPokerProject[] results = activeObjects.find(ScrumPokerProject.class, Query.select());
        Arrays.stream(results).forEach(scrumPokerProject -> {
            scrumPokerProject.setActivateScrumPoker(
                scrumPokerProject.isScrumPokerEnabled() ? ProjectActivation.ACTIVATE : ProjectActivation.INHERIT);
            scrumPokerProject.save();
        });
    }

}
