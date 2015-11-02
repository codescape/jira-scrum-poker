package net.congstar.jira.plugins.scrumpoker.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.congstar.jira.plugins.scrumpoker.action.PokerUtil;

import org.apache.commons.lang3.math.NumberUtils;

/**
 * A {@link ScrumPokerSession} represents a Scrum poker session that is associated with exactly one issue. It holds all information required to run a poker session and support the
 * visual representation in the graphical user interface.
 */
public class ScrumPokerSession {

    private Map<String, String> cards = new HashMap<String, String>();

    private boolean visible;

    /**
     * Returns the cards presented by users.
     * 
     * @return
     */
    public Map<String, String> getCards() {
        return cards;
    }

    /**
     * Returns whether the cards are shown or hidden.
     * 
     * @return
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
     * 
     * @param userKey
     * @param chosenCard
     */
    public void updateCard(String userKey, String chosenCard) {
        cards.put(userKey, chosenCard);
        visible = false;
    }

    /**
     * Resets the session and removes all cards previously presented by users.
     */
    public void resetDeck() {
        cards = new HashMap<String, String>();
        visible = false;
    }

    public String getMinimumVote() {
        double min = 1000.0;
        for (String voted : cards.values()) {
            if (NumberUtils.isNumber(voted)) {
                min = Math.min(min, new BigDecimal(voted).doubleValue());
            }
        }
        return String.valueOf(min).replace(".0", "");
    }

    public String getMaximumVote() {
        double max = 0;
        for (String voted : cards.values()) {
            if (NumberUtils.isNumber(voted)) {
                max = Math.max(max, new BigDecimal(voted).doubleValue());
            }
        }
        return String.valueOf(max).replace(".0", "");
    }

    public List<String> getBoundedVotes() {
        List<String> result = new ArrayList<String>();

        String maximum = getMaximumVote();
        String minimum = getMinimumVote();

        boolean minimumReached = false;
        boolean maximumReached = false;

        for (PokerCard card : PokerUtil.pokerDeck) {
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

}
