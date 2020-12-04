package de.codescape.jira.plugins.scrumpoker.action;

/**
 * Interface for Scrum Poker actions that provide a documentation link to be displayed to the user.
 */
public interface ProvidesDocumentationLink {

    /**
     * Returns the URL to the documentation for that feature or page.
     */
    String getDocumentationUrl();

}
