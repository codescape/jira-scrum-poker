package net.congstar.jira.plugins.scrumpoker.data;

import java.util.HashMap;
import java.util.Map;

import net.congstar.jira.plugins.scrumpoker.model.ScrumPokerSession;

/**
 * Simple implementation using a Map to store the data.
 */
public class DefaultPlanningPokerStorage implements PlanningPokerStorage {

    private Map<String, ScrumPokerSession> scrumPokerSessions;

    public DefaultPlanningPokerStorage() {
        scrumPokerSessions = new HashMap<String, ScrumPokerSession>();
    }

    @Override
    public ScrumPokerSession sessionForIssue(String issueKey) {
        ScrumPokerSession scrumPokerSession = scrumPokerSessions.get(issueKey);
        if (scrumPokerSession == null) {
            scrumPokerSession = new ScrumPokerSession();
            scrumPokerSessions.put(issueKey, scrumPokerSession);
        }
        return scrumPokerSession;
    }

}
