package de.codescape.jira.plugins.scrumpoker.action;

import com.atlassian.jira.project.Project;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import de.codescape.jira.plugins.scrumpoker.service.ProjectSettingService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Configuration of a Jira project for Scrum Poker specific settings.
 */
public class ConfigureProjectSettingsAction extends JiraWebActionSupport {

    private static final long serialVersionUID = 1L;
    private static final String PARAM_ACTION = "action";
    private static final String PARAM_PROJECT_KEY = "projectKey";
    private static final String PARAM_SCRUM_POKER_ENABLED = "scrumPokerEnabled";

    private final ProjectSettingService projectSettingService;

    private String projectKey;

    @Autowired
    public ConfigureProjectSettingsAction(ProjectSettingService projectSettingService) {
        this.projectSettingService = projectSettingService;
    }

    public String getProjectKey() {
        return projectKey;
    }

    /**
     * Current configured project activation for this concrete project.
     */
    public boolean isScrumPokerEnabled() {
        return projectSettingService.loadScrumPokerEnabled(getProjectByKey(projectKey).getId());
    }

    @Override
    protected String doExecute() {
        projectKey = getHttpRequest().getParameter(PARAM_PROJECT_KEY);
        String action = getHttpRequest().getParameter(PARAM_ACTION);
        if (action != null && action.equals("save")) {
            Long projectId = getProjectByKey(projectKey).getId();
            String newScrumPokerEnabled = getHttpRequest().getParameter(PARAM_SCRUM_POKER_ENABLED);
            projectSettingService.persistScrumPokerEnabled(projectId, Boolean.valueOf(newScrumPokerEnabled));
        }
        return "success";
    }

    private Project getProjectByKey(String projectKey) {
        return getProjectManager().getProjectByCurrentKey(projectKey);
    }

}
