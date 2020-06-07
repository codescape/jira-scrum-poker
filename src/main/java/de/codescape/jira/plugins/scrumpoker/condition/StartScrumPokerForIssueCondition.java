package de.codescape.jira.plugins.scrumpoker.condition;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.plugin.webfragment.conditions.AbstractIssueWebCondition;
import com.atlassian.jira.plugin.webfragment.model.JiraHelper;
import com.atlassian.jira.user.ApplicationUser;
import de.codescape.jira.plugins.scrumpoker.service.EstimateFieldService;
import de.codescape.jira.plugins.scrumpoker.service.ScrumPokerSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This condition is used to decide whether a button to start a Scrum Poker session should be displayed or not for the
 * given issue. The issue must be editable and have the custom field that is configured for Scrum Poker estimations.
 */
@Component
public class StartScrumPokerForIssueCondition extends AbstractIssueWebCondition {

    private final EstimateFieldService estimateFieldService;
    private final ScrumPokerSessionService scrumPokerSessionService;

    @Autowired
    public StartScrumPokerForIssueCondition(EstimateFieldService estimateFieldService,
                                            ScrumPokerSessionService scrumPokerSessionService) {
        this.estimateFieldService = estimateFieldService;
        this.scrumPokerSessionService = scrumPokerSessionService;
    }

    @Override
    public boolean shouldDisplay(ApplicationUser applicationUser, Issue issue, JiraHelper jiraHelper) {
        return isEstimable(issue) && !activeSessionExists(issue);
    }

    private boolean activeSessionExists(Issue issue) {
        return scrumPokerSessionService.hasActiveSession(issue.getKey());
    }

    /**
     * Returns whether a Scrum Poker session can be started for the given issue.
     */
    private boolean isEstimable(Issue issue) {
        return estimateFieldService.isEstimable(issue);
    }

}
