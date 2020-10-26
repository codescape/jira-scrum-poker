package de.codescape.jira.plugins.scrumpoker.service;

import com.atlassian.activeobjects.tx.Transactional;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerProject;

import java.util.List;

/**
 * Service to persist and retrieve project related configuration from the database.
 */
@Transactional
public interface ProjectSettingsService {

    /**
     * Saves the new Scrum Poker settings for a specific Jira project.
     *
     * @param projectId          unique ID of the Jira project
     * @param activateScrumPoker flag whether Scrum Poker is enabled for the Jira project
     * @param estimateField      estimate field for the Jira project
     * @param cardSet            card set to be used for this Jira project
     */
    void persistSettings(Long projectId, boolean activateScrumPoker, String estimateField, String cardSet);

    /**
     * Returns the project specific Scrum Poker settings for the given Jira project or <code>null</code> if no project
     * specific settings exist.
     *
     * @param projectId unitque ID of the Jira project
     * @return project specific settings or <code>null</code> if no project specific settings exist
     */
    ScrumPokerProject loadSettings(Long projectId);

    /**
     * Removes the project specific Scrum Poker settings for the given Jira project.
     *
     * @param projectId unique ID of the Jira project
     */
    // TODO add tests to remove project specific settings
    void removeSettings(Long projectId);

    /**
     * Returns all project specific Scrum Poker settings.
     *
     * @return project specific settings for all projects
     */
    List<ScrumPokerProject> loadAll();

}
