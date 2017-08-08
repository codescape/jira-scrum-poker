package net.congstar.jira.plugins.scrumpoker.model;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeThat;

public class ScrumPokerCardsTest {

    private static final List<String> CARD_KEYS = Arrays.stream(ScrumPokerCards.pokerDeck)
            .map(ScrumPokerCard::getName).collect(Collectors.toList());

    @Test
    public void asMapShouldReturnMapOfAllCards() {
        assumeThat(CARD_KEYS, hasSize(11));
        for (String cardKey : CARD_KEYS) {
            assertThat(ScrumPokerCards.asMap(), hasKey(cardKey));
        }
    }

}
