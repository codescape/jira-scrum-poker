package de.codescape.jira.plugins.scrumpoker.model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This enumeration represents all possible cards used in a Scrum Poker session.
 */
public enum ScrumPokerCard {

    QUESTION_MARK("question"),
    COFFEE("coffee"),
    ZERO("0"),
    ONE("1"),
    TWO("2"),
    THREE("3"),
    FIVE("5"),
    EIGHT("8"),
    THIRTEEN("13"),
    TWENTY("20"),
    FORTY("40"),
    HUNDRED("100");

    private final String name;

    ScrumPokerCard(String name) {
        this.name = name;
    }

    /**
     * Return the name of the Scrum Poker card.
     */
    public String getName() {
        return name;
    }

    /**
     * Return the whole deck of cards.
     */
    public static List<ScrumPokerCard> getDeck() {
        return Arrays.stream(values()).collect(Collectors.toList());
    }

}
