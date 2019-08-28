package de.codescape.jira.plugins.scrumpoker.ao;

import net.java.ao.schema.StringLength;

import java.util.Date;

/**
 * Active Object to persist Scrum Poker errors into the database.
 */
public interface ScrumPokerError extends ScrumPokerEntity {

    /**
     * The timestamp when the error occurred.
     */
    Date getErrorTimestamp();

    void setErrorTimestamp(Date errorTimestamp);

    /**
     * The version of the Scrum Poker add-on.
     */
    String getScrumPokerVersion();

    void setScrumPokerVersion(String scrumPokerVersion);

    /**
     * The version of the Jira instance.
     */
    String getJiraVersion();

    void setJiraVersion(String jiraVersion);

    /**
     * The message of the error.
     */
    String getErrorMessage();

    void setErrorMessage(String errorMessage);

    /**
     * The stacktrace of the error.
     */
    @StringLength(StringLength.UNLIMITED)
    String getStacktrace();

    void setStacktrace(String stacktrace);

}
