package de.codescape.jira.plugins.scrumpoker.action;

import com.atlassian.jira.project.Project;
import de.codescape.jira.plugins.scrumpoker.service.ProjectSettingService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Configuration of a Jira project for Scrum Poker specific settings.
 */
public class ConfigureProjectSettingsAction extends AbstractScrumPokerAction {

    private static final long serialVersionUID = 1L;

    /**
     * Names of all parameters used on the project configuration page.
     */
    static final class Parameters {

        static final String ACTION = "action";
        static final String PROJECT_KEY = "projectKey";
        static final String SCRUM_POKER_ENABLED = "scrumPokerEnabled";

    }

    private final ProjectSettingService projectSettingService;

    private String projectKey;

    @Autowired
    public ConfigureProjectSettingsAction(ProjectSettingService projectSettingService) {
        this.projectSettingService = projectSettingService;
    }

    /**
     * Key of the project to configure Scrum Poker for.
     */
    public String getProjectKey() {
        return projectKey;
    }

    /**
     * Current configured project activation for this concrete project.
     */
    public boolean isScrumPokerEnabled() {
        return projectSettingService.loadScrumPokerEnabled(getProjectByKey(projectKey).getId());
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
            String newScrumPokerEnabled = getParameter(Parameters.SCRUM_POKER_ENABLED);
            projectSettingService.persistScrumPokerEnabled(projectId, Boolean.parseBoolean(newScrumPokerEnabled));
        }
        return SUCCESS;
    }

    private Project getProjectByKey(String projectKey) {
        return getProjectManager().getProjectByCurrentKey(projectKey);
    }

}
