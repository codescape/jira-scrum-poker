package net.congstar.jira.plugins.scrumpoker.data;

import net.congstar.jira.plugins.scrumpoker.model.ScrumPokerSession;
import org.joda.time.DateTime;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Simple implementation using a Map to store the data.
 */
public class DefaultScrumPokerStorage implements ScrumPokerStorage {

    private static final int POKER_SESSION_TIMEOUT_HOURS = 12;

    private final Map<String, ScrumPokerSession> scrumPokerSessions;

    public DefaultScrumPokerStorage() {
        scrumPokerSessions = new HashMap<>();
    }

    @Override
    public ScrumPokerSession sessionForIssue(String issueKey, String userKey) {
        synchronized (scrumPokerSessions) {
            removeOldScrumPokerSessions();
        }
        ScrumPokerSession scrumPokerSession = scrumPokerSessions.get(issueKey);
        if (scrumPokerSession == null) {
            scrumPokerSession = new ScrumPokerSession(issueKey, userKey);
            scrumPokerSessions.put(issueKey, scrumPokerSession);
        }
        return scrumPokerSession;
    }

    @Override
    public List<ScrumPokerSession> getOpenSessions() {
        return scrumPokerSessions.values().stream()
                .filter(session -> session.getConfirmedVote() == null)
                .sorted(Comparator.comparing(ScrumPokerSession::getStartedOn).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<ScrumPokerSession> getClosedSessions() {
        return scrumPokerSessions.values().stream()
                .filter(session -> session.getConfirmedVote() != null)
                .sorted(Comparator.comparing(ScrumPokerSession::getStartedOn).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Removes all sessions that are older than the timeout.
     */
    private void removeOldScrumPokerSessions() {
        scrumPokerSessions.entrySet().removeIf(sessionEntry ->
                sessionEntry.getValue().getStartedOn().isBefore(DateTime.now().minusHours(POKER_SESSION_TIMEOUT_HOURS))
        );
    }

}
