package de.codescape.jira.plugins.scrumpoker.service;

import com.atlassian.activeobjects.tx.Transactional;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerProject;

import java.util.List;

/**
 * Service to persist and retrieve project related configuration from the database.
 */
@Transactional
public interface ProjectSettingService {

    /**
     * Returns whether a specific project has Scrum Poker explicitly enabled.
     *
     * @param projectId Unique ID of the Jira project
     * @return project has Scrum Poker enabled
     */
    boolean loadScrumPokerEnabled(Long projectId);

    /**
     * Saves whether a specific project has Scrum Poker explicitly enabled.
     *
     * @param projectId         Unique ID of the Jira project
     * @param scrumPokerEnabled project has Scrum Poker enabled
     */
    void persistScrumPokerEnabled(Long projectId, boolean scrumPokerEnabled);

    /**
     * Returns all project specific settings.
     *
     * @return settings for all projects
     */
    List<ScrumPokerProject> loadAll();

}
