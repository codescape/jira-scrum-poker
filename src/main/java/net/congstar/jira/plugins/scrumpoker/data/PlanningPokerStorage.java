package net.congstar.jira.plugins.scrumpoker.data;

import net.congstar.jira.plugins.scrumpoker.model.ScrumPokerSession;

public interface PlanningPokerStorage {

    /**
     * Return the Scrum poker session for the given issue.
     * 
     * @param issueKey
     * @return
     */
    ScrumPokerSession sessionForIssue(String issueKey);

}
