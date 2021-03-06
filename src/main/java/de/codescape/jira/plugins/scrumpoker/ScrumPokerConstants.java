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

    /**
     * URL to official Scrum Poker Service Desk.
     */
    public static final String SERVICE_DESK_URL = "https://codescape.atlassian.net/servicedesk";

    /**
     * URL to the Marketplace entry for Scrum Poker for Jira.
     */
    public static final String MARKETPLACE_URL = "https://marketplace.atlassian.com/apps/1218884/scrum-poker-for-jira";

    // Prevent utility class from being instantiated
    private ScrumPokerConstants() {
        throw new AssertionError("ScrumPokerConstants must not be instantiated.");
    }

}
