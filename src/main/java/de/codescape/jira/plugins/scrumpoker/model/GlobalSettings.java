package de.codescape.jira.plugins.scrumpoker.model;

/**
 * Global Settings for Scrum Poker to be used across all Jira projects.
 */
public class GlobalSettings {

    // defaults

    public static final AllowRevealDeck ALLOW_REVEAL_DECK_DEFAULT = AllowRevealDeck.EVERYONE;
    public static final boolean DEFAULT_PROJECT_ACTIVATION_DEFAULT = true;
    public static final Integer SESSION_TIMEOUT_DEFAULT = 12;

    // settings

    private String storyPointField;
    private Integer sessionTimeout = SESSION_TIMEOUT_DEFAULT;
    private AllowRevealDeck allowRevealDeck = ALLOW_REVEAL_DECK_DEFAULT;
    private boolean defaultProjectActivation = DEFAULT_PROJECT_ACTIVATION_DEFAULT;

    public void setStoryPointField(String storyPointField) {
        this.storyPointField = storyPointField;
    }

    public String getStoryPointField() {
        return storyPointField;
    }

    public void setSessionTimeout(Integer sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    public Integer getSessionTimeout() {
        return sessionTimeout;
    }

    public void setAllowRevealDeck(AllowRevealDeck allowRevealDeck) {
        this.allowRevealDeck = allowRevealDeck;
    }

    public AllowRevealDeck getAllowRevealDeck() {
        return allowRevealDeck;
    }

    public void setDefaultProjectActivation(boolean defaultProjectActivation) {
        this.defaultProjectActivation = defaultProjectActivation;
    }

    public boolean getDefaultProjectActivation() {
        return defaultProjectActivation;
    }

}
