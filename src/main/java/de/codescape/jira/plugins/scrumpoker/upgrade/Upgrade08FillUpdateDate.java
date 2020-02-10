package de.codescape.jira.plugins.scrumpoker.upgrade;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.upgrade.PluginUpgradeTask;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerSession;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerVote;
import net.java.ao.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;

/**
 * Fill the update date for all existing Scrum Poker sessions.
 *
 * @since 4.8
 */
@Component
@ExportAsService(PluginUpgradeTask.class)
public class Upgrade08FillUpdateDate extends AbstractUpgradeTask {

    private final ActiveObjects activeObjects;

    @Autowired
    public Upgrade08FillUpdateDate(@ComponentImport ActiveObjects activeObjects) {
        this.activeObjects = activeObjects;
    }

    @Override
    public int getBuildNumber() {
        return 8;
    }

    @Override
    public String getShortDescription() {
        return "Fill update date for all existing sessions.";
    }

    @Override
    protected void performUpgrade() {
        ScrumPokerSession[] scrumPokerSessions = activeObjects.find(ScrumPokerSession.class,
            Query.select().where("UPDATE_DATE is null"));
        Arrays.stream(scrumPokerSessions).forEach(this::performUpgradeForSession);
    }

    private void performUpgradeForSession(ScrumPokerSession session) {
        session.setUpdateDate(session.getCreateDate());
        if (session.getVotes() != null) {
            Arrays.stream(session.getVotes())
                .map(ScrumPokerVote::getCreateDate)
                .max(Date::compareTo)
                .ifPresent(session::setUpdateDate);
        }
        if (session.getConfirmedDate() != null && session.getUpdateDate().before(session.getConfirmedDate())) {
            session.setUpdateDate(session.getConfirmedDate());
        }
        session.save();
    }

}
