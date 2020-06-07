package de.codescape.jira.plugins.scrumpoker.condition;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.plugin.webfragment.conditions.AbstractIssueWebCondition;
import com.atlassian.jira.plugin.webfragment.model.JiraHelper;
import com.atlassian.jira.user.ApplicationUser;
import de.codescape.jira.plugins.scrumpoker.service.ScrumPokerSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This condition is used to decide whether a button to join an active Scrum Poker session should be displayed or not
 * for the given issue. A currently active Scrum Poker session must exist for this condition to be fulfilled.
 */
@Component
public class JoinScrumPokerForIssueCondition extends AbstractIssueWebCondition {

    private final ScrumPokerSessionService scrumPokerSessionService;

    @Autowired
    public JoinScrumPokerForIssueCondition(ScrumPokerSessionService scrumPokerSessionService) {
        this.scrumPokerSessionService = scrumPokerSessionService;
    }

    @Override
    public boolean shouldDisplay(ApplicationUser applicationUser, Issue issue, JiraHelper jiraHelper) {
        return scrumPokerSessionService.hasActiveSession(issue.getKey());
    }

}
