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
     * Returns whether a specific project has Scrum Poker explicitly activated.
     *
     * @param projectId Unique ID of the Jira project
     * @return <code>true</code> if project has Scrum Poker activated, otherwise <code>false</code>
     */
    boolean loadActivateScrumPoker(Long projectId);

    /**
     * Saves whether a specific project has Scrum Poker explicitly activated.
     *
     * @param projectId          Unique ID of the Jira project
     * @param activateScrumPoker project has Scrum Poker activated
     */
    void persistActivateScrumPoker(Long projectId, boolean activateScrumPoker);

    /**
     * Returns all project specific settings.
     *
     * @return settings for all projects
     */
    List<ScrumPokerProject> loadAll();

}
