package de.codescape.jira.plugins.scrumpoker.upgrade;

import com.atlassian.activeobjects.test.TestActiveObjects;
import de.codescape.jira.plugins.scrumpoker.ScrumPokerTestDatabaseUpdater;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerSession;
import net.java.ao.DBParam;
import net.java.ao.EntityManager;
import net.java.ao.Query;
import net.java.ao.test.converters.NameConverters;
import net.java.ao.test.jdbc.Data;
import net.java.ao.test.jdbc.Hsql;
import net.java.ao.test.jdbc.Jdbc;
import net.java.ao.test.junit.ActiveObjectsJUnitRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@RunWith(ActiveObjectsJUnitRunner.class)
@Data(ScrumPokerTestDatabaseUpdater.class)
@Jdbc(Hsql.class)
@NameConverters
public class Upgrade10MigrateConfirmedEstimateTest {

    @SuppressWarnings("unused")
    private EntityManager entityManager;
    private TestActiveObjects activeObjects;

    private Upgrade10MigrateConfirmedEstimate upgrade;

    @Before
    public void before() {
        activeObjects = new TestActiveObjects(entityManager);
        upgrade = new Upgrade10MigrateConfirmedEstimate(activeObjects);
    }

    @Test
    public void shouldReturnCorrectBuildNumber() {
        assertThat(upgrade.getBuildNumber(), is(equalTo(10)));
    }

    @Test
    public void shouldReturnShortDescriptionWithLessThan50Characters() {
        assertThat(upgrade.getShortDescription().length(), is(lessThan(50)));
    }

    @Test
    public void shouldMigrateSessionsWithConfirmedVoteSet() {
        createScrumPokerSession("ISSUE-1", 5, null);

        upgrade.performUpgrade();

        ScrumPokerSession scrumPokerSession = getScrumPokerSession("ISSUE-1");
        assertThat(scrumPokerSession.getConfirmedVote(), is(nullValue()));
        assertThat(scrumPokerSession.getConfirmedEstimate(), is("5"));
    }

    @Test
    public void shouldNotChangeSessionsWithConfirmedVoteAndConfirmedEstimateEmpty() {
        createScrumPokerSession("ISSUE-2", null, null);

        upgrade.performUpgrade();

        ScrumPokerSession scrumPokerSession = getScrumPokerSession("ISSUE-2");
        assertThat(scrumPokerSession.getConfirmedVote(), is(nullValue()));
        assertThat(scrumPokerSession.getConfirmedEstimate(), is(nullValue()));
    }

    @Test
    public void shouldNotChangeSessionsWithConfirmedVoteEmptyButConfirmedEstimateSet() {
        createScrumPokerSession("ISSUE-3", null, "5");

        upgrade.performUpgrade();

        ScrumPokerSession scrumPokerSession = getScrumPokerSession("ISSUE-3");
        assertThat(scrumPokerSession.getConfirmedVote(), is(nullValue()));
        assertThat(scrumPokerSession.getConfirmedEstimate(), is("5"));
    }

    private ScrumPokerSession createScrumPokerSession(String issueKey, Integer confirmedVote, String confirmedEstimate) {
        ScrumPokerSession scrumPokerSession = activeObjects.create(ScrumPokerSession.class, new DBParam("ISSUE_KEY", issueKey));
        scrumPokerSession.setConfirmedVote(confirmedVote);
        scrumPokerSession.setConfirmedEstimate(confirmedEstimate);
        scrumPokerSession.save();
        return scrumPokerSession;
    }

    private ScrumPokerSession getScrumPokerSession(String issueKey) {
        ScrumPokerSession[] scrumPokerSessions = activeObjects.find(ScrumPokerSession.class, Query.select()
            .where("ISSUE_KEY = ?", issueKey));
        if (scrumPokerSessions.length != 1) {
            fail("Scrum Poker session not found.");
        }
        return scrumPokerSessions[0];
    }

}
