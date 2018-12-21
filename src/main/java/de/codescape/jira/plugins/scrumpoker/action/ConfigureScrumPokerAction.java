package de.codescape.jira.plugins.scrumpoker.action;

import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import de.codescape.jira.plugins.scrumpoker.service.ScrumPokerSettingService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Configuration of the Scrum poker plugin.
 */
public class ConfigureScrumPokerAction extends JiraWebActionSupport {

    private static final long serialVersionUID = 1L;
    private static final String PARAM_STORY_POINT_FIELD = "storyPointField";
    private static final String PARAM_SESSION_TIMEOUT = "sessionTimeout";
    private static final String PARAM_DEFAULT_PROJECT_ACTIVATION = "defaultProjectActivation";
    private static final String PARAM_ACTION = "action";

    private final CustomFieldManager customFieldManager;
    private final ScrumPokerSettingService scrumPokerSettingService;

    @Autowired
    public ConfigureScrumPokerAction(ScrumPokerSettingService scrumPokerSettingService,
                                     CustomFieldManager customFieldManager) {
        this.scrumPokerSettingService = scrumPokerSettingService;
        this.customFieldManager = customFieldManager;
    }

    /**
     * List of all custom fields currently available in this Jira instance.
     */
    public List<CustomField> getCustomFields() {
        return customFieldManager.getCustomFieldObjects();
    }

    /**
     * Current configured id of the story point field in this Jira instance.
     */
    public String getStoryPointFieldId() {
        return scrumPokerSettingService.loadStoryPointField();
    }

    /**
     * Current configured session timeout in this Jira instance.
     */
    public String getSessionTimeout() {
        return scrumPokerSettingService.loadSessionTimeout().toString();
    }

    /**
     * Current configured default project activation in this Jira instance.
     */
    public String getDefaultProjectActivation() {
        return String.valueOf(scrumPokerSettingService.loadDefaultProjectActivation());
    }

    @Override
    protected String doExecute() {
        String action = getHttpRequest().getParameter(PARAM_ACTION);
        if (action != null && action.equals("save")) {
            String newStoryPointField = getHttpRequest().getParameter(PARAM_STORY_POINT_FIELD);
            scrumPokerSettingService.persistStoryPointField(newStoryPointField);

            String newSessionTimeout = getHttpRequest().getParameter(PARAM_SESSION_TIMEOUT);
            scrumPokerSettingService.persistSessionTimehout(Integer.valueOf(newSessionTimeout));

            String newDefaultProjectActivation = getHttpRequest().getParameter(PARAM_DEFAULT_PROJECT_ACTIVATION);
            scrumPokerSettingService.persistDefaultProjectActivation(Boolean.valueOf(newDefaultProjectActivation));
        }
        return "success";
    }

}
