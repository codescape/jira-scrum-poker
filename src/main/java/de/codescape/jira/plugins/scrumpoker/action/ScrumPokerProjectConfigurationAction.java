package de.codescape.jira.plugins.scrumpoker.action;

import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.project.ProjectManager;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import de.codescape.jira.plugins.scrumpoker.ScrumPokerConstants;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerProject;
import de.codescape.jira.plugins.scrumpoker.service.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Configuration of a Jira project for Scrum Poker specific settings.
 */
public class ScrumPokerProjectConfigurationAction extends AbstractScrumPokerAction implements ProvidesDocumentationLink {

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
        static final String DEFAULTS = "defaults";

    }

    private final ProjectManager projectManager;
    private final EstimateFieldService estimateFieldService;
    private final ProjectSettingsService projectSettingsService;
    private final GlobalSettingsService globalSettingsService;
    private final ScrumPokerLicenseService scrumPokerLicenseService;

    private String projectKey;

    @Autowired
    public ScrumPokerProjectConfigurationAction(@ComponentImport ProjectManager projectManager,
                                                EstimateFieldService estimateFieldService,
                                                ProjectSettingsService projectSettingsService,
                                                GlobalSettingsService globalSettingsService,
                                                ErrorLogService errorLogService,
                                                ScrumPokerLicenseService scrumPokerLicenseService) {
        super(errorLogService);
        this.projectManager = projectManager;
        this.estimateFieldService = estimateFieldService;
        this.projectSettingsService = projectSettingsService;
        this.globalSettingsService = globalSettingsService;
        this.scrumPokerLicenseService = scrumPokerLicenseService;
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
     * Returns the license error if it exists or <code>null</code> otherwise.
     */
    public String getLicenseError() {
        return scrumPokerLicenseService.getLicenseError();
    }

    @Override
    protected String doExecute() {
        // make sure we have a project to configure specific Scrum Poker settings for
        projectKey = getParameter(Parameters.PROJECT_KEY);
        Project project = getProjectByKey(projectKey);
        if (project == null || project.getId() == null) {
            errorMessage("Unable to find project with key " + projectKey + " for project configuration.");
            return ERROR;
        }

        // action can be null (just show the page), save (persist form data) or defaults (reset settings)
        String action = getParameter(Parameters.ACTION);
        if (action != null) {
            switch (action) {
                case Actions.SAVE:
                    boolean activateScrumPoker = Boolean.parseBoolean(getParameter(Parameters.ACTIVATE_SCRUM_POKER));
                    String estimateField = nullOrValue(getParameter(Parameters.ESTIMATE_FIELD));
                    String cardSet = nullOrValue(getParameter(Parameters.CARD_SET));
                    projectSettingsService.persistSettings(project.getId(), activateScrumPoker, estimateField, cardSet);
                    break;
                case Actions.DEFAULTS:
                    projectSettingsService.removeSettings(project.getId());
                    break;
            }
        }
        return SUCCESS;
    }

    @Override
    public String getDocumentationUrl() {
        return ScrumPokerConstants.CONFIGURATION_DOCUMENTATION;
    }

    // Scrum Poker should not save empty strings for unused settings. Those settings are persisted as null.
    private String nullOrValue(String input) {
        return (input != null && !input.isEmpty()) ? input : null;
    }

    private Project getProjectByKey(String projectKey) {
        return projectManager.getProjectByCurrentKey(projectKey);
    }

}
