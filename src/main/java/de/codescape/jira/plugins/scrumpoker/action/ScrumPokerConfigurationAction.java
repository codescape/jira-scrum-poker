package de.codescape.jira.plugins.scrumpoker.action;

import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.security.request.RequestMethod;
import com.atlassian.jira.security.request.SupportedMethods;
import com.atlassian.jira.security.xsrf.RequiresXsrfCheck;
import de.codescape.jira.plugins.scrumpoker.ScrumPokerConstants;
import de.codescape.jira.plugins.scrumpoker.model.AdditionalField;
import de.codescape.jira.plugins.scrumpoker.model.AllowRevealDeck;
import de.codescape.jira.plugins.scrumpoker.model.DisplayCommentsForIssue;
import de.codescape.jira.plugins.scrumpoker.model.GlobalSettings;
import de.codescape.jira.plugins.scrumpoker.service.*;
import jakarta.inject.Inject;

import java.util.List;

/**
 * Global configuration of the Scrum Poker plugin.
 */
public class ScrumPokerConfigurationAction extends AbstractScrumPokerAction implements ProvidesDocumentationLink {

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

    @Inject
    public ScrumPokerConfigurationAction(EstimateFieldService estimateFieldService,
                                         GlobalSettingsService globalSettingsService,
                                         AdditionalFieldService additionalFieldService,
                                         ScrumPokerLicenseService scrumPokerLicenseService,
                                         ErrorLogService errorLogService) {
        super(errorLogService);
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
     * Show the configuration page.
     */
    @Override
    @SupportedMethods({RequestMethod.GET})
    public String doDefault() {
        return SUCCESS;
    }

    /**
     * Save the global settings if the form is saved and submitted.
     */
    @Override
    @RequiresXsrfCheck
    @SupportedMethods({RequestMethod.POST})
    protected String doExecute() {
        String action = getParameter(Parameters.ACTION);
        if (action != null) {
            switch (action) {
                case Actions.SAVE:
                    saveGlobalSettings();
                    break;
                case Actions.DEFAULTS:
                    saveDefaultSettings();
                    break;
            }
        }
        return SUCCESS;
    }

    private void saveGlobalSettings() {
        GlobalSettings globalSettings = new GlobalSettings();

        // estimate field (required - always provided)
        String estimateField = getParameter(Parameters.ESTIMATE_FIELD);
        globalSettings.setEstimateField(estimateField);

        // session timeout (override only if value provided)
        String sessionTimeout = getParameter(Parameters.SESSION_TIMEOUT);
        if (sessionTimeout != null) {
            globalSettings.setSessionTimeout(Integer.valueOf(sessionTimeout));
        }

        // activate Scrum Poker (always override default)
        String activateScrumPoker = getParameter(Parameters.ACTIVATE_SCRUM_POKER);
        globalSettings.setActivateScrumPoker(Boolean.parseBoolean(activateScrumPoker));

        // allow reveal deck (override only if value provided)
        String allowRevealDeck = getParameter(Parameters.ALLOW_REVEAL_DECK);
        if (allowRevealDeck != null) {
            globalSettings.setAllowRevealDeck(AllowRevealDeck.valueOf(allowRevealDeck));
        }

        // display dropdown on boards (always override default)
        String displayDropdownOnBoards = getParameter(Parameters.DISPLAY_DROPDOWN_ON_BOARDS);
        globalSettings.setDisplayDropdownOnBoards(Boolean.parseBoolean(displayDropdownOnBoards));

        // check permission to save estimate (always override default)
        String checkPermissionToSaveEstimate = getParameter(Parameters.CHECK_PERMISSION_TO_SAVE_ESTIMATE);
        globalSettings.setCheckPermissionToSaveEstimate(Boolean.parseBoolean(checkPermissionToSaveEstimate));

        // display comments for issue (override only if value provided)
        String displayCommentsForIssue = getParameter(Parameters.DISPLAY_COMMENTS_FOR_ISSUE);
        if (displayCommentsForIssue != null) {
            globalSettings.setDisplayCommentsForIssue(DisplayCommentsForIssue.valueOf(displayCommentsForIssue));
        }

        // card set (required - always provided)
        String cardSet = getParameter(Parameters.CARD_SET);
        globalSettings.setCardSet(cardSet);

        // display additional fields (persist null instead of empty string)
        String displayAdditionalFields = getParameter(Parameters.DISPLAY_ADDITIONAL_FIELDS);
        if (displayAdditionalFields != null && displayAdditionalFields.isEmpty()) {
            displayAdditionalFields = null;
        }
        globalSettings.setAdditionalFields(displayAdditionalFields);

        globalSettingsService.persist(globalSettings);
    }

    private void saveDefaultSettings() {
        globalSettingsService.persist(new GlobalSettings());
    }

    @Override
    public String getDocumentationUrl() {
        return ScrumPokerConstants.CONFIGURATION_DOCUMENTATION;
    }

}
