package de.codescape.jira.plugins.scrumpoker.model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ScrumPokerDeck {

    private final static ScrumPokerCard[] POKER_DECK = {
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

    public static List<ScrumPokerCard> asList() {
        return Arrays.stream(POKER_DECK).collect(Collectors.toList());
    }

}
