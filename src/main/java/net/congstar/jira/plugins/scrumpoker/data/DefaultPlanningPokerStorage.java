package net.congstar.jira.plugins.scrumpoker.data;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;

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
        removeOldScrumPokerSessions();
        ScrumPokerSession scrumPokerSession = scrumPokerSessions.get(issueKey);
        if (scrumPokerSession == null) {
            scrumPokerSession = new ScrumPokerSession();
            scrumPokerSessions.put(issueKey, scrumPokerSession);
        }
        return scrumPokerSession;
    }

    /**
     * Removes all sessions that are older than one day.
     */
    private void removeOldScrumPokerSessions() {
        for (String issueKey : scrumPokerSessions.keySet()) {
            if (scrumPokerSessions.get(issueKey).getStartedOn().isBefore(DateTime.now().minusDays(1))) {
                scrumPokerSessions.remove(issueKey);
            }
        }
    }

}
