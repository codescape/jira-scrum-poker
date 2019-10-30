package de.codescape.jira.plugins.scrumpoker.model;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class DisplayCommentsForIssueTest {

    @Test
    public void shouldDisplayForValueAll() {
        assertThat(DisplayCommentsForIssue.ALL.shouldDisplay(), is(true));
    }

    @Test
    public void shouldDisplayForValueLatest() {
        assertThat(DisplayCommentsForIssue.LATEST.shouldDisplay(), is(true));
    }

    @Test
    public void shouldNotDisplayForValueNone() {
        assertThat(DisplayCommentsForIssue.NONE.shouldDisplay(), is(false));
    }

}
