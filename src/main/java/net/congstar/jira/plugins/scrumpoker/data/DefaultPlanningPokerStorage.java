package net.congstar.jira.plugins.scrumpoker.data;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

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
        Iterator<Entry<String, ScrumPokerSession>> iterator = scrumPokerSessions.entrySet().iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getValue().getStartedOn().isBefore(DateTime.now().minusDays(1))) {
                iterator.remove();
            }
        }
    }

}
