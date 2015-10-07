package net.congstar.jira.plugins.scrumpoker.data;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import net.congstar.jira.plugins.scrumpoker.model.ScrumPokerSession;

import org.junit.Test;

public class DefaultPlanningPokerStorageTest {

    @Test
    public void shouldProduceNewSessionIfNonExists() {
        DefaultPlanningPokerStorage storage = new DefaultPlanningPokerStorage();

        assertThat(storage.sessionForIssue("Issue-1"), is(not(nullValue())));
    }
    
    @Test
    public void shouldReturnExistingSessionIfExists() {
        DefaultPlanningPokerStorage storage = new DefaultPlanningPokerStorage();

        ScrumPokerSession sessionForIssue = storage.sessionForIssue("Issue-1");

        assertThat(storage.sessionForIssue("Issue-1"), is(equalTo(sessionForIssue)));
    }

}