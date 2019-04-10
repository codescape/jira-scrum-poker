package de.codescape.jira.plugins.scrumpoker.model;

import org.junit.Test;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.assertThat;

public class ScrumPokerCardTest {

    @Test
    public void shouldIncludeQuestionMark() {
        assertThat(ScrumPokerCard.getDeck(), hasItem(ScrumPokerCard.QUESTION_MARK));
    }

    @Test
    public void shouldIncludeCoffeeCard() {
        assertThat(ScrumPokerCard.getDeck(), hasItem(ScrumPokerCard.COFFEE));
    }

    @Test
    public void shouldIncludeTypicalScrumPokerValues() {
        assertThat(ScrumPokerCard.getDeck(), hasItems(
            ScrumPokerCard.ZERO,
            ScrumPokerCard.ONE,
            ScrumPokerCard.TWO,
            ScrumPokerCard.THREE,
            ScrumPokerCard.FIVE,
            ScrumPokerCard.EIGHT,
            ScrumPokerCard.THIRTEEN,
            ScrumPokerCard.TWENTY,
            ScrumPokerCard.FORTY,
            ScrumPokerCard.HUNDRED)
        );
    }

}
