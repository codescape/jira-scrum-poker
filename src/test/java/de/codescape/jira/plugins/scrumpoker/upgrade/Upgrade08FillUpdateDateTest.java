package de.codescape.jira.plugins.scrumpoker.upgrade;

import com.atlassian.activeobjects.test.TestActiveObjects;
import de.codescape.jira.plugins.scrumpoker.ScrumPokerTestDatabaseUpdater;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerSession;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerVote;
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

import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.fail;

@RunWith(ActiveObjectsJUnitRunner.class)
@Data(ScrumPokerTestDatabaseUpdater.class)
@Jdbc(Hsql.class)
@NameConverters
public class Upgrade08FillUpdateDateTest {

    @SuppressWarnings("unused")
    private EntityManager entityManager;
    private TestActiveObjects activeObjects;

    private Upgrade08FillUpdateDate upgrade;

    private static final Date OLD_DATE = new Date(System.currentTimeMillis() / 1000 * 1000 - 1000 * 3600 * 24 * 10);
    private static final Date MIDDLE_DATE = new Date(System.currentTimeMillis() / 1000 * 1000 - 1000 * 3600 * 10);
    private static final Date NEW_DATE = new Date(System.currentTimeMillis() / 1000 * 1000 - 1000 * 3600 * 2);

    @Before
    public void before() {
        activeObjects = new TestActiveObjects(entityManager);
        upgrade = new Upgrade08FillUpdateDate(activeObjects);
    }

    @Test
    public void sessionWithoutVotesShouldHaveSetTheCreateDateAsUpdateDate() {
        createScrumPokerSession("ISSUE-1", OLD_DATE);

        upgrade.performUpgrade();

        ScrumPokerSession upgradedScrumPokerSession = getScrumPokerSession("ISSUE-1");
        assertThat(upgradedScrumPokerSession.getUpdateDate(), is(equalTo(upgradedScrumPokerSession.getCreateDate())));
    }

    @Test
    public void sessionWithVotesShouldHaveSetTheLastVoteDateAsUpdateDate() {
        ScrumPokerSession scrumPokerSession = createScrumPokerSession("ISSUE-2", OLD_DATE);
        createScrumPokerVote(scrumPokerSession, MIDDLE_DATE);

        upgrade.performUpgrade();

        ScrumPokerSession upgradedScrumPokerSession = getScrumPokerSession("ISSUE-2");
        assertThat(upgradedScrumPokerSession.getUpdateDate(), is(equalTo(scrumPokerSession.getVotes()[0].getCreateDate())));
    }

    @Test
    public void sessionWithVotesAndConfirmedDateShouldHaveTheMaximumDateAsUpdateDate() {
        ScrumPokerSession scrumPokerSession = createScrumPokerSession("ISSUE-3", OLD_DATE);
        createScrumPokerVote(scrumPokerSession, MIDDLE_DATE);
        scrumPokerSession.setConfirmedDate(NEW_DATE);
        scrumPokerSession.save();

        upgrade.performUpgrade();

        ScrumPokerSession upgradedScrumPokerSession = getScrumPokerSession("ISSUE-3");
        assertThat(upgradedScrumPokerSession.getUpdateDate(), is(equalTo(upgradedScrumPokerSession.getConfirmedDate())));
    }

    @Test
    public void shouldReturnCorrectBuildNumber() {
        assertThat(upgrade.getBuildNumber(), is(equalTo(8)));
    }

    @Test
    public void shouldReturnShortDescriptionWithLessThan50Characters() {
        assertThat(upgrade.getShortDescription().length(), is(lessThan(50)));
    }

    private void createScrumPokerVote(ScrumPokerSession scrumPokerSession, Date createDate) {
        ScrumPokerVote scrumPokerVote = activeObjects.create(ScrumPokerVote.class);
        scrumPokerVote.setSession(scrumPokerSession);
        scrumPokerVote.setCreateDate(createDate);
        scrumPokerVote.save();
    }

    private ScrumPokerSession createScrumPokerSession(String issueKey, Date createDate) {
        ScrumPokerSession scrumPokerSession = activeObjects.create(ScrumPokerSession.class, new DBParam("ISSUE_KEY", issueKey));
        scrumPokerSession.setCreateDate(createDate);
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
