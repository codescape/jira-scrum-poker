package de.codescape.jira.plugins.scrumpoker.model;

import static de.codescape.jira.plugins.scrumpoker.model.Card.COFFEE_BREAK;
import static de.codescape.jira.plugins.scrumpoker.model.Card.QUESTION_MARK;

/**
 * Global Settings for Scrum Poker to be used across all Jira projects.
 */
public class GlobalSettings {

    // defaults

    public static final Integer SESSION_TIMEOUT_DEFAULT = 12;
    public static final AllowRevealDeck ALLOW_REVEAL_DECK_DEFAULT = AllowRevealDeck.EVERYONE;
    public static final boolean ACTIVATE_SCRUM_POKER_DEFAULT = true;
    public static final boolean DISPLAY_DROPDOWN_ON_BOARDS_DEFAULT = false;
    public static final boolean CHECK_PERMISSION_TO_SAVE_ESTIMATE_DEFAULT = false;
    public static final DisplayCommentsForIssue DISPLAY_COMMENTS_FOR_ISSUE_DEFAULT = DisplayCommentsForIssue.LATEST;
    public static final String CARD_SET_DEFAULT = QUESTION_MARK.getValue() + ", " + COFFEE_BREAK.getValue() +
        ", 0, 1, 2, 3, 5, 8, 13, 20, 40, 100";

    // settings

    private String estimateField;
    private Integer sessionTimeout = SESSION_TIMEOUT_DEFAULT;
    private AllowRevealDeck allowRevealDeck = ALLOW_REVEAL_DECK_DEFAULT;
    private boolean activateScrumPoker = ACTIVATE_SCRUM_POKER_DEFAULT;
    private boolean displayDropdownOnBoards = DISPLAY_DROPDOWN_ON_BOARDS_DEFAULT;
    private boolean checkPermissionToSaveEstimate = CHECK_PERMISSION_TO_SAVE_ESTIMATE_DEFAULT;
    private DisplayCommentsForIssue displayCommentsForIssue = DISPLAY_COMMENTS_FOR_ISSUE_DEFAULT;
    private String cardSet = CARD_SET_DEFAULT;

    /**
     * Set the estimate field.
     */
    public void setEstimateField(String estimateField) {
        this.estimateField = estimateField;
    }

    /**
     * Return the estimate field.
     */
    public String getEstimateField() {
        return estimateField;
    }

    /**
     * Set the session timeout.
     */
    public void setSessionTimeout(Integer sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    /**
     * Return the session timeout.
     */
    public Integer getSessionTimeout() {
        return sessionTimeout;
    }

    /**
     * Set who is allowed to reveal the deck.
     */
    public void setAllowRevealDeck(AllowRevealDeck allowRevealDeck) {
        this.allowRevealDeck = allowRevealDeck;
    }

    /**
     * Return who is allowed to reveal the deck.
     */
    public AllowRevealDeck getAllowRevealDeck() {
        return allowRevealDeck;
    }

    /**
     * Set whether Scrum Poker is globally enabled.
     */
    public void setActivateScrumPoker(boolean activateScrumPoker) {
        this.activateScrumPoker = activateScrumPoker;
    }

    /**
     * Return whether Scrum Poker is globally enabled.
     */
    public boolean isActivateScrumPoker() {
        return activateScrumPoker;
    }

    /**
     * Set whether the dropdown on boards shall be displayed.
     */
    public void setDisplayDropdownOnBoards(boolean displayDropdownOnBoards) {
        this.displayDropdownOnBoards = displayDropdownOnBoards;
    }

    /**
     * Return whether the dropdown on boards shall be displayed.
     */
    public boolean isDisplayDropdownOnBoards() {
        return displayDropdownOnBoards;
    }

    /**
     * Set whether to check edit permission before persisting the estimation value.
     */
    public void setCheckPermissionToSaveEstimate(boolean checkPermissionToSaveEstimate) {
        this.checkPermissionToSaveEstimate = checkPermissionToSaveEstimate;
    }

    /**
     * Return whether to check edit permission before persisting the estimation value.
     */
    public boolean isCheckPermissionToSaveEstimate() {
        return checkPermissionToSaveEstimate;
    }

    /**
     * Set whether and how many comments shall be displayed for an issue.
     */
    public void setDisplayCommentsForIssue(DisplayCommentsForIssue displayCommentsForIssue) {
        this.displayCommentsForIssue = displayCommentsForIssue;
    }

    /**
     * Return whether and how many comments shall be displayed for an issue.
     */
    public DisplayCommentsForIssue getDisplayCommentsForIssue() {
        return displayCommentsForIssue;
    }

    /**
     * Set the globally defined card set.
     */
    public void setCardSet(String cardSet) {
        this.cardSet = cardSet;
    }

    /**
     * Return the globally defined card set.
     */
    public String getCardSet() {
        return cardSet;
    }

}
