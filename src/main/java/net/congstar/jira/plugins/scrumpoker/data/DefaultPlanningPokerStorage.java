package net.congstar.jira.plugins.scrumpoker.data;

import net.congstar.jira.plugins.scrumpoker.model.ScrumPokerSession;
import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Simple implementation using a Map to store the data.
 */
public class DefaultPlanningPokerStorage implements PlanningPokerStorage {

    private static final int POKER_SESSION_TIMEOUT_HOURS = 12;

    private final Map<String, ScrumPokerSession> scrumPokerSessions;

    public DefaultPlanningPokerStorage() {
        scrumPokerSessions = new HashMap<>();
    }

    @Override
    public ScrumPokerSession sessionForIssue(String issueKey) {
        synchronized (scrumPokerSessions) {
            removeOldScrumPokerSessions();
        }
        ScrumPokerSession scrumPokerSession = scrumPokerSessions.get(issueKey);
        if (scrumPokerSession == null) {
            scrumPokerSession = new ScrumPokerSession();
            scrumPokerSessions.put(issueKey, scrumPokerSession);
        }
        return scrumPokerSession;
    }

    @Override
    public Map<String, ScrumPokerSession> getSessions() {
        return scrumPokerSessions;
    }

    /**
     * Removes all sessions that are older than one day.
     */
    private void removeOldScrumPokerSessions() {
        scrumPokerSessions.entrySet().removeIf(sessionEntry ->
                sessionEntry.getValue().getStartedOn().isBefore(DateTime.now().minusHours(POKER_SESSION_TIMEOUT_HOURS))
        );
    }

}
