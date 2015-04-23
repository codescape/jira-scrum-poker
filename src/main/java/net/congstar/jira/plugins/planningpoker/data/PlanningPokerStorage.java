package net.congstar.jira.plugins.planningpoker.data;

import java.util.Map;

public interface PlanningPokerStorage {

    void update(String issueKey, String userKey, String chosenCard);

    Map<String, String> chosenCardsForIssue(String issueKey);

}
