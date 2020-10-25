package de.codescape.jira.plugins.scrumpoker.action;

import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.project.ProjectManager;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerProject;
import de.codescape.jira.plugins.scrumpoker.service.ErrorLogService;
import de.codescape.jira.plugins.scrumpoker.service.EstimateFieldService;
import de.codescape.jira.plugins.scrumpoker.service.GlobalSettingsService;
import de.codescape.jira.plugins.scrumpoker.service.ProjectSettingsService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Configuration of a Jira project for Scrum Poker specific settings.
 */
public class ScrumPokerProjectConfigurationAction extends AbstractScrumPokerAction {

    private static final long serialVersionUID = 1L;

    /**
     * Names of all parameters used on the project configuration page.
     */
    static final class Parameters {

        static final String ACTION = "action";
        static final String PROJECT_KEY = "projectKey";
        static final String ACTIVATE_SCRUM_POKER = "activateScrumPoker";
        static final String ESTIMATE_FIELD = "estimateField";
        static final String CARD_SET = "cardSet";

    }

    /**
     * Values of all actions used on the project configuration page.
     */
    static final class Actions {

        static final String SAVE = "save";

    }

    private final ProjectManager projectManager;
    private final EstimateFieldService estimateFieldService;
    private final ProjectSettingsService projectSettingsService;
    private final GlobalSettingsService globalSettingsService;
    private final ErrorLogService errorLogService;

    private String projectKey;

    @Autowired
    public ScrumPokerProjectConfigurationAction(@ComponentImport ProjectManager projectManager,
                                                EstimateFieldService estimateFieldService,
                                                ProjectSettingsService projectSettingsService,
                                                GlobalSettingsService globalSettingsService,
                                                ErrorLogService errorLogService) {
        this.projectManager = projectManager;
        this.estimateFieldService = estimateFieldService;
        this.projectSettingsService = projectSettingsService;
        this.globalSettingsService = globalSettingsService;
        this.errorLogService = errorLogService;
    }

    /**
     * List of all custom fields that are supported as an estimate field.
     */
    public List<CustomField> getEstimateFields() {
        return estimateFieldService.supportedCustomFields();
    }

    /**
     * Key of the project to configure Scrum Poker for.
     */
    public String getProjectKey() {
        return projectKey;
    }

    /**
     * Returns the settings for the given project.
     */
    public ScrumPokerProject getProjectSettings() {
        return projectSettingsService.loadSettings(getProjectByKey(projectKey).getId());
    }

    /**
     * Return whether Scrum Poker is activated globally.
     */
    public boolean isActivateScrumPokerGlobally() {
        return globalSettingsService.load().isActivateScrumPoker();
    }

    /**
     * Save the project specific settings if the form is saved and submitted.
     */
    @Override
    protected String doExecute() {
        projectKey = getParameter(Parameters.PROJECT_KEY);
        Long projectId = getProjectByKey(projectKey).getId();
        if (projectId == null) {
            errorMessage("Unable to find project with key " + projectKey + " for project configuration.");
            return ERROR;
        }

        String action = getParameter(Parameters.ACTION);
        if (action != null && action.equals(Actions.SAVE)) {
            boolean activateScrumPoker = Boolean.parseBoolean(getParameter(Parameters.ACTIVATE_SCRUM_POKER));
            String estimateField = getParameter(Parameters.ESTIMATE_FIELD);
            if (estimateField != null && estimateField.isEmpty()) {
                estimateField = null;
            }
            String cardSet = getParameter(Parameters.CARD_SET);
            if (cardSet != null && cardSet.isEmpty()) {
                cardSet = null;
            }
            projectSettingsService.persistSettings(projectId, activateScrumPoker, estimateField, cardSet);
        }
        return SUCCESS;
    }

    private void errorMessage(String errorMessage) {
        errorLogService.logError(errorMessage);
        addErrorMessage(errorMessage);
    }

    private Project getProjectByKey(String projectKey) {
        return projectManager.getProjectByCurrentKey(projectKey);
    }

}
