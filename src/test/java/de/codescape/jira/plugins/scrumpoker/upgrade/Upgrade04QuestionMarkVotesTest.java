package de.codescape.jira.plugins.scrumpoker.upgrade;

import com.atlassian.activeobjects.test.TestActiveObjects;
import de.codescape.jira.plugins.scrumpoker.ScrumPokerTestDatabaseUpdater;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerVote;
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

@RunWith(ActiveObjectsJUnitRunner.class)
@Data(ScrumPokerTestDatabaseUpdater.class)
@Jdbc(Hsql.class)
@NameConverters
public class Upgrade04QuestionMarkVotesTest {

    @SuppressWarnings("unused")
    private EntityManager entityManager;
    private TestActiveObjects activeObjects;

    private Upgrade04QuestionMarkVotes upgrade;

    @Before
    public void before() {
        activeObjects = new TestActiveObjects(entityManager);
        upgrade = new Upgrade04QuestionMarkVotes(activeObjects);
    }

    @Test
    public void shouldMigrateAllQuestionMarksOnUpgrade() {
        createVoteWithQuestionMark(5);
        upgrade.doUpgrade();
        assertThat(votesWithQuestionMark().length, is(0));
        assertThat(votesWithQuestionString().length, is(5));
    }

    @Test
    public void shouldNotHaveProblemsIfNoQuestionMarksExistOnUpgrade() {
        createVoteWithQuestionMark(0);
        upgrade.doUpgrade();
        assertThat(votesWithQuestionMark().length, is(0));
        assertThat(votesWithQuestionString().length, is(0));
    }

    @Test
    public void shouldReturnCorrectBuildNumber() {
        assertThat(upgrade.getBuildNumber(), is(equalTo(4)));
    }

    @Test
    public void shouldReturnShortDescriptionWithLessThan50Characters() {
        assertThat(upgrade.getShortDescription().length(), is(lessThan(50)));
    }

    private ScrumPokerVote[] votesWithQuestionString() {
        return activeObjects.find(ScrumPokerVote.class,
            Query.select().where("VOTE = ?", "question"));
    }

    private ScrumPokerVote[] votesWithQuestionMark() {
        return activeObjects.find(ScrumPokerVote.class, Query.select().where("VOTE = ?", "?"));
    }

    private void createVoteWithQuestionMark(int count) {
        for (int i = 0; i < count; i++) {
            ScrumPokerVote scrumPokerVote = activeObjects.create(ScrumPokerVote.class);
            scrumPokerVote.setUserKey("SOME-USER-1");
            scrumPokerVote.setVote("?");
            scrumPokerVote.save();
        }
    }

}
