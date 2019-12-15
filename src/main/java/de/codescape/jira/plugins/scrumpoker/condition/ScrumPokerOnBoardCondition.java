package de.codescape.jira.plugins.scrumpoker.condition;

import com.atlassian.jira.plugin.webfragment.conditions.AbstractWebCondition;
import com.atlassian.jira.plugin.webfragment.model.JiraHelper;
import com.atlassian.jira.user.ApplicationUser;
import de.codescape.jira.plugins.scrumpoker.service.ScrumPokerSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This condition is used to decide whether the Scrum Poker dropdown shall be shown on the boards view that is
 * introduced by Jira Software. This is one possible entry point to the active sessions list for example.
 */
@Component
public class ScrumPokerOnBoardCondition extends AbstractWebCondition {

    private final ScrumPokerSettingService scrumPokerSettingService;

    @Autowired
    public ScrumPokerOnBoardCondition(ScrumPokerSettingService scrumPokerSettingService) {
        this.scrumPokerSettingService = scrumPokerSettingService;
    }

    @Override
    public boolean shouldDisplay(ApplicationUser applicationUser, JiraHelper jiraHelper) {
        return scrumPokerSettingService.load().isDisplayDropdownOnBoards();
    }

}
