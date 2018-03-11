package net.congstar.jira.plugins.scrumpoker.model;

import org.apache.commons.lang3.math.NumberUtils;
import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    private Integer confirmedVote;

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
    public Integer getConfirmedVote() {
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
    public Integer getMinimumVote() {
        return numericValues().stream().reduce(Integer::min).orElse(0);
    }

    private List<Integer> numericValues() {
        return cards.values().stream()
            .filter(NumberUtils::isNumber)
            .map(Integer::valueOf)
            .collect(Collectors.toList());
    }

    /**
     * Returns the highest vote.
     */
    public Integer getMaximumVote() {
        return numericValues().stream().reduce(Integer::max).orElse(100);
    }

    /**
     * Returns all cards between the lowest and the highest vote.
     */
    public List<Integer> getBoundedVotes() {
        return ScrumPokerDeck.asList().stream()
            .filter(scrumPokerCard -> NumberUtils.isNumber(scrumPokerCard.getName()))
            .map(scrumPokerCard -> Integer.valueOf(scrumPokerCard.getName()))
            .filter(value -> value >= getMinimumVote() && value <= getMaximumVote())
            .collect(Collectors.toList());
    }

    /**
     * Saves the confirmed vote.
     */
    public void confirm(Integer finalVote) {
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
