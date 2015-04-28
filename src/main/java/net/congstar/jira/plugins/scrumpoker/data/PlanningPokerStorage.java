package net.congstar.jira.plugins.scrumpoker.data;

import java.util.Map;

public interface PlanningPokerStorage {

    void update(String issueKey, String userKey, String chosenCard);

    Map<String, String> chosenCardsForIssue(String issueKey);

    boolean isVisible(String issueKey);

    void revealDeck(String issueKey);

    void resetDeck(String issueKey);

}
