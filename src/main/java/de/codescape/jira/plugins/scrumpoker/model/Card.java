package de.codescape.jira.plugins.scrumpoker.model;

import java.util.Objects;

/**
 * A card represents one of the cards of a card set that is shown to the Scrum Poker participants to choose from.
 */
public class Card {

    /**
     * The QUESTION_MARK signals that the user who is presenting this card cannot give an estimate.
     */
    public static final Card QUESTION_MARK = new Card("question", false);

    /**
     * The COFFEE_BREAK signals that the user who is presenting this cards needs a break.
     */
    public static final Card COFFEE_BREAK = new Card("coffee", false);

    private final String value;
    private final boolean assignable;

    /**
     * Creates a new card with the given value and flag whether it can be used as an estimate.
     *
     * @param value      value of the card
     * @param assignable flag whether it can be used as an estimate
     */
    public Card(String value, boolean assignable) {
        this.value = value;
        this.assignable = assignable;
    }

    /**
     * Returns the value of the card.
     */
    public String getValue() {
        return value;
    }

    /**
     * Returns whether this card can be agreed on and used as an estimate.
     */
    public boolean isAssignable() {
        return assignable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return assignable == card.assignable &&
            value.equals(card.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, assignable);
    }

    @Override
    public String toString() {
        return "Card{" +
            "value='" + value + '\'' +
            ", assignable=" + assignable +
            '}';
    }

}
