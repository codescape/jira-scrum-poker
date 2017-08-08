package net.congstar.jira.plugins.scrumpoker.model;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class ScrumPokerCards {

    public final static ScrumPokerCard[] pokerDeck = {
            new ScrumPokerCard("q", "q.jpg"),
            new ScrumPokerCard("0", "0.jpg"),
            new ScrumPokerCard("1", "1.jpg"),
            new ScrumPokerCard("2", "2.jpg"),
            new ScrumPokerCard("3", "3.jpg"),
            new ScrumPokerCard("5", "5.jpg"),
            new ScrumPokerCard("8", "8.jpg"),
            new ScrumPokerCard("13", "13.jpg"),
            new ScrumPokerCard("20", "20.jpg"),
            new ScrumPokerCard("40", "40.jpg"),
            new ScrumPokerCard("100", "100.jpg")
    };

    public static Map<String, ScrumPokerCard> asMap() {
        return Arrays.stream(pokerDeck).collect(Collectors.toMap(ScrumPokerCard::getName, card -> card));
    }

}
