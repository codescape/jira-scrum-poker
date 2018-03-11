package net.congstar.jira.plugins.scrumpoker.model;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ScrumPokerCardTest {

    @Test
    public void shouldBeEqualToCardWithSameName() {
        assertThat(new ScrumPokerCard("sameName"), is(equalTo(new ScrumPokerCard("sameName"))));
    }

    @Test
    public void shouldReturnNameAsToString() {
        assertThat(new ScrumPokerCard("someString").toString(), is(equalTo("someString")));
    }

}
