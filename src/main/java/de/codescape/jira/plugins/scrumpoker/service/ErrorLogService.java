package de.codescape.jira.plugins.scrumpoker.service;

import com.atlassian.activeobjects.tx.Transactional;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerError;

import java.util.List;

/**
 * Service to persist and retrieve Scrum Poker errors from the database.
 */
@Transactional
public interface ErrorLogService {

    /**
     * Adds a new Scrum Poker error to the error log.
     *
     * @param errorMessage the message to be logged
     * @param throwable    the exception of the error
     */
    void logError(String errorMessage, Throwable throwable);

    /**
     * Adds a new Scrum Poker error to the error log.
     *
     * @param errorMessage the message to be logged
     */
    void logError(String errorMessage);

    /**
     * Returns a list of all logged Scrum Poker errors.
     *
     * @return list of Scrum Poker errors
     */
    List<ScrumPokerError> listAll();

    /**
     * Purges the list of Scrum Poker errors.
     */
    void emptyErrorLog();

}
