package de.codescape.jira.plugins.scrumpoker.ao;

/**
 * Active Object to persist card sets into the database.
 */
public interface ScrumPokerCards extends ScrumPokerEntity {

    /**
     * Comma separated list of card values to be used as the card set.
     */
    String getCardSet();

    void setCardSet(String cardSet);

}
