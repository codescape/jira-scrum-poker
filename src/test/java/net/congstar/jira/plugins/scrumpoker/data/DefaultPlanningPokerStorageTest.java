package net.congstar.jira.plugins.scrumpoker.data;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import net.congstar.jira.plugins.scrumpoker.model.ScrumPokerSession;

import org.junit.Before;
import org.junit.Test;

public class DefaultPlanningPokerStorageTest {

    private DefaultPlanningPokerStorage storage;

    @Before
    public void before() {
        storage = new DefaultPlanningPokerStorage();
    }

    @Test
    public void shouldProduceNewSessionIfNonExists() {
        assertThat(storage.sessionForIssue("Issue-1"), is(not(nullValue())));
    }

    @Test
    public void shouldReturnExistingSessionIfExists() {
        ScrumPokerSession sessionForIssue = storage.sessionForIssue("Issue-1");

        assertThat(storage.sessionForIssue("Issue-1"), is(equalTo(sessionForIssue)));
    }

}