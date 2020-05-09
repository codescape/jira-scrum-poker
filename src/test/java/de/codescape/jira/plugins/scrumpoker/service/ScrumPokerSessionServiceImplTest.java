package de.codescape.jira.plugins.scrumpoker.service;

import com.atlassian.activeobjects.test.TestActiveObjects;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.MutableIssue;
import de.codescape.jira.plugins.scrumpoker.ScrumPokerTestDatabaseUpdater;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerSession;
import de.codescape.jira.plugins.scrumpoker.condition.ScrumPokerForIssueCondition;
import de.codescape.jira.plugins.scrumpoker.model.GlobalSettings;
import net.java.ao.EntityManager;
import net.java.ao.test.converters.NameConverters;
import net.java.ao.test.jdbc.Data;
import net.java.ao.test.jdbc.Hsql;
import net.java.ao.test.jdbc.Jdbc;
import net.java.ao.test.junit.ActiveObjectsJUnitRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

@RunWith(ActiveObjectsJUnitRunner.class)
@Data(ScrumPokerTestDatabaseUpdater.class)
@Jdbc(Hsql.class)
@NameConverters
public class ScrumPokerSessionServiceImplTest {

    private static final int EXPECTED_SESSION_TIMEOUT = 12;

    @SuppressWarnings("unused")
    private EntityManager entityManager;
    private TestActiveObjects activeObjects;

    private ScrumPokerSessionService scrumPokerSessionService;

    private ScrumPokerForIssueCondition scrumPokerForIssueCondition;

    @Before
    public void before() {
        activeObjects = new TestActiveObjects(entityManager);

        IssueManager issueManager = mock(IssueManager.class);
        MutableIssue issue = mock(MutableIssue.class);
        when(issueManager.getIssueObject(ArgumentMatchers.startsWith("ISSUE-"))).thenReturn(issue);

        GlobalSettingsService scrumPokerSettingsService = mock(GlobalSettingsService.class);
        GlobalSettings globalSettings = mock(GlobalSettings.class);
        when(scrumPokerSettingsService.load()).thenReturn(globalSettings);
        when(globalSettings.getSessionTimeout()).thenReturn(EXPECTED_SESSION_TIMEOUT);

        scrumPokerForIssueCondition = mock(ScrumPokerForIssueCondition.class);
        when(scrumPokerForIssueCondition.isEstimable(ArgumentMatchers.any(Issue.class))).thenReturn(true);

        ErrorLogService errorLogService = mock(ErrorLogService.class);

        scrumPokerSessionService = new ScrumPokerSessionServiceImpl(activeObjects, issueManager,
            scrumPokerSettingsService, scrumPokerForIssueCondition, errorLogService);
    }

    @Test
    public void shouldCreateSessionIfNotExists() {
        scrumPokerSessionService.byIssueKey("ISSUE-1", "USER-1");
        assertThat(activeObjects.get(ScrumPokerSession.class, "ISSUE-1").getCreatorUserKey(), equalTo("USER-1"));
    }

    @Test
    public void shouldCreateNewSessionIfUpdateDateOfExistingSessionIsOlderThanSessionTimeout() {
        // given a session with the update date older than the session timeout
        ScrumPokerSession scrumPokerSession = scrumPokerSessionService.byIssueKey("ISSUE-1", "USER-1");
        scrumPokerSession.setCreateDate(beforeTimeout());
        scrumPokerSession.setUpdateDate(scrumPokerSession.getCreateDate());
        scrumPokerSession.save();
        // when requesting this session
        ScrumPokerSession newScrumPokerSession = scrumPokerSessionService.byIssueKey("ISSUE-1", "USER-1");
        // then a new session shall be created and returned
        assertThat(scrumPokerSession.getCreateDate(), is(not(equalTo(newScrumPokerSession.getCreateDate()))));
    }

