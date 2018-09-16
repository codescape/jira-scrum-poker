package de.codescape.jira.plugins.scrumpoker.service;

import com.atlassian.activeobjects.tx.Transactional;

/**
 * Service to persist and retrieve Scrum poker settings from the database.
 */
@Transactional
public interface ScrumPokerSettingService {

    /**
     * Loads the currently saved story point field from the global settings.
     *
     * @return story point field
     */
    String loadStoryPointField();

    /**
     * Loads the currently saved session timeout from the global settings.
     *
     * @return session timeout in hours
     */
    Integer loadSessionTimeout();

    /**
     * Persist the global settings of the plugin.
     *
     * @param storyPointField story point field id
     * @param sessionTimeout  session timeout in hours
     */
    void persistSettings(String storyPointField, String sessionTimeout);

}
