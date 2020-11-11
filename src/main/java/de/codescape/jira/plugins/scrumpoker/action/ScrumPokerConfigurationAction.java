package de.codescape.jira.plugins.scrumpoker.action;

import com.atlassian.jira.issue.fields.CustomField;
import de.codescape.jira.plugins.scrumpoker.model.AdditionalField;
import de.codescape.jira.plugins.scrumpoker.model.AllowRevealDeck;
import de.codescape.jira.plugins.scrumpoker.model.DisplayCommentsForIssue;
import de.codescape.jira.plugins.scrumpoker.model.GlobalSettings;
import de.codescape.jira.plugins.scrumpoker.service.AdditionalFieldService;
import de.codescape.jira.plugins.scrumpoker.service.EstimateFieldService;
import de.codescape.jira.plugins.scrumpoker.service.GlobalSettingsService;
import de.codescape.jira.plugins.scrumpoker.service.ScrumPokerLicenseService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Global configuration of the Scrum Poker plugin.
 */
public class ScrumPokerConfigurationAction extends AbstractScrumPokerAction {

    private static final long serialVersionUID = 1L;

    /**
     * Values of all actions used on the global configuration page.
     */
    static final class Actions {

        static final String SAVE = "save";
        static final String DEFAULTS = "defaults";

    }

    /**
     * Names of all parameters used on the global configuration page.
     */
    static final class Parameters {

        static final String ACTION = "action";
        static final String ESTIMATE_FIELD = "estimateField";
        static final String SESSION_TIMEOUT = "sessionTimeout";
        static final String ACTIVATE_SCRUM_POKER = "activateScrumPoker";
        static final String ALLOW_REVEAL_DECK = "allowRevealDeck";
        static final String DISPLAY_DROPDOWN_ON_BOARDS = "displayDropdownOnBoards";
        static final String CHECK_PERMISSION_TO_SAVE_ESTIMATE = "checkPermissionToSaveEstimate";
        static final String DISPLAY_COMMENTS_FOR_ISSUE = "displayCommentsForIssue";
        static final String CARD_SET = "cardSet";
        static final String DISPLAY_ADDITIONAL_FIELDS = "displayAdditionalFieldsIds";

    }

    private final EstimateFieldService estimateFieldService;
    private final GlobalSettingsService globalSettingsService;
    private final AdditionalFieldService additionalFieldService;
    private final ScrumPokerLicenseService scrumPokerLicenseService;

    @Autowired
    public ScrumPokerConfigurationAction(EstimateFieldService estimateFieldService,
                                         GlobalSettingsService globalSettingsService,
                                         AdditionalFieldService additionalFieldService,
                                         ScrumPokerLicenseService scrumPokerLicenseService) {
        this.estimateFieldService = estimateFieldService;
        this.globalSettingsService = globalSettingsService;
        this.additionalFieldService = additionalFieldService;
        this.scrumPokerLicenseService = scrumPokerLicenseService;
    }

    /**
     * List of all custom fields that are supported as an estimate field.
     */
    public List<CustomField> getEstimateFields() {
        return estimateFieldService.supportedCustomFields();
    }

    /**
     * List of all custom fields to be displayed as an additional field.
     */
    public List<AdditionalField> getAdditionalFields() {
        return additionalFieldService.supportedCustomFields();
    }

    /**
     * Return the currently set global settings.
     */
    public GlobalSettings getGlobalSettings() {
        return globalSettingsService.load();
    }

    /**
     * Returns the license error if it exists or <code>null</code> otherwise.
     */
    public String getLicenseError() {
        return scrumPokerLicenseService.getLicenseError();
    }

    /**
     * Save the global settings if the form is saved and submitted.
     */
    @Override
    protected String doExecute() {
        String action = getParameter(Parameters.ACTION);
        if (action != null) {
            switch (action) {
                case Actions.SAVE:
                    GlobalSettings globalSettings = new GlobalSettings();

                    String newEstimateField = getParameter(Parameters.ESTIMATE_FIELD);
                    globalSettings.setEstimateField(newEstimateField);

                    String newSessionTimeout = getParameter(Parameters.SESSION_TIMEOUT);
                    if (newSessionTimeout != null) {
                        globalSettings.setSessionTimeout(Integer.valueOf(newSessionTimeout));
                    }

                    String newActivateScrumPoker = getParameter(Parameters.ACTIVATE_SCRUM_POKER);
                    if (newActivateScrumPoker != null) {
                        globalSettings.setActivateScrumPoker(Boolean.parseBoolean(newActivateScrumPoker));
                    }

                    String newAllowRevealDeck = getParameter(Parameters.ALLOW_REVEAL_DECK);
                    if (newAllowRevealDeck != null) {
                        globalSettings.setAllowRevealDeck(AllowRevealDeck.valueOf(newAllowRevealDeck));
                    }

                    String displayDropdownOnBoards = getParameter(Parameters.DISPLAY_DROPDOWN_ON_BOARDS);
                    if (displayDropdownOnBoards != null) {
                        globalSettings.setDisplayDropdownOnBoards(Boolean.parseBoolean(displayDropdownOnBoards));
                    }

                    String checkPermissionToSaveEstimate = getParameter(Parameters.CHECK_PERMISSION_TO_SAVE_ESTIMATE);
                    if (checkPermissionToSaveEstimate != null) {
                        globalSettings.setCheckPermissionToSaveEstimate(Boolean.parseBoolean(checkPermissionToSaveEstimate));
                    }

                    String displayCommentsForIssue = getParameter(Parameters.DISPLAY_COMMENTS_FOR_ISSUE);
                    if (displayCommentsForIssue != null) {
                        globalSettings.setDisplayCommentsForIssue(DisplayCommentsForIssue.valueOf(displayCommentsForIssue));
                    }

                    String cardSet = getParameter(Parameters.CARD_SET);
                    if (cardSet != null) {
                        globalSettings.setCardSet(cardSet);
                    }

                    String displayAdditionalFields = getParameter(Parameters.DISPLAY_ADDITIONAL_FIELDS);
                    if (displayAdditionalFields != null) {
                        globalSettings.setAdditionalFields(displayAdditionalFields);
                    }

                    globalSettingsService.persist(globalSettings);
                    break;
                case Actions.DEFAULTS:
                    globalSettingsService.persist(new GlobalSettings());
                    break;
            }
        }

        return SUCCESS;
    }

}
