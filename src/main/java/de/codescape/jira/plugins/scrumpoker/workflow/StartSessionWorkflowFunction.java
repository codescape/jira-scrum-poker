package de.codescape.jira.plugins.scrumpoker.workflow;

import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.workflow.function.issue.AbstractJiraFunctionProvider;
import com.opensymphony.module.propertyset.PropertySet;
import de.codescape.jira.plugins.scrumpoker.service.ErrorLogService;
import de.codescape.jira.plugins.scrumpoker.service.EstimateFieldService;
import de.codescape.jira.plugins.scrumpoker.service.ScrumPokerLicenseService;
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
    private final ScrumPokerLicenseService scrumPokerLicenseService;
    private final EstimateFieldService estimateFieldService;
    private final ErrorLogService errorLogService;

    @Autowired
    public StartSessionWorkflowFunction(ScrumPokerSessionService scrumPokerSessionService,
                                        ScrumPokerLicenseService scrumPokerLicenseService,
                                        EstimateFieldService estimateFieldService,
                                        ErrorLogService errorLogService) {
        this.scrumPokerSessionService = scrumPokerSessionService;
        this.scrumPokerLicenseService = scrumPokerLicenseService;
        this.estimateFieldService = estimateFieldService;
        this.errorLogService = errorLogService;
    }

    @Override
    public void execute(Map transientVars, Map args, PropertySet propertySet) {
        // check license
        if (!scrumPokerLicenseService.hasValidLicense()) {
            errorLogService.logError("Unable to start Scrum Poker session because of license errors: "
                + scrumPokerLicenseService.getLicenseError());
            return;
        }

        // check issue and sessions
        MutableIssue issue = getIssue(transientVars);
        if (!estimateFieldService.isEstimable(issue)) {
            log.info("Unable to start Scrum Poker session for issue {} because it is not estimable.", issue.getKey());
            return;
        }
        if (scrumPokerSessionService.hasActiveSession(issue.getKey())) {
            log.info("Unable to start Scrum Poker session for issue {} because it is already started.", issue.getKey());
            return;
        }

        // start session
        scrumPokerSessionService.byIssueKey(issue.getKey(), getCallerKey(transientVars, args));
        log.debug("Scrum Poker session successfully started for issue {} on workflow transition.", issue.getKey());
    }

}
