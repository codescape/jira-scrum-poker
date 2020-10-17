package de.codescape.jira.plugins.scrumpoker.model;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class AllowRevealDeckTest {

    @Test
    public void valueOfReturnsTheEnumValueForCorrectEntries() {
        assertThat(AllowRevealDeck.valueOf("CREATOR"), is(equalTo(AllowRevealDeck.CREATOR)));
    }

    @Test
    public void AllowRevealDeckHasThreeOptions() {
        assertThat(AllowRevealDeck.values().length, is(3));
    }

}
