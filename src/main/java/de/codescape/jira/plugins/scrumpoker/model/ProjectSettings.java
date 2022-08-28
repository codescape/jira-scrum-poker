package de.codescape.jira.plugins.scrumpoker.model;

import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerProject;

import java.util.Objects;

/**
 * ProjectSettings represents the data transfer object for the {@link ScrumPokerProject} AO object which cannot be
 * directly rendered in velocity templates anymore since Jira 9.0.0.
 */
public class ProjectSettings {

    private final ProjectActivation activateScrumPoker;
    private final String cardSet;
    private final String estimateField;

    /**
     * Constructor for any project without any custom settings.
     */
    public ProjectSettings() {
        activateScrumPoker = ProjectActivation.INHERIT;
        cardSet = null;
        estimateField = null;
    }

    /**
     * Constructor for any project with custom settings set.
     *
     * @param scrumPokerProject project specific settings
     */
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjectSettings that = (ProjectSettings) o;
        return activateScrumPoker == that.activateScrumPoker && Objects.equals(cardSet, that.cardSet) && Objects.equals(estimateField, that.estimateField);
    }

    @Override
    public int hashCode() {
        return Objects.hash(activateScrumPoker, cardSet, estimateField);
    }

}
