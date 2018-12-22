package de.codescape.jira.plugins.scrumpoker;

import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerProject;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerSession;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerSetting;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerVote;
import net.java.ao.EntityManager;
import net.java.ao.test.jdbc.DatabaseUpdater;

/**
 * Database updater used for testing Active Object implementation.
 */
public class ScrumPokerTestDatabaseUpdater implements DatabaseUpdater {

    @Override
    public void update(EntityManager entityManager) throws Exception {
        //noinspection unchecked
        entityManager.migrate(
            ScrumPokerProject.class,
            ScrumPokerSession.class,
            ScrumPokerSetting.class,
            ScrumPokerVote.class
        );
    }

}
