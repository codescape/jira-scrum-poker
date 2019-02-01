package de.codescape.jira.plugins.scrumpoker.model;

/**
 * This enumeration represents all possibilities who can reveal a Scrum Poker session.
 */
public enum AllowRevealDeck {

    /**
     * Only the creator of the session can reveal the deck.
     */
    CREATOR,

    /**
     * Everyone can reveal the deck.
     */
    EVERYONE,

    /**
     * Only participants who have provided a vote can reveal the deck.
     */
    PARTICIPANTS

}
