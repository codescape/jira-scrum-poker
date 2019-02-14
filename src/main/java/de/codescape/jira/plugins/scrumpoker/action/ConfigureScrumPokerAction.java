package de.codescape.jira.plugins.scrumpoker.action;

import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import de.codescape.jira.plugins.scrumpoker.model.AllowRevealDeck;
import de.codescape.jira.plugins.scrumpoker.service.ScrumPokerSettingService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Global configuration of the Scrum Poker plugin.
 */
public class ConfigureScrumPokerAction extends AbstractScrumPokerAction {

    private static final long serialVersionUID = 1L;

    /**
     * Names of all parameters used on the global configuration page.
     */
    static final class Parameters {

        static final String ACTION = "action";
        static final String STORY_POINT_FIELD = "storyPointField";
        static final String SESSION_TIMEOUT = "sessionTimeout";
        static final String DEFAULT_PROJECT_ACTIVATION = "defaultProjectActivation";
        static final String ALLOW_REVEAL_DECK = "allowRevealDeck";

    }

    private final CustomFieldManager customFieldManager;
    private final ScrumPokerSettingService scrumPokerSettingService;

    @Autowired
    public ConfigureScrumPokerAction(ScrumPokerSettingService scrumPokerSettingService,
                                     @ComponentImport CustomFieldManager customFieldManager) {
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

    /**
     * Current configured option how is allowed to reveal a deck.
     */
    public String getAllowRevealDeck() {
        return scrumPokerSettingService.loadAllowRevealDeck().name();
    }

    @Override
    protected String doExecute() {
        String action = getParameter(Parameters.ACTION);
        if (action != null && action.equals("save")) {
            String newStoryPointField = getParameter(Parameters.STORY_POINT_FIELD);
            scrumPokerSettingService.persistStoryPointField(newStoryPointField);

            String newSessionTimeout = getParameter(Parameters.SESSION_TIMEOUT);
            scrumPokerSettingService.persistSessionTimeout(Integer.valueOf(newSessionTimeout));

            String newDefaultProjectActivation = getParameter(Parameters.DEFAULT_PROJECT_ACTIVATION);
            scrumPokerSettingService.persistDefaultProjectActivation(Boolean.valueOf(newDefaultProjectActivation));

            String newAllowRevealDeck = getParameter(Parameters.ALLOW_REVEAL_DECK);
            scrumPokerSettingService.persistAllowRevealDeck(AllowRevealDeck.valueOf(newAllowRevealDeck));
        }
        return SUCCESS;
    }

}
