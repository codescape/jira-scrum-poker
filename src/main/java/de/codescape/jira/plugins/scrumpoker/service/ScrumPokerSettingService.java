package de.codescape.jira.plugins.scrumpoker.service;

import com.atlassian.activeobjects.tx.Transactional;
import de.codescape.jira.plugins.scrumpoker.model.GlobalSettings;

/**
 * Service to persist and retrieve Scrum poker settings from the database.
 */
@Transactional
public interface ScrumPokerSettingService {

    /**
     * Loads the global Scrum Poker settings from the database.
     *
     * @return global settings
     */
    GlobalSettings load();

    /**
     * Persists the global Scrum Poker settings into the database.
     *
     * @param globalSettings global settings
     */
    void persist(GlobalSettings globalSettings);

}
