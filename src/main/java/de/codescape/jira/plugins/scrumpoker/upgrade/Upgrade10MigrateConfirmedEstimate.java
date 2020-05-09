package de.codescape.jira.plugins.scrumpoker.upgrade;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.upgrade.PluginUpgradeTask;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerSession;
import net.java.ao.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Migrate deprecated numeric-only values in the database field confirmed_vote for every session with a value set to
 * new string based value in the newly created database field confirmed_estimate.
 *
 * @since 4.11
 */
@Component
@ExportAsService(PluginUpgradeTask.class)
public class Upgrade10MigrateConfirmedEstimate extends AbstractUpgradeTask {

    private final ActiveObjects activeObjects;

    @Autowired
    public Upgrade10MigrateConfirmedEstimate(@ComponentImport ActiveObjects activeObjects) {
        this.activeObjects = activeObjects;
    }

    @Override
    public int getBuildNumber() {
        return 10;
    }

    @Override
    public String getShortDescription() {
        return "Migrate confirmed votes to confirmed estimates.";
    }

    @Override
    protected void performUpgrade() {
        ScrumPokerSession[] scrumPokerSessions = activeObjects.find(ScrumPokerSession.class,
            Query.select().where("CONFIRMED_VOTE is not null"));
        Arrays.stream(scrumPokerSessions).forEach(this::performUpgradeForSession);
    }

    @SuppressWarnings("deprecation")
    private void performUpgradeForSession(ScrumPokerSession session) {
        Integer confirmedVote = session.getConfirmedVote();
        if (confirmedVote != null) {
            session.setConfirmedEstimate(confirmedVote.toString());
        }
        session.setConfirmedVote(null);
        session.save();
    }

}
