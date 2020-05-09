package de.codescape.jira.plugins.scrumpoker.service;

import com.atlassian.activeobjects.tx.Transactional;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerSession;

import java.util.List;

/**
 * Service to persist and retrieve Scrum Poker sessions and votes from the database.
 */
@Transactional
public interface ScrumPokerSessionService {

    /**
     * Returns the list of recent Scrum Poker session.
     */
    List<ScrumPokerSession> recent();

    /**
     * Returns a Scrum Poker session for the given key.
     *
     * @param issueKey key of the issue
     * @param userKey  key of the user
     * @return Scrum Poker session
     */
    ScrumPokerSession byIssueKey(String issueKey, String userKey);

    /**
     * Adds a vote to the Scrum Poker session for the given user.
     *
     * @param issueKey key of the issue
     * @param userKey  key of the user
     * @param vote     vote of the user
     * @return Scrum Poker session
     */
    ScrumPokerSession addVote(String issueKey, String userKey, String vote);

    /**
     * Reveals the Scrum Poker session and makes the votes visible to the users.
     *
     * @param issueKey key of the issue
     * @param userKey  key of the user
     * @return Scrum Poker session
     */
    ScrumPokerSession reveal(String issueKey, String userKey);

    /**
     * Confirms an estimate for the Scrum Poker session.
     *
     * @param issueKey key of the issue
     * @param userKey  key of the user
     * @param estimate confirmed estimate
     * @return Scrum Poker session
     */
    ScrumPokerSession confirm(String issueKey, String userKey, String estimate);

    /**
     * Resets a Scrum Poker session and allows a new round.
     *
     * @param issueKey key of the issue
     * @param userKey  key of the user
     * @return Scrum Poker session
     */
    ScrumPokerSession reset(String issueKey, String userKey);

    /**
     * Cancels a Scrum Poker session.
     *
     * @param issueKey key of the issue
     * @param userKey  key of the user
     * @return Scrum Poker session
     */
    ScrumPokerSession cancel(String issueKey, String userKey);

    /**
     * Returns a list of reference sessions with the same estimate to display for the given user.
     *
     * @param userKey  key of the user
     * @param estimate estimate
     * @return List of Scrum Poker sessions
     */
    List<ScrumPokerSession> references(String userKey, String estimate);

}
