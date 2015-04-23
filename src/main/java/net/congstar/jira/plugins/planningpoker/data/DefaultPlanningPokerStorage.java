package net.congstar.jira.plugins.planningpoker.data;

import java.util.HashMap;
import java.util.Map;

/**
 * Simple implementation using a Map to store the data.
 */
public class DefaultPlanningPokerStorage implements PlanningPokerStorage {

    private static Map<String, Map<String, String>> storage = new HashMap<String, Map<String, String>>();

    public DefaultPlanningPokerStorage() {
    }

    @Override
    public void update(String issueKey, String userKey, String chosenCard) {
        Map<String, String> chosenCardsForIssue = chosenCardsForIssue(issueKey);
        chosenCardsForIssue.put(userKey, chosenCard);
        debugOutput();
    }

    @Override
    public Map<String, String> chosenCardsForIssue(String issueKey) {
        Map<String, String> chosenCardsForIssue = storage.get(issueKey);
        if (chosenCardsForIssue == null) {
            chosenCardsForIssue = new HashMap<String, String>();
            storage.put(issueKey, chosenCardsForIssue);
        }
        return chosenCardsForIssue;
    }

    private void debugOutput() {
        for (String issue : storage.keySet()) {
            System.out.println("########## Issue " + issue);
            Map<String, String> issueCards = storage.get(issue);
            for (String user : issueCards.keySet()) {
                System.out.println(" >> User " + user + " chose card " + issueCards.get(user));
            }
        }
    }

}
