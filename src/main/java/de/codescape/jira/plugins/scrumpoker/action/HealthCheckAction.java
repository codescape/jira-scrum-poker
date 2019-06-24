package de.codescape.jira.plugins.scrumpoker.action;

import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.upm.api.license.PluginLicenseManager;
import de.codescape.jira.plugins.scrumpoker.service.EstimationFieldService;
import de.codescape.jira.plugins.scrumpoker.service.ScrumPokerSettingService;

import java.util.ArrayList;
import java.util.List;

/**
 * Health Check page for the Scrum Poker app.
 */
public class HealthCheckAction extends AbstractScrumPokerAction {

    private static final long serialVersionUID = 1L;

    /**
     * Names of all parameters used on the global configuration page.
     */
    static final class Parameters {

        static final String ACTION = "action";
        static final String SCAN_LICENSE = "scanLicense";
        static final String SCAN_CONFIGURATION = "scanConfiguration";
        static final String SCAN_ERRORS = "scanErrors";

    }

    private final ScrumPokerSettingService scrumPokerSettingService;
    private final EstimationFieldService estimationFieldService;
    private final PluginLicenseManager pluginLicenseManager;

    public HealthCheckAction(ScrumPokerSettingService scrumPokerSettingService,
                             EstimationFieldService estimationFieldService,
                             @ComponentImport PluginLicenseManager pluginLicenseManager) {
        this.scrumPokerSettingService = scrumPokerSettingService;
        this.estimationFieldService = estimationFieldService;
        this.pluginLicenseManager = pluginLicenseManager;
    }

    @Override
    protected String doExecute() {
        return "success";
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
            results.add("scrumpoker.healthcheck.results.errors.nolicensefound");
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
        String storyPointField = scrumPokerSettingService.load().getStoryPointField();
        if (storyPointField == null || storyPointField.isEmpty()) {
            results.add("scrumpoker.healthcheck.results.errors.estimationfieldnotset");
        }

        // check that the confirmed estimation custom field can be found
        CustomField customField = estimationFieldService.findStoryPointField();
        if (customField == null) {
            results.add("scrumpoker.healthcheck.results.errors.customfieldnotfound");
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
        // TODO: add check for emptiness of the error log once #69 is implemented
        // results.add("scrumpoker.healthcheck.results.errors.errorlognotempty");
        return results;
    }

}
