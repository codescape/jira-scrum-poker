package de.codescape.jira.plugins.scrumpoker.model;

public enum ProjectActivation {

    /**
     * Inherit activation for the current project from the global settings.
     */
    INHERIT,

    /**
     * Scrum Poker is explicitly activated for the current project regardless of the global settings.
     */
    ACTIVATE,

    /**
     * Scrum Poker is explicitly disabled for the current project regardless of the global settings.
     */
    DISABLE;

}
