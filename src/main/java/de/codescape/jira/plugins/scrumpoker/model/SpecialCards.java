package de.codescape.jira.plugins.scrumpoker.model;

/**
 * Special cards that can be used in the card sets to be used for Scrum Poker sessions.
 */
public final class SpecialCards {

    /**
     * The QUESTION_MARK signals that the user presenting this card lacks information to present another card.
     */
    public static final String QUESTION_MARK = "question";

    /**
     * The COFFEE_CARD signals that the user presenting this cards needs a break.
     */
    public static final String COFFEE_CARD = "coffee";

    // Prevent utility class from being instantiated
    private SpecialCards() {
        throw new AssertionError("SpecialCards must not be instantiated.");
    }

}
