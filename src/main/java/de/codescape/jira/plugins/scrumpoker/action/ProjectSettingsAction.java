package de.codescape.jira.plugins.scrumpoker.action;

import com.atlassian.jira.project.Project;
import com.atlassian.jira.project.ProjectManager;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import de.codescape.jira.plugins.scrumpoker.service.GlobalSettingsService;
import de.codescape.jira.plugins.scrumpoker.service.ProjectSettingsService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Configuration of a Jira project for Scrum Poker specific settings.
 */
public class ProjectSettingsAction extends AbstractScrumPokerAction {

    private static final long serialVersionUID = 1L;

    /**
     * Names of all parameters used on the project configuration page.
     */
    static final class Parameters {

        static final String ACTION = "action";
        static final String PROJECT_KEY = "projectKey";
        static final String ACTIVATE_SCRUM_POKER = "activateScrumPoker";

    }

    private final ProjectManager projectManager;
    private final ProjectSettingsService projectSettingsService;
    private final GlobalSettingsService globalSettingsService;

    private String projectKey;

    @Autowired
    public ProjectSettingsAction(@ComponentImport ProjectManager projectManager,
                                 ProjectSettingsService projectSettingsService,
                                 GlobalSettingsService globalSettingsService) {
        this.projectManager = projectManager;
        this.projectSettingsService = projectSettingsService;
        this.globalSettingsService = globalSettingsService;
    }

    /**
     * Key of the project to configure Scrum Poker for.
     */
    public String getProjectKey() {
        return projectKey;
    }

    /**
     * Return whether Scrum Poker is activated for the current project.
     */
    public boolean isActivateScrumPokerForProject() {
        return projectSettingsService.loadActivateScrumPoker(getProjectByKey(projectKey).getId());
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
        String action = getParameter(Parameters.ACTION);
        if (action != null && action.equals("save")) {
            Long projectId = getProjectByKey(projectKey).getId();
            String newActivateScrumPoker = getParameter(Parameters.ACTIVATE_SCRUM_POKER);
            projectSettingsService.persistActivateScrumPoker(projectId, Boolean.parseBoolean(newActivateScrumPoker));
        }
        return SUCCESS;
    }

    private Project getProjectByKey(String projectKey) {
        return projectManager.getProjectByCurrentKey(projectKey);
    }

}
