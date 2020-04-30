package de.codescape.jira.plugins.scrumpoker.action;

import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.upm.api.license.PluginLicenseManager;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerProject;
import de.codescape.jira.plugins.scrumpoker.model.Card;
import de.codescape.jira.plugins.scrumpoker.service.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Health Check page for the Scrum Poker app.
 */
public class HealthCheckAction extends AbstractScrumPokerAction {

    private static final long serialVersionUID = 1L;

    /**
     * Potential license error codes.
     */
    static final class License {

        static final String LICENSE_INVALID = "scrumpoker.healthcheck.results.errors.licenseinvalid";
        static final String NO_LICENSE_FOUND = "scrumpoker.healthcheck.results.errors.nolicensefound";

    }

    /**
     * Potential configuration error codes.
     */
    static final class Configuration {

        static final String ESTIMATION_FIELD_NOT_FOUND = "scrumpoker.healthcheck.results.errors.estimationfieldnotfound";
        static final String ESTIMATION_FIELD_NOT_SET = "scrumpoker.healthcheck.results.errors.estimationfieldnotset";
        static final String ENABLED_FOR_NO_PROJECT = "scrumpoker.healthcheck.results.errors.enabledfornoproject";
        static final String CARD_SET_WITHOUT_OPTIONS = "scrumpoker.healthcheck.results.errors.cardsetwithoutoptions";

    }

    /**
     * Potential error log error codes.
     */
    static final class ErrorLog {

        static final String ERROR_LOG_NOT_EMPTY = "scrumpoker.healthcheck.results.errors.errorlognotempty";

    }

    /**
     * Names of all parameters used on the global configuration page.
     */
    static final class Parameters {

        static final String ACTION = "action";
        static final String SCAN_LICENSE = "scanLicense";
        static final String SCAN_CONFIGURATION = "scanConfiguration";
        static final String SCAN_ERRORS = "scanErrors";

    }

    private final GlobalSettingsService globalSettingsService;
    private final EstimateFieldService estimateFieldService;
    private final PluginLicenseManager pluginLicenseManager;
    private final ProjectSettingsService projectSettingsService;
    private final ErrorLogService errorLogService;
    private final CardSetService cardSetService;

    @Autowired
    public HealthCheckAction(@ComponentImport PluginLicenseManager pluginLicenseManager,
                             GlobalSettingsService globalSettingsService,
                             EstimateFieldService estimateFieldService,
                             ProjectSettingsService projectSettingsService,
                             ErrorLogService errorLogService,
                             CardSetService cardSetService) {
        this.pluginLicenseManager = pluginLicenseManager;
        this.globalSettingsService = globalSettingsService;
        this.estimateFieldService = estimateFieldService;
        this.projectSettingsService = projectSettingsService;
        this.errorLogService = errorLogService;
        this.cardSetService = cardSetService;
    }

    @Override
    protected String doExecute() {
        return SUCCESS;
    }

    /**
     * Indicator to show the results part of this page.
     */
    public boolean showResults() {
        String action = getParameter(Parameters.ACTION);
        return action != null && action.equals("start");
    }

    /**
     * Indicator to show the license checks.
     */
    public boolean showLicense() {
        String showLicense = getParameter(Parameters.SCAN_LICENSE);
        return showLicense != null && showLicense.equals("true");
    }

    /**
     * Return the list of negative results on the license checks.
     */
    public List<String> getLicenseResults() {
        List<String> results = new ArrayList<>();

        // check that a license is defined
        if (!pluginLicenseManager.getLicense().isDefined()) {
            results.add(License.NO_LICENSE_FOUND);
        } else {
            // check that the license is valid
            if (!pluginLicenseManager.getLicense().get().isValid()) {
                results.add(License.LICENSE_INVALID);
            }
        }

        return results;
    }

    /**
     * Indicator to show the configuration checks.
     */
    public boolean showConfiguration() {
        String showConfiguration = getParameter(Parameters.SCAN_CONFIGURATION);
        return showConfiguration != null && showConfiguration.equals("true");
    }

    /**
     * Return the list of negative results on the configuration checks.
     */
    public List<String> getConfigurationResults() {
        List<String> results = new ArrayList<>();

        // check that the confirmed estimation field is set
        String storyPointField = globalSettingsService.load().getStoryPointField();
        if (storyPointField == null || storyPointField.isEmpty()) {
            results.add(Configuration.ESTIMATION_FIELD_NOT_SET);
        } else {
            // check that the confirmed estimation custom field can be found
            CustomField customField = estimateFieldService.findEstimateField();
            if (customField == null) {
                results.add(Configuration.ESTIMATION_FIELD_NOT_FOUND);
            }
        }

        // check that Scrum Poker is either globally enabled or has projects explicitly enabled
        if (!globalSettingsService.load().isDefaultProjectActivation() &&
            projectSettingsService.loadAll().stream().noneMatch(ScrumPokerProject::isScrumPokerEnabled)) {
            results.add(Configuration.ENABLED_FOR_NO_PROJECT);
        }

        // check that a card set is defined that can be parsed into different cards
        List<Card> cardSet = cardSetService.getCardSet().stream()
            .filter(Card::isAssignable)
            .collect(Collectors.toList());
        if (cardSet.isEmpty() || cardSet.size() == 1) {
            results.add(Configuration.CARD_SET_WITHOUT_OPTIONS);
        }

        return results;
    }

    /**
     * Indicator to show the error log checks.
     */
    public boolean showErrors() {
        String showErrors = getParameter(Parameters.SCAN_ERRORS);
        return showErrors != null && showErrors.equals("true");
    }

    /**
     * Return the list of negative results on the error log checks.
     */
    public List<String> getErrorsResults() {
        List<String> results = new ArrayList<>();

        // check that the Scrum Poker error log is empty
        if (!errorLogService.listAll().isEmpty()) {
            results.add(ErrorLog.ERROR_LOG_NOT_EMPTY);
        }

        return results;
    }

}
