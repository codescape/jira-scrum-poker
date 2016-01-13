package net.congstar.jira.plugins.scrumpoker.data;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import net.congstar.jira.plugins.scrumpoker.model.ScrumPokerSession;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DefaultPlanningPokerStorageTest {

    private static final String SOME_ISSUE = "Issue-1";

    private static final String YOUNG_ISSUE = "Young Issue";

    private static final String OLD_ISSUE = "Old Issue";

    private DefaultPlanningPokerStorage storage;

    @Before
    public void before() {
        storage = new DefaultPlanningPokerStorage();
    }

    @After
    public void after() {
        DateTimeUtils.setCurrentMillisSystem();
    }

    @Test
    public void shouldProduceNewSessionIfNonExists() {
        assertThat(storage.sessionForIssue(SOME_ISSUE), is(not(nullValue())));
    }

    @Test
    public void shouldReturnExistingSessionIfExists() {
        ScrumPokerSession sessionForIssue = storage.sessionForIssue(SOME_ISSUE);
        assertThat(storage.sessionForIssue(SOME_ISSUE), is(equalTo(sessionForIssue)));
    }

    @Test
    public void shoudlRemoveSessionsOlderThanOneDay() {
        createOldScrumPokerSession(OLD_ISSUE, DateTime.now().minusDays(2).getMillis());
        assertThat(storage.sessionForIssue(OLD_ISSUE).getStartedOn(), is(equalTo(DateTime.now())));
    }

    @Test
    public void shoudlKeepSessionsYoungerThanOneDay() {
        DateTime youngEnough = DateTime.now().minusHours(14);
        createOldScrumPokerSession(YOUNG_ISSUE, youngEnough.getMillis());
        assertThat(storage.sessionForIssue(YOUNG_ISSUE).getStartedOn(), is(equalTo(youngEnough)));
    }

    private void createOldScrumPokerSession(String issueKey, long creationDate) {
        DateTimeUtils.setCurrentMillisFixed(creationDate);
        storage.sessionForIssue(issueKey);
        DateTimeUtils.setCurrentMillisSystem();
    }

}