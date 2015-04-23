package net.congstar.jira.plugins.planningpoker.action;

import java.util.HashMap;
import java.util.Map;

public class PlanningPokerCardStorage {

    private static Map<String, Map<String, String>> storage = new HashMap<String, Map<String, String>>();

    public static void update(String issueKey, String userKey, String chosenCard) {
        Map<String, String> chosenCardsForIssue = chosenCardsForIssue(issueKey);
        chosenCardsForIssue.put(userKey, chosenCard);
        debugOutput();
    }

    public static Map<String, String> chosenCardsForIssue(String issueKey) {
        Map<String, String> chosenCardsForIssue = storage.get(issueKey);
        if (chosenCardsForIssue == null) {
            chosenCardsForIssue = new HashMap<String, String>();
            storage.put(issueKey, chosenCardsForIssue);
        }
        return chosenCardsForIssue;
    }

    private static void debugOutput() {
        for (String issue : storage.keySet()) {
            System.out.println("########## Issue " + issue);
            Map<String, String> issueCards = storage.get(issue);
            for (String user : issueCards.keySet()) {
                System.out.println(" >> User " + user + " chose card " + issueCards.get(user));
            }
        }
    }

}
