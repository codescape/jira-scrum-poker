package de.codescape.jira.plugins.scrumpoker.model;

import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerError;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

public class ErrorTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private ScrumPokerError scrumPokerError;

    @Test
    public void errorCanBeCreatedFromActiveObjectUsingAllFields() {
        when(scrumPokerError.getErrorMessage()).thenReturn("the error message");
        when(scrumPokerError.getStacktrace()).thenReturn("the stacktrace");
        Date expectedDate = new Date();
        when(scrumPokerError.getErrorTimestamp()).thenReturn(expectedDate);
        when(scrumPokerError.getScrumPokerVersion()).thenReturn("the scrum poker version");
        when(scrumPokerError.getJiraVersion()).thenReturn("the jira version");

        Error error = new Error(scrumPokerError);

        assertThat(error.getErrorMessage(), is(equalTo("the error message")));
        assertThat(error.getStacktrace(), is(equalTo("the stacktrace")));
        assertThat(error.getErrorTimestamp(), is(equalTo(expectedDate)));
        assertThat(error.getScrumPokerVersion(), is(equalTo("the scrum poker version")));
        assertThat(error.getJiraVersion(), is(equalTo("the jira version")));
    }

}
