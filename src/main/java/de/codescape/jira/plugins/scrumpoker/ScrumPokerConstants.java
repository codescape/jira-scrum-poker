package de.codescape.jira.plugins.scrumpoker;

/**
 * Globally used constants that are reused among Scrum Poker classes go into this class.
 */
public final class ScrumPokerConstants {

    /**
     * Plugin key that identifies the Scrum Poker for Jira plugin.
     */
    public static final String SCRUM_POKER_PLUGIN_KEY = "de.codescape.jira.plugins.scrum-poker";

    private ScrumPokerConstants() {
        throw new IllegalStateException("ScrumPokerConstants should never be instantiated.");
    }

}
