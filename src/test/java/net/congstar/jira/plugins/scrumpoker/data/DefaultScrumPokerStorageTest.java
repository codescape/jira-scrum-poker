package net.congstar.jira.plugins.scrumpoker.data;

import net.congstar.jira.plugins.scrumpoker.model.ScrumPokerSession;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class DefaultScrumPokerStorageTest {

    private static final DateTime FOURTEEN_HOURS_AGO = DateTime.now().minusHours(14);
    private static final DateTime EIGHT_HOURS_AGO = DateTime.now().minusHours(8);

    private static final String USER_KEY = "SomeUserKey";

    private static final String SOME_ISSUE = "Issue-1";
    private static final String YOUNG_ISSUE = "Young Issue";
    private static final String OLD_ISSUE = "Old Issue";

    private DefaultScrumPokerStorage storage;

    @Before
    public void before() {
        storage = new DefaultScrumPokerStorage();
    }

    @After
    public void after() {
        DateTimeUtils.setCurrentMillisSystem();
    }

    @Test
    public void shouldProduceNewSessionIfNonExists() {
        assertThat(storage.sessionForIssue(SOME_ISSUE, USER_KEY), is(not(nullValue())));
    }

    @Test
    public void shouldReturnExistingSessionIfExists() {
        ScrumPokerSession sessionForIssue = storage.sessionForIssue(SOME_ISSUE, USER_KEY);
        assertThat(storage.sessionForIssue(SOME_ISSUE, USER_KEY), is(equalTo(sessionForIssue)));
    }

    @Test
    public void shouldRemoveSessionsOlderThanTwelveHours() {
        sessionWithoutConfirmedVoteAndCreationDate(OLD_ISSUE, FOURTEEN_HOURS_AGO.getMillis());
        assertThat(storage.sessionForIssue(OLD_ISSUE, USER_KEY).getStartedOn(), is(greaterThan(DateTime.now().minusMinutes(1))));
    }

    @Test
    public void shouldKeepSessionsYoungerThanTwelveHours() {
        sessionWithoutConfirmedVoteAndCreationDate(YOUNG_ISSUE, EIGHT_HOURS_AGO.getMillis());
        assertThat(storage.sessionForIssue(YOUNG_ISSUE, USER_KEY).getStartedOn(), is(equalTo(EIGHT_HOURS_AGO)));
    }

    @Test
    public void shouldReturnSessionsWithoutConfirmedVoteAsOpenSessions() {
        sessionWithConfirmedVote("CLOSED-1");
        sessionWithoutConfirmedVote("OPEN-1");
        sessionWithConfirmedVote("CLOSED-2");
        sessionWithoutConfirmedVote("OPEN-2");
        assertThat(storage.getOpenSessions(), hasSize(2));
        storage.getOpenSessions().forEach(session -> assertThat(session.getIssueKey(), startsWith("OPEN")));
    }

    @Test
    public void shouldReturnSessionsWithConfirmedVoteAsClosedSessions() {
        sessionWithConfirmedVote("CLOSED-1");
        sessionWithoutConfirmedVote("OPEN-1");
        sessionWithConfirmedVote("CLOSED-2");
        sessionWithoutConfirmedVote("OPEN-2");
        assertThat(storage.getClosedSessions(), hasSize(2));
        storage.getClosedSessions().forEach(session -> assertThat(session.getIssueKey(), startsWith("CLOSED")));
    }

    @Test
    public void shouldReturnOpenSessionsInOrderOfCreation() {
        sessionWithoutConfirmedVoteAndCreationDate("OLDEST", DateTime.now().minusHours(6).getMillis());
        sessionWithoutConfirmedVoteAndCreationDate("YOUNGEST", DateTime.now().minusHours(1).getMillis());
        sessionWithoutConfirmedVoteAndCreationDate("MIDDLE", DateTime.now().minusHours(3).getMillis());
        List<ScrumPokerSession> openSessions = storage.getOpenSessions();
        assertThat(openSessions.get(0).getIssueKey(), is(equalTo("YOUNGEST")));
        assertThat(openSessions.get(1).getIssueKey(), is(equalTo("MIDDLE")));
        assertThat(openSessions.get(2).getIssueKey(), is(equalTo("OLDEST")));
    }

    @Test
    public void shouldReturnClosedSessionsInOrderOfCreation() {
        sessionWithConfirmedVoteAndCreationDate("OLDEST", DateTime.now().minusHours(6).getMillis());
        sessionWithConfirmedVoteAndCreationDate("YOUNGEST", DateTime.now().minusHours(1).getMillis());
        sessionWithConfirmedVoteAndCreationDate("MIDDLE", DateTime.now().minusHours(3).getMillis());
        List<ScrumPokerSession> closedSessions = storage.getClosedSessions();
        assertThat(closedSessions.get(0).getIssueKey(), is(equalTo("YOUNGEST")));
        assertThat(closedSessions.get(1).getIssueKey(), is(equalTo("MIDDLE")));
        assertThat(closedSessions.get(2).getIssueKey(), is(equalTo("OLDEST")));
    }

    @Test
    public void shouldNotReturnOpenSessionsThatAreAlreadyTimedOut() {
        sessionWithoutConfirmedVoteAndCreationDate("TIMED_OUT_SESSION", FOURTEEN_HOURS_AGO.getMillis());
        assertThat(storage.getOpenSessions(), is(empty()));
    }

    @Test
    public void shouldNotReturnClosedSessionsThatAreAlreadyTimedOut() {
        sessionWithConfirmedVoteAndCreationDate("TIMED_OUT_SESSION", FOURTEEN_HOURS_AGO.getMillis());
        assertThat(storage.getClosedSessions(), is(empty()));
    }

    private void sessionWithConfirmedVoteAndCreationDate(String issueKey, long creationDate) {
        DateTimeUtils.setCurrentMillisFixed(creationDate);
        sessionWithConfirmedVote(issueKey);
        DateTimeUtils.setCurrentMillisSystem();
    }

    private void sessionWithConfirmedVote(String issueKey) {
        storage.sessionForIssue(issueKey, USER_KEY).confirm(5);
    }

    private void sessionWithoutConfirmedVoteAndCreationDate(String issueKey, long creationDate) {
        DateTimeUtils.setCurrentMillisFixed(creationDate);
        sessionWithoutConfirmedVote(issueKey);
        DateTimeUtils.setCurrentMillisSystem();
    }

    private void sessionWithoutConfirmedVote(String issueKey) {
        storage.sessionForIssue(issueKey, USER_KEY);
    }

}
