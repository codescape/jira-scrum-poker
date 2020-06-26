package de.codescape.jira.plugins.scrumpoker.workflow;

import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.workflow.function.issue.AbstractJiraFunctionProvider;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.upm.api.license.PluginLicenseManager;
import com.atlassian.upm.api.license.entity.PluginLicense;
import com.opensymphony.module.propertyset.PropertySet;
import de.codescape.jira.plugins.scrumpoker.service.ErrorLogService;
import de.codescape.jira.plugins.scrumpoker.service.EstimateFieldService;
import de.codescape.jira.plugins.scrumpoker.service.ScrumPokerSessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Workflow function that allows to start a Scrum Poker session on the workflow transition for an estimable issue.
 */
public class StartSessionWorkflowFunction extends AbstractJiraFunctionProvider {

    private static final Logger log = LoggerFactory.getLogger(StartSessionWorkflowFunction.class);

    private final ScrumPokerSessionService scrumPokerSessionService;
    private final EstimateFieldService estimateFieldService;
    private final PluginLicenseManager pluginLicenseManager;
    private final ErrorLogService errorLogService;

    @Autowired
    public StartSessionWorkflowFunction(@ComponentImport PluginLicenseManager pluginLicenseManager,
                                        ScrumPokerSessionService scrumPokerSessionService,
                                        EstimateFieldService estimateFieldService,
                                        ErrorLogService errorLogService) {
        this.pluginLicenseManager = pluginLicenseManager;
        this.scrumPokerSessionService = scrumPokerSessionService;
        this.estimateFieldService = estimateFieldService;
        this.errorLogService = errorLogService;
    }

    @Override
    public void execute(Map transientVars, Map args, PropertySet propertySet) {
        // license check
        if (pluginLicenseManager.getLicense().isDefined()) {
            PluginLicense license = pluginLicenseManager.getLicense().get();
            if (license.getError().isDefined()) {
                errorLogService.logError("Unable to start Scrum Poker session because of license errors: "
                    + license.getError().get().name());
                return;
            }
        } else {
            errorLogService.logError("Unable to start Scrum Poker session because of missing license.");
            return;
        }

        MutableIssue issue = getIssue(transientVars);
        if (!estimateFieldService.isEstimable(issue)) {
            log.info("Unable to start Scrum Poker session for issue {} because it is not estimable.", issue.getKey());
            return;
        }
        if (scrumPokerSessionService.hasActiveSession(issue.getKey())) {
            log.info("Unable to start Scrum Poker session for issue {} because it is already started.", issue.getKey());
            return;
        }
        scrumPokerSessionService.byIssueKey(issue.getKey(), getCallerKey(transientVars, args));
        log.debug("Scrum Poker session successfully started for issue {} on workflow transition.", issue.getKey());
    }

}
