package de.codescape.jira.plugins.scrumpoker;

/**
 * Globally used constants that are reused among Scrum Poker classes go into this class.
 */
public final class ScrumPokerConstants {

    /**
     * Plugin key that identifies the Scrum Poker for Jira plugin.
     */
    public static final String SCRUM_POKER_PLUGIN_KEY = "de.codescape.jira.plugins.scrum-poker";

    /**
     * URL to documentation of the Health Check page.
     */
    public static final String HEALTH_CHECK_DOCUMENTATION = "https://jira-scrum-poker.codescape.de/health-check";

    /**
     * URL to documentation of the Error Log page.
     */
    public static final String ERROR_LOG_DOCUMENTATION = "https://jira-scrum-poker.codescape.de/error-log";

    /**
     * URL to documentation of the Configuration page.
     */
    public static final String CONFIGURATION_DOCUMENTATION = "https://jira-scrum-poker.codescape.de/configuration";

    /**
     * URL to documentation of Getting Started page.
     */
    public static final String GETTING_STARTED_DOCUMENTATION = "https://jira-scrum-poker.codescape.de";

    // Prevent utility class from being instantiated
    private ScrumPokerConstants() {
        throw new AssertionError("ScrumPokerConstants must not be instantiated.");
    }

}
