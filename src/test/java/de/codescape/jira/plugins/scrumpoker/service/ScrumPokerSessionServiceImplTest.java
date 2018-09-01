package de.codescape.jira.plugins.scrumpoker.service;

import com.atlassian.activeobjects.test.TestActiveObjects;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.MutableIssue;
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
import org.mockito.Matchers;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(ActiveObjectsJUnitRunner.class)
@Data(ScrumPokerSessionServiceImplTest.ScrumPokerDatabaseUpdater.class)
@Jdbc(Hsql.class)
@NameConverters
public class ScrumPokerSessionServiceImplTest {

    private EntityManager entityManager;
    private TestActiveObjects activeObjects;

    private ScrumPokerSessionService scrumPokerSessionService;

    @Before
    public void before() {
        activeObjects = new TestActiveObjects(entityManager);

        IssueManager issueManager = mock(IssueManager.class);
        MutableIssue issue = mock(MutableIssue.class);
        when(issueManager.getIssueObject(Matchers.startsWith("ISSUE-"))).thenReturn(issue);

        ScrumPokerSettingsService scrumPokerSettingsService = mock(ScrumPokerSettingsService.class);
        when(scrumPokerSettingsService.loadSessionTimeout()).thenReturn(12);

        scrumPokerSessionService = new ScrumPokerSessionServiceImpl(activeObjects, issueManager,
            scrumPokerSettingsService);
    }

    @Test
    public void shouldCreateSessionIfNotExists() {
        scrumPokerSessionService.byIssueKey("ISSUE-1", "USER-1");
        assertThat(activeObjects.get(ScrumPokerSession.class, "ISSUE-1").getCreatorUserKey(), equalTo("USER-1"));
    }

    @Test
    public void shouldAddVoteToSession() {
        scrumPokerSessionService.addVote("ISSUE-1", "USER-1", "5");
        assertThat(activeObjects.get(ScrumPokerSession.class, "ISSUE-1").getVotes().length, equalTo(1));
    }

    @Test
    public void shouldOverrideVoteOnSessionForSameUser() {
        scrumPokerSessionService.addVote("ISSUE-1", "USER-1", "5");
        scrumPokerSessionService.addVote("ISSUE-1", "USER-1", "8");
        assertThat(activeObjects.get(ScrumPokerSession.class, "ISSUE-1").getVotes().length, equalTo(1));
        assertThat(activeObjects.get(ScrumPokerSession.class, "ISSUE-1").getVotes()[0].getVote(), equalTo("8"));
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

    @Test
    public void shouldCancelASessionIfCurrentUserIsTheCreatorOfTheSession() {
        scrumPokerSessionService.byIssueKey("ISSUE-9", "USER-1");
        ScrumPokerSession cancelledSession = scrumPokerSessionService.cancel("ISSUE-9", "USER-1");
        assertThat(cancelledSession.isCancelled(), is(true));
    }

    @Test
    public void shouldNotCancelASessionIfCurrentUserIsNotTheCreatorOfTheSession() {
        scrumPokerSessionService.byIssueKey("ISSUE-9", "USER-1");
        ScrumPokerSession cancelledSession = scrumPokerSessionService.cancel("ISSUE-9", "USER-2");
        assertThat(cancelledSession.isCancelled(), is(false));
    }

    @Test
    public void shouldReturnReferencesWithTheSameUserAndEstimation() {
        scrumPokerSessionService.addVote("ISSUE-1", "USER-1", "3");
        scrumPokerSessionService.confirm("ISSUE-1", "USER-1", 8);

        scrumPokerSessionService.addVote("ISSUE-2", "USER-1", "8");
        scrumPokerSessionService.confirm("ISSUE-2", "USER-1", 8);

        scrumPokerSessionService.addVote("ISSUE-5", "USER-1", "5");
        scrumPokerSessionService.confirm("ISSUE-5", "USER-1", 8);

        scrumPokerSessionService.addVote("ISSUE-8", "USER-1", "8");
        scrumPokerSessionService.confirm("ISSUE-8", "USER-1", 8);

        scrumPokerSessionService.addVote("ISSUE-3", "USER-2", "8");
        scrumPokerSessionService.confirm("ISSUE-3", "USER-2", 8);

        List<ScrumPokerSession> references = scrumPokerSessionService.references("USER-1", 8);
        assertThat(references.size(), is(3));
        assertThat(references.stream().map(ScrumPokerSession::getIssueKey).collect(Collectors.toList()),
            hasItems("ISSUE-8", "ISSUE-5", "ISSUE-2"));
    }

    @Test(expected = IllegalStateException.class)
    public void shouldNotCreateSessionForIssueKeyWhichDoesNotExist() {
        scrumPokerSessionService.byIssueKey("UNKNOWN", "USER-1");
    }

    public static final class ScrumPokerDatabaseUpdater implements DatabaseUpdater {

        @Override
        public void update(EntityManager entityManager) throws Exception {
            entityManager.migrate(ScrumPokerSession.class, ScrumPokerVote.class);
        }

    }

}
