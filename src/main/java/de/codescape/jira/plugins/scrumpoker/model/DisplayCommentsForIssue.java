package de.codescape.jira.plugins.scrumpoker.model;

/**
 * This enumeration represents the configuration whether and how many comments to be displayed.
 */
public enum DisplayCommentsForIssue {

    /**
     * Display all comments for the issue.
     */
    ALL,

    /**
     * Display no comments for the issue.
     */
    NONE,

    /**
     * Display only the latest 10 comments for the issue.
     */
    LATEST;

    /**
     * Return whether this setting should result in displaying comments or not.
     */
    public boolean shouldDisplay() {
        return this != DisplayCommentsForIssue.NONE;
    }

}
