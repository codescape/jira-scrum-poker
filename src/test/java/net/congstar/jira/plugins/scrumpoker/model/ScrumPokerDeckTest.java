package net.congstar.jira.plugins.scrumpoker.model;

import org.junit.Test;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.assertThat;

public class ScrumPokerDeckTest {

    @Test
    public void shouldIncludeQuestionMark() {
        assertThat(ScrumPokerDeck.asList(), hasItem(new ScrumPokerCard("?")));
    }

    @Test
    public void shouldIncludeTypicalScrumPokerValues() {
        assertThat(ScrumPokerDeck.asList(), hasItems(
            new ScrumPokerCard("0"),
            new ScrumPokerCard("1"),
            new ScrumPokerCard("2"),
            new ScrumPokerCard("3"),
            new ScrumPokerCard("5"),
            new ScrumPokerCard("8"),
            new ScrumPokerCard("13"),
            new ScrumPokerCard("20"),
            new ScrumPokerCard("40"),
            new ScrumPokerCard("100")));
    }

}