    @Test
    public void shouldReturnExistingSessionIfUpdateDateOfExistingSessionIsYoungerThanSessionTimeout() {
        // given a session with the update date younger than the session timeout
        ScrumPokerSession scrumPokerSession = scrumPokerSessionService.byIssueKey("ISSUE-1", "USER-1");
        scrumPokerSession.setCreateDate(afterTimeout());
        scrumPokerSession.setUpdateDate(scrumPokerSession.getCreateDate());
        scrumPokerSession.save();
        // when requesting this session
        ScrumPokerSession newScrumPokerSession = scrumPokerSessionService.byIssueKey("ISSUE-1", "USER-1");
        // then the current session shall be returned
        assertThat(scrumPokerSession.getCreateDate(), is(equalTo(newScrumPokerSession.getCreateDate())));
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
    public void shouldAddUserAndDateWhenVoteIsConfirmed() {
        scrumPokerSessionService.addVote("ISSUE-1", "USER-1", "5");
        ScrumPokerSession scrumPokerSession = scrumPokerSessionService.confirm("ISSUE-1", "USER-2", "5");
        assertThat(scrumPokerSession.getConfirmedEstimate(), is(equalTo("5")));
        assertThat(scrumPokerSession.getConfirmedUserKey(), is(equalTo("USER-2")));
        assertThat(scrumPokerSession.getConfirmedDate(), is(notNullValue()));
    }

    @Test
    public void shouldIncludeNewSessionInRecentSessions() {
        // given a newly created session
        scrumPokerSessionService.addVote("ISSUE-1", "USER-2", "5");
        // when querying for recent sessions
        List<ScrumPokerSession> recent = scrumPokerSessionService.recent();
        // then it should be included
        assertThat(recent.size(), equalTo(1));
        assertThat(recent.get(0).getIssueKey(), is(equalTo("ISSUE-1")));
    }

    @Test
    public void shouldOnlyIncludeSessionsWithinTheTimeoutInRecentSessions() {
        // given one session older than the timeout
        scrumPokerSessionService.addVote("ISSUE-1", "USER-2", "5");
        ScrumPokerSession scrumPokerSession = scrumPokerSessionService.byIssueKey("ISSUE-1", "USER-2");
        scrumPokerSession.setUpdateDate(beforeTimeout());
        scrumPokerSession.save();
        // given one session inside the timeout
        scrumPokerSessionService.addVote("ISSUE-2", "USER-2", "8");
        // when querying recent sessions
        List<ScrumPokerSession> recent = scrumPokerSessionService.recent();
        // then only the new one should be included
        assertThat(recent.size(), is(equalTo(1)));
        assertThat(recent.get(0).getIssueKey(), is(equalTo("ISSUE-2")));
    }

    private Date beforeTimeout() {
        return new Date(System.currentTimeMillis() - 1000 * 3600 * (EXPECTED_SESSION_TIMEOUT + 1));
    }

    private Date afterTimeout() {
        return new Date(System.currentTimeMillis() - 1000 * 3600 * (EXPECTED_SESSION_TIMEOUT - 1));
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
        scrumPokerSessionService.confirm("ISSUE-1", "USER-1", "8");

        scrumPokerSessionService.addVote("ISSUE-2", "USER-1", "8");
        scrumPokerSessionService.confirm("ISSUE-2", "USER-1", "8");

        scrumPokerSessionService.addVote("ISSUE-5", "USER-1", "5");
        scrumPokerSessionService.confirm("ISSUE-5", "USER-1", "8");

        scrumPokerSessionService.addVote("ISSUE-8", "USER-1", "8");
        scrumPokerSessionService.confirm("ISSUE-8", "USER-1", "8");

        scrumPokerSessionService.addVote("ISSUE-3", "USER-2", "8");
        scrumPokerSessionService.confirm("ISSUE-3", "USER-2", "8");

        List<ScrumPokerSession> references = scrumPokerSessionService.references("USER-1", "8");
        assertThat(references.size(), is(3));
        assertThat(references.stream().map(ScrumPokerSession::getIssueKey).collect(Collectors.toList()),
            hasItems("ISSUE-8", "ISSUE-5", "ISSUE-2"));
    }

    @Test
    public void shouldReturnAnEmptyListWhenNoReferencesExistWithTheSameEstimationForTheSameUser() {
        scrumPokerSessionService.addVote("ISSUE-1", "USER-1", "3");
        scrumPokerSessionService.confirm("ISSUE-1", "USER-2", "5");

        scrumPokerSessionService.addVote("ISSUE-2", "USER-1", "8");
        scrumPokerSessionService.confirm("ISSUE-2", "USER-2", "3");

        List<ScrumPokerSession> references = scrumPokerSessionService.references("USER-1", "8");
        assertThat(references.isEmpty(), is(true));
    }

    @Test(expected = IllegalStateException.class)
    public void shouldNotCreateSessionForIssueKeyWhichDoesNotExist() {
        scrumPokerSessionService.byIssueKey("UNKNOWN", "USER-1");
        fail();
    }

    @Test(expected = IllegalStateException.class)
    public void shouldNotCreateSessionForIssueWhichCannotBeEstimated() {
        reset(scrumPokerForIssueCondition);
        when(scrumPokerForIssueCondition.isEstimable(ArgumentMatchers.any(Issue.class))).thenReturn(false);
        scrumPokerSessionService.byIssueKey("ISSUE-21", "USER-1");
        fail();
    }

}
