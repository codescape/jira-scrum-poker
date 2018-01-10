package net.congstar.jira.plugins.scrumpoker.model;

import org.apache.commons.lang3.math.NumberUtils;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A Scrum poker session is associated with exactly one issue. It holds all information required to run a poker session
 * and support the visual representation of the session in the graphical user interface.
 */
public class ScrumPokerSession {

    private final String issueKey;

    private final String userKey;

    private Map<String, String> cards = new HashMap<>();

    private boolean visible;

    private final DateTime startedOn;

    private String confirmedVote;

    public ScrumPokerSession(String issueKey, String userKey) {
        this.issueKey = issueKey;
        this.userKey = userKey;
        this.startedOn = DateTime.now();
    }

    /**
     * Returns the date and time this session is started.
     */
    public DateTime getStartedOn() {
        return startedOn;
    }

    /**
     * Returns the confirmed vote.
     */
    public String getConfirmedVote() {
        return confirmedVote;
    }

    /**
     * Returns the cards presented by users.
     */
    public Map<String, String> getCards() {
        return cards;
    }

    /**
     * Returns whether the cards are shown or hidden.
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Shows the cards if there is a minimum of one card.
     */
    public void revealDeck() {
        if (cards.size() > 0) {
            visible = true;
        }
    }

    /**
     * Updates the card for the given user and hides all cards.
     */
    public void updateCard(String userKey, String chosenCard) {
        cards.put(userKey, chosenCard);
        visible = false;
    }

    /**
     * Resets the session and removes all cards previously presented by users.
     */
    public void resetDeck() {
        cards = new HashMap<>();
        confirmedVote = null;
        visible = false;
    }

    /**
     * Returns the lowest vote.
     */
    public String getMinimumVote() {
        double min = 1000.0;
        for (String voted : cards.values()) {
            if (NumberUtils.isNumber(voted)) {
                min = Math.min(min, new BigDecimal(voted).doubleValue());
            }
        }
        return String.valueOf(min).replace(".0", "");
    }

    /**
     * Returns the highest vote.
     */
    public String getMaximumVote() {
        double max = 0;
        for (String voted : cards.values()) {
            if (NumberUtils.isNumber(voted)) {
                max = Math.max(max, new BigDecimal(voted).doubleValue());
            }
        }
        return String.valueOf(max).replace(".0", "");
    }

    /**
     * Returns all cards between the lowest and the highest vote.
     */
    public List<String> getBoundedVotes() {
        List<String> result = new ArrayList<>();

        String maximum = getMaximumVote();
        String minimum = getMinimumVote();

        boolean minimumReached = false;
        boolean maximumReached = false;

        for (ScrumPokerCard card : ScrumPokerCards.pokerDeck) {
            if (card.getName().equals(minimum)) {
                minimumReached = true;
            }

            if (minimumReached && !maximumReached) {
                result.add(card.getName());
            }

            if (card.getName().equals(maximum)) {
                maximumReached = true;
            }
        }

        return result;
    }

    /**
     * Saves the confirmed vote.
     */
    public void confirm(String finalVote) {
        this.confirmedVote = finalVote;
    }

    /**
     * Returns the key of the associated Jira issue.
     *
     * @return issue key
     */
    public String getIssueKey() {
        return issueKey;
    }

    /**
     * Returns the key of the creator of the Scrum poker session.
     *
     * @return user key
     */
    public String getUserKey() {
        return userKey;
    }

}
