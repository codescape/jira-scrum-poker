package de.codescape.jira.plugins.scrumpoker;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ScrumPokerConstantsTest {

    @Test
    public void shouldExposePluginKey() {
        assertThat(ScrumPokerConstants.SCRUM_POKER_PLUGIN_KEY, is(not(nullValue())));
    }

}
