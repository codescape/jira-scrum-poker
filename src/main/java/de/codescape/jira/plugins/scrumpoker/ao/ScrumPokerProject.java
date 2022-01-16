package de.codescape.jira.plugins.scrumpoker.ao;

import de.codescape.jira.plugins.scrumpoker.model.ProjectActivation;
import net.java.ao.Preload;
import net.java.ao.schema.Indexed;
import net.java.ao.schema.NotNull;

/**
 * Active Object to persist project specific settings into the database.
 */
@Preload
public interface ScrumPokerProject extends ScrumPokerEntity {

    /**
     * Id of the Jira project that is configured here.
     */
    @NotNull
    @Indexed
    Long getProjectId();

    void setProjectId(Long projectId);

    /**
     * @deprecated replaced by {@link #getActivateScrumPoker()} since version 22.01.0
     */
    @Deprecated
    boolean isScrumPokerEnabled();

    /**
     * @deprecated replaced by {@link #setActivateScrumPoker(ProjectActivation)} since version 22.01.0
     */
    @Deprecated
    void setScrumPokerEnabled(boolean scrumPokerEnabled);

    /**
     * Scrum Poker can be explicitly activated and disabled or inherited from the global configuration.
     */
    ProjectActivation getActivateScrumPoker();

    void setActivateScrumPoker(ProjectActivation projectActivation);

    /**
     * Returns the estimate field for this project or <code>null</code> if derived from the global configuration.
     */
    String getEstimateField();

    void setEstimateField(String estimateField);

    /**
     * Returns the card set for this project or <code>null</code> if derived from the global configuration.
     */
    String getCardSet();

    void setCardSet(String cardSet);

}
