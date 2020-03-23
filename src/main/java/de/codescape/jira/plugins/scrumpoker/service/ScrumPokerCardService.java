package de.codescape.jira.plugins.scrumpoker.service;

import com.atlassian.activeobjects.tx.Transactional;

import java.util.List;

/**
 * Service to persist and retrieve card sets from the database.
 */
@Transactional
public interface ScrumPokerCardService {

    /**
     * Returns the default card set.
     */
    List<String> getCardSet();

}
