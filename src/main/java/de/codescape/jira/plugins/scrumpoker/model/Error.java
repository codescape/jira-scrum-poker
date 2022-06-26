package de.codescape.jira.plugins.scrumpoker.model;

import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerError;

import java.util.Date;

/**
 * An Error represents the data transfer object for the {@link ScrumPokerError} AO object which cannot be directly
 * rendered in velocity templates anymore since Jira 9.0.0.
 */
public class Error {

    private final Date errorTimestamp;
    private final String scrumPokerVersion;
    private final String jiraVersion;
    private final String stacktrace;
    private final String errorMessage;

    public Error(ScrumPokerError scrumPokerError) {
        this.errorTimestamp = scrumPokerError.getErrorTimestamp();
        this.scrumPokerVersion = scrumPokerError.getScrumPokerVersion();
        this.jiraVersion = scrumPokerError.getJiraVersion();
        this.stacktrace = scrumPokerError.getStacktrace();
        this.errorMessage = scrumPokerError.getErrorMessage();
    }

    public Date getErrorTimestamp() {
        return errorTimestamp;
    }

    public String getScrumPokerVersion() {
        return scrumPokerVersion;
    }

    public String getJiraVersion() {
        return jiraVersion;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getStacktrace() {
        return stacktrace;
    }
}
