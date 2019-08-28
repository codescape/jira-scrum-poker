package de.codescape.jira.plugins.scrumpoker;

import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class ScrumPokerConstantsTest {

    @Test
    public void shouldExposePluginKey() {
        assertThat(ScrumPokerConstants.SCRUM_POKER_PLUGIN_KEY, is(not(nullValue())));
    }

}
