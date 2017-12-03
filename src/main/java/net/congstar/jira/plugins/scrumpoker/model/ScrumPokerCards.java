package net.congstar.jira.plugins.scrumpoker.model;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class ScrumPokerCards {

    public final static ScrumPokerCard[] pokerDeck = {
            new ScrumPokerCard("?"),
            new ScrumPokerCard("0"),
            new ScrumPokerCard("1"),
            new ScrumPokerCard("2"),
            new ScrumPokerCard("3"),
            new ScrumPokerCard("5"),
            new ScrumPokerCard("8"),
            new ScrumPokerCard("13"),
            new ScrumPokerCard("20"),
            new ScrumPokerCard("40"),
            new ScrumPokerCard("100")
    };

    public static Map<String, ScrumPokerCard> asMap() {
        return Arrays.stream(pokerDeck).collect(Collectors.toMap(ScrumPokerCard::getName, card -> card));
    }

}
