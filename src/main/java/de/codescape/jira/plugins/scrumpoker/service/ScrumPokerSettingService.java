package de.codescape.jira.plugins.scrumpoker.service;

import com.atlassian.activeobjects.tx.Transactional;
import de.codescape.jira.plugins.scrumpoker.model.AllowRevealDeck;

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
    void persistSessionTimeout(Integer sessionTimeout);

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

    /**
     * Loads the setting who is allowed to reveal a session.
     *
     * @return who is allowed to reveal a session
     */
    AllowRevealDeck loadAllowRevealDeck();

    /**
     * Persist the setting who is allowed to reveal a session.
     *
     * @param allowRevealDeck who is allowed to reveal a sesion
     */
    void persistAllowRevealDeck(AllowRevealDeck allowRevealDeck);

}
