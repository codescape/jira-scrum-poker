package net.congstar.jira.plugins.planningpoker.data;

import java.util.HashMap;
import java.util.Map;

/**
 * Simple implementation using a Map to store the data.
 */
public class DefaultPlanningPokerStorage implements PlanningPokerStorage {

    private Map<String, IssuePlanningPokerData> storage;

    public DefaultPlanningPokerStorage() {
        storage = new HashMap<String, IssuePlanningPokerData>();
    }

    @Override
    public void update(String issueKey, String userKey, String chosenCard) {
        IssuePlanningPokerData issuePlanningPokerData = issueByIssueKey(issueKey);
        issuePlanningPokerData.updateCard(userKey, chosenCard);
        issuePlanningPokerData.hideDeck();
    }

    @Override
    public Map<String, String> chosenCardsForIssue(String issueKey) {
        return issueByIssueKey(issueKey).getCards();
    }

    @Override
    public boolean isVisible(String issueKey) {
        return issueByIssueKey(issueKey).isVisible();
    }

    @Override
    public void revealDeck(String issueKey) {
        /* check if anybody voted to avoid race conditions when revealing
           (one person resets, another one reveals shortly after) */
        if (issueByIssueKey(issueKey).getCards().size() > 0) {
            issueByIssueKey(issueKey).revealDeck();
        }
    }

    @Override
    public void resetDeck(String issueKey) {
        issueByIssueKey(issueKey).resetDeck();
    }

    private IssuePlanningPokerData issueByIssueKey(String issueKey) {
        IssuePlanningPokerData issuePlanningPokerData = storage.get(issueKey);
        if (issuePlanningPokerData == null) {
            issuePlanningPokerData = new IssuePlanningPokerData();
            storage.put(issueKey, issuePlanningPokerData);
        }
        return issuePlanningPokerData;
    }

    private static class IssuePlanningPokerData {

        private Map<String, String> cards = new HashMap<String, String>();

        private boolean visible;

        public Map<String, String> getCards() {
            return cards;
        }

        public boolean isVisible() {
            return visible;
        }

        public void hideDeck() {
            visible = false;
        }

        public void revealDeck() {
            visible = true;
        }

        public void updateCard(String userKey, String chosenCard) {
            cards.put(userKey, chosenCard);
        }

        public void resetDeck() {
            cards = new HashMap<String, String>();
            visible = false;
        }

    }

}
