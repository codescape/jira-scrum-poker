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
     * Persist the story point field into the global settings.
     *
     * @param storyPointField story point field
     */
    void persistStoryPointField(String storyPointField);

    /**
     * Loads the currently saved session timeout from the global settings.
     *
     * @return session timeout in hours
     */
    Integer loadSessionTimeout();

    /**
     * Persist the session timeout into the global settings.
     *
     * @param sessionTimeout session timehout in hours
     */
    void persistSessionTimehout(Integer sessionTimeout);

    /**
     * Loads the currently saved default project activation from the global settings.
     *
     * @return default project activation
     */
    boolean loadDefaultProjectActivation();

    /**
     * Persist the defaultproject activation into the global settings.
     *
     * @param defaultProjectActivation default project activation
     */
    void persistDefaultProjectActivation(boolean defaultProjectActivation);

}
