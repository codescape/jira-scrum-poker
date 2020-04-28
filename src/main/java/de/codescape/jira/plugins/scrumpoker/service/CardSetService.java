package de.codescape.jira.plugins.scrumpoker.service;

import com.atlassian.activeobjects.tx.Transactional;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerSession;
import de.codescape.jira.plugins.scrumpoker.model.Card;

import java.util.List;

/**
 * Service to persist and retrieve card sets from the database.
 */
@Transactional
public interface CardSetService {

    /**
     * Returns the default card set.
     *
     * @return default card set
     */
    List<Card> getCardSet();

    /**
     * Returns the card set used by this Scrum Poker session.
     *
     * @param scrumPokerSession Scrum Poker session
     * @return card set
     */
    List<Card> getCardSet(ScrumPokerSession scrumPokerSession);

}
