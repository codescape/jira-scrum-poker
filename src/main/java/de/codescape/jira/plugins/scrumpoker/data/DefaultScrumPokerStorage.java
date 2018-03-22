package de.codescape.jira.plugins.scrumpoker.data;

import de.codescape.jira.plugins.scrumpoker.model.ScrumPokerSession;
import org.joda.time.DateTime;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Simple implementation using a Map to store the data.
 */
public class DefaultScrumPokerStorage implements ScrumPokerStorage {

    private static final int POKER_SESSION_TIMEOUT_HOURS = 12;

    private final Map<String, ScrumPokerSession> scrumPokerSessions;

    public DefaultScrumPokerStorage() {
        scrumPokerSessions = new ConcurrentHashMap<>();
    }

    @Override
    public ScrumPokerSession sessionForIssue(String issueKey, String userKey) {
        return validSessions().computeIfAbsent(issueKey, k -> new ScrumPokerSession(k, userKey));
    }

    @Override
    public List<ScrumPokerSession> getOpenSessions() {
        return validSessions().values().stream()
            .filter(session -> session.getConfirmedVote() == null)
            .sorted(Comparator.comparing(ScrumPokerSession::getStartedOn).reversed())
            .collect(Collectors.toList());
    }

    @Override
    public List<ScrumPokerSession> getClosedSessions() {
        return validSessions().values().stream()
            .filter(session -> session.getConfirmedVote() != null)
            .sorted(Comparator.comparing(ScrumPokerSession::getStartedOn).reversed())
            .collect(Collectors.toList());
    }

    /**
     * Removes all sessions that are older than the timeout.
     */
    private Map<String, ScrumPokerSession> validSessions() {
        scrumPokerSessions.entrySet().removeIf(sessionEntry ->
            sessionEntry.getValue().getStartedOn().isBefore(DateTime.now().minusHours(POKER_SESSION_TIMEOUT_HOURS))
        );
        return scrumPokerSessions;
    }

}
