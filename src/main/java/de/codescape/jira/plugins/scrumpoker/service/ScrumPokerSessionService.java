package de.codescape.jira.plugins.scrumpoker.service;

import com.atlassian.activeobjects.tx.Transactional;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerSession;

import java.util.List;

/**
 * Active Object persistence service to persist and retrieve Scrum poker sessions and votes from the database.
 */
@Transactional
public interface ScrumPokerSessionService {

    /**
     * Returns the list of recent Scrum poker session.
     */
    List<ScrumPokerSession> recent();

    /**
     * Returns a Scrum poker session for the given key.
     *
     * @param issueKey key of the issue
     * @param userKey  key of the user
     * @return Scrum poker session
     */
    ScrumPokerSession byIssueKey(String issueKey, String userKey);

    /**
     * Adds a vote to the Scrum poker session for the given user.
     *
     * @param issueKey key of the issue
     * @param userKey  key of the user
     * @param vote     vote of the user
     * @return Scrum poker session
     */
    ScrumPokerSession addVote(String issueKey, String userKey, String vote);

    /**
     * Reveals the Scrum poker session and makes the votes visible to the users.
     *
     * @param issueKey key of the issue
     * @param userKey  key of the user
     * @return Scrum poker session
     */
    ScrumPokerSession reveal(String issueKey, String userKey);

    /**
     * Confirms an estimation for the Scrum poker session.
     *
     * @param issueKey   key of the issue
     * @param userKey    key of the user
     * @param estimation confirmed estimation
     * @return Scrum poker session
     */
    ScrumPokerSession confirm(String issueKey, String userKey, Integer estimation);

    /**
     * Resets a Scrum poker session and allows a new round.
     *
     * @param issueKey key of the issue
     * @param userKey  key of the user
     * @return Scrum poker session
     */
    ScrumPokerSession reset(String issueKey, String userKey);

    /**
     * Cancels a Scrum poker session.
     *
     * @param issueKey key of the issue
     * @param userKey  key of the user
     * @return Scrum poker session
     */
    ScrumPokerSession cancel(String issueKey, String userKey);

    /**
     * Returns a list of reference sessions with the same estimation to display for the given user.
     *
     * @param userKey    key of the user
     * @param estimation estimation
     * @return List of Scrum poker sessions
     */
    List<ScrumPokerSession> references(String userKey, Integer estimation);

}
