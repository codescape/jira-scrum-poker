package de.codescape.jira.plugins.scrumpoker.persistence;

import de.codescape.jira.plugins.scrumpoker.model.ScrumPokerSession;

import java.util.List;

/**
 * Component to access Scrum poker sessions.
 */
public interface ScrumPokerStorage {

    /**
     * Return the Scrum poker session for the given issue.
     *
     * @param issueKey Key of the issue
     * @param userKey  Key of the current user
     * @return Scrum poker session
     */
    ScrumPokerSession sessionForIssue(String issueKey, String userKey);

    /**
     * List of Scrum poker sessions without confirmed estimation.
     *
     * @return list of Scrum poker sessions
     */
    List<ScrumPokerSession> getOpenSessions();

    /**
     * List of Scrum poker sessions with confirmed estimation.
     *
     * @return list of Scrum poker sessions
     */
    List<ScrumPokerSession> getClosedSessions();

}
