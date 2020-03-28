package de.codescape.jira.plugins.scrumpoker.action;

import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import de.codescape.jira.plugins.scrumpoker.model.AllowRevealDeck;
import de.codescape.jira.plugins.scrumpoker.model.DisplayCommentsForIssue;
import de.codescape.jira.plugins.scrumpoker.model.GlobalSettings;
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
        static final String DISPLAY_DROPDOWN_ON_BOARDS = "displayDropdownOnBoards";
        static final String CHECK_PERMISSION_TO_SAVE_ESTIMATE = "checkPermissionToSaveEstimate";
        static final String DISPLAY_COMMENTS_FOR_ISSUE = "displayCommentsForIssue";
        static final String CARD_SET = "cardSet";

    }

    private final CustomFieldManager customFieldManager;
    private final ScrumPokerSettingService scrumPokerSettingService;

    @Autowired
    public ConfigureScrumPokerAction(@ComponentImport CustomFieldManager customFieldManager,
                                     ScrumPokerSettingService scrumPokerSettingService) {
        this.customFieldManager = customFieldManager;
        this.scrumPokerSettingService = scrumPokerSettingService;
    }

    /**
     * List of all custom fields currently available in this Jira instance.
     */
    public List<CustomField> getCustomFields() {
        return customFieldManager.getCustomFieldObjects();
    }

    /**
     * Return the currently set global settings.
     */
    public GlobalSettings getGlobalSettings() {
        return scrumPokerSettingService.load();
    }

    /**
     * Save the global settings if the form is saved and submitted.
     */
    @Override
    protected String doExecute() {
        String action = getParameter(Parameters.ACTION);
        if (action != null) {
            if (action.equals("save")) {
                GlobalSettings globalSettings = new GlobalSettings();

                String newStoryPointField = getParameter(Parameters.STORY_POINT_FIELD);
                globalSettings.setStoryPointField(newStoryPointField);

                String newSessionTimeout = getParameter(Parameters.SESSION_TIMEOUT);
                globalSettings.setSessionTimeout(Integer.valueOf(newSessionTimeout));

                String newDefaultProjectActivation = getParameter(Parameters.DEFAULT_PROJECT_ACTIVATION);
                globalSettings.setDefaultProjectActivation(Boolean.parseBoolean(newDefaultProjectActivation));

                String newAllowRevealDeck = getParameter(Parameters.ALLOW_REVEAL_DECK);
                globalSettings.setAllowRevealDeck(AllowRevealDeck.valueOf(newAllowRevealDeck));

                String displayDropdownOnBoards = getParameter(Parameters.DISPLAY_DROPDOWN_ON_BOARDS);
                globalSettings.setDisplayDropdownOnBoards(Boolean.parseBoolean(displayDropdownOnBoards));

                String checkPermissionToSaveEstimate = getParameter(Parameters.CHECK_PERMISSION_TO_SAVE_ESTIMATE);
                globalSettings.setCheckPermissionToSaveEstimate(Boolean.parseBoolean(checkPermissionToSaveEstimate));

                String displayCommentsForIssue = getParameter(Parameters.DISPLAY_COMMENTS_FOR_ISSUE);
                globalSettings.setDisplayCommentsForIssue(DisplayCommentsForIssue.valueOf(displayCommentsForIssue));

                String cardSet = getParameter(Parameters.CARD_SET);
                globalSettings.setCardSet(cardSet);

                scrumPokerSettingService.persist(globalSettings);
            } else if (action.equals("defaults")) {
                scrumPokerSettingService.persist(new GlobalSettings());
            }
        }
        return SUCCESS;
    }

}
