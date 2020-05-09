package de.codescape.jira.plugins.scrumpoker.rest.mapper;

import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerSession;
import de.codescape.jira.plugins.scrumpoker.rest.entities.ReferenceEntity;
import de.codescape.jira.plugins.scrumpoker.rest.entities.ReferenceListEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Service that allows the map a list of {@link ScrumPokerSession} elements to a {@link ReferenceListEntity} that
 * contains a {@link ReferenceEntity} element for every {@link ScrumPokerSession}. This service creates a model that can
 * be used and transferred as a REST resource and is optimized for a logic less templating mechanism.
 */
@Component
public class SessionReferenceMapper {

    private final IssueManager issueManager;

    @Autowired
    public SessionReferenceMapper(@ComponentImport IssueManager issueManager) {
        this.issueManager = issueManager;
    }

    /**
     * Map a given list of {@link ScrumPokerSession} and a given estimate to a {@link ReferenceListEntity}.
     *
     * @param scrumPokerSessions list of {@link ScrumPokerSession}
     * @param estimate           estimate
     * @return transformed {@link ReferenceListEntity}
     */
    public ReferenceListEntity build(List<ScrumPokerSession> scrumPokerSessions, String estimate) {
        return new ReferenceListEntity(scrumPokerSessions.stream()
            .map(scrumPokerSession -> {
                MutableIssue issue = issueManager.getIssueObject(scrumPokerSession.getIssueKey());
                return issue != null ? referenceFromIssue(issue) : null;
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toList()), estimate);
    }

    /**
     * Create the reference with key, icon and summary from the issue.
     */
    private ReferenceEntity referenceFromIssue(MutableIssue issue) {
        return new ReferenceEntity(issue.getKey(),
            issue.getIssueType() != null ? issue.getIssueType().getCompleteIconUrl() : null,
            issue.getSummary());
    }

}
