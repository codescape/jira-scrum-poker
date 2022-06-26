package de.codescape.jira.plugins.scrumpoker.model;

import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerProject;

/**
 * ProjectSettings represents the data transfer object for the {@link ScrumPokerProject} AO object which cannot be
 * directly rendered in velocity templates anymore since Jira 9.0.0.
 */
public class ProjectSettings {

    private final ProjectActivation activateScrumPoker;
    private final String cardSet;
    private final String estimateField;

    public ProjectSettings(ScrumPokerProject scrumPokerProject) {
        activateScrumPoker = scrumPokerProject.getActivateScrumPoker();
        cardSet = scrumPokerProject.getCardSet();
        estimateField = scrumPokerProject.getEstimateField();
    }

    public ProjectActivation getActivateScrumPoker() {
        return activateScrumPoker;
    }

    public String getCardSet() {
        return cardSet;
    }

    public String getEstimateField() {
        return estimateField;
    }

}
