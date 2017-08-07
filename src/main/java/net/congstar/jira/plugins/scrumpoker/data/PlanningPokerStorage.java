package net.congstar.jira.plugins.scrumpoker.data;

import net.congstar.jira.plugins.scrumpoker.model.ScrumPokerSession;

import java.util.Map;

public interface PlanningPokerStorage {

    /**
     * Return the Scrum poker session for the given issue.
     *
     * @param issueKey Key of the issue
     * @return Scrum poker session
     */
    ScrumPokerSession sessionForIssue(String issueKey);

    /**
     * Return the list of all Scrum poker sessions.
     *
     * @return list of all Scrum poker sessions
     */
    Map<String, ScrumPokerSession> getSessions();

}
