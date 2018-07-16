package de.codescape.jira.plugins.scrumpoker.service;

import com.atlassian.activeobjects.test.TestActiveObjects;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerSession;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerVote;
import net.java.ao.EntityManager;
import net.java.ao.test.converters.NameConverters;
import net.java.ao.test.jdbc.Data;
import net.java.ao.test.jdbc.DatabaseUpdater;
import net.java.ao.test.jdbc.Hsql;
import net.java.ao.test.jdbc.Jdbc;
import net.java.ao.test.junit.ActiveObjectsJUnitRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(ActiveObjectsJUnitRunner.class)
@Data(DefaultScrumPokerSessionServiceTest.ScrumPokerDatabaseUpdater.class)
@Jdbc(Hsql.class)
@NameConverters
public class DefaultScrumPokerSessionServiceTest {

    private EntityManager entityManager;
    private TestActiveObjects ao;

    private ScrumPokerSessionService scrumPokerSessionService;

    @Before
    public void before() {
        ao = new TestActiveObjects(entityManager);
        scrumPokerSessionService = new DefaultScrumPokerSessionService(ao);
    }

    @Test
    public void shouldCreateSessionIfNotExists() {
        scrumPokerSessionService.byIssueKey("ISSUE-1", "USER-1");
        assertThat(ao.get(ScrumPokerSession.class, "ISSUE-1").getCreatorUserKey(), equalTo("USER-1"));
    }

    @Test
    public void shouldAddVoteToSession() {
        scrumPokerSessionService.addVote("ISSUE-1", "USER-1", "5");
        assertThat(ao.get(ScrumPokerSession.class, "ISSUE-1").getVotes().length, equalTo(1));
    }

    @Test
    public void shouldOverrideVoteOnSessionForSameUser() {
        scrumPokerSessionService.addVote("ISSUE-1", "USER-1", "5");
        scrumPokerSessionService.addVote("ISSUE-1", "USER-1", "8");
        assertThat(ao.get(ScrumPokerSession.class, "ISSUE-1").getVotes().length, equalTo(1));
        assertThat(ao.get(ScrumPokerSession.class, "ISSUE-1").getVotes()[0].getVote(), equalTo("8"));
    }

    @Test
    public void shouldHideSessionWhenNewVoteIsAdded() {
        scrumPokerSessionService.addVote("ISSUE-1", "USER-1", "5");
        scrumPokerSessionService.reveal("ISSUE-1", "USER-1");
        scrumPokerSessionService.addVote("ISSUE-1", "USER-2", "8");
        ScrumPokerSession scrumPokerSession = scrumPokerSessionService.byIssueKey("ISSUE-1", "USER-1");
        assertThat(scrumPokerSession.isVisible(), is(false));
    }

    @Test
    public void shouldRemoveAllVotesOnReset() {
        scrumPokerSessionService.addVote("ISSUE-1", "USER-1", "5");
        scrumPokerSessionService.addVote("ISSUE-1", "USER-2", "8");
        ScrumPokerSession scrumPokerSession = scrumPokerSessionService.reset("ISSUE-1", "USER-3");
        assertThat(scrumPokerSession.getVotes().length, equalTo(0));
    }

    @Test
    public void shouldReturnRecentSessions() {
        scrumPokerSessionService.addVote("ISSUE-1", "USER-2", "6");
        List<ScrumPokerSession> recent = scrumPokerSessionService.recent();
        assertThat(recent.size(), equalTo(1));
    }

    public static final class ScrumPokerDatabaseUpdater implements DatabaseUpdater {

        @Override
        public void update(EntityManager entityManager) throws Exception {
            entityManager.migrate(ScrumPokerSession.class, ScrumPokerVote.class);
        }

    }

}
