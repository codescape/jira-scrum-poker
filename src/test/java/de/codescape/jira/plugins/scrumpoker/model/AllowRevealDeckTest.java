package de.codescape.jira.plugins.scrumpoker.model;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class AllowRevealDeckTest {

    @Test
    public void valueOfReturnsTheEnumValueForCorrectEntries() {
        assertThat(AllowRevealDeck.valueOf("CREATOR"), is(equalTo(AllowRevealDeck.CREATOR)));
    }

}
