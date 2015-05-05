package net.congstar.jira.plugins.scrumpoker.data;

import java.util.Map;

public interface PlanningPokerStorage {

    /**
     * Update the card for the given issue and user.
     * 
     * @param issueKey
     * @param userKey
     * @param chosenCard
     */
    void update(String issueKey, String userKey, String chosenCard);

    /**
     * Return the cards for the given issue.
     * 
     * @param issueKey
     * @return
     */
    Map<String, String> chosenCardsForIssue(String issueKey);

    /**
     * Return the visibility of the cards for the given issue.
     * 
     * @param issueKey
     * @return
     */
    boolean isVisible(String issueKey);

    /**
     * Show the cards for the given issue.
     * 
     * @param issueKey
     */
    void revealDeck(String issueKey);

    /**
     * Discard all cards for the given issue key.
     * 
     * @param issueKey
     */
    void resetDeck(String issueKey);

}
