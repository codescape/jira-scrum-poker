package de.codescape.jira.plugins.scrumpoker.service;

import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.MutableIssue;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerSession;
import de.codescape.jira.plugins.scrumpoker.rest.entities.ReferenceEntity;
import de.codescape.jira.plugins.scrumpoker.rest.entities.ReferenceListEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Service that allows the transform a list of {@link ScrumPokerSession} elements into a {@link ReferenceListEntity}
 * that contains a {@link ReferenceEntity} element for every {@link ScrumPokerSession}. This service creates a model
 * that can be used and transferred as a REST resource and is optimized for a logic less templating mechanism.
 */
@Component
public class SessionReferenceTransformer {

    private final IssueManager issueManager;

    @Autowired
    public SessionReferenceTransformer(IssueManager issueManager) {
        this.issueManager = issueManager;
    }

    /**
     * Transform a given list of {@link ScrumPokerSession} and a given estimation into a {@link ReferenceListEntity}.
     *
     * @param scrumPokerSessions list of {@link ScrumPokerSession}
     * @param estimation         estimation
     * @return transformed {@link ReferenceListEntity}
     */
    public ReferenceListEntity build(List<ScrumPokerSession> scrumPokerSessions, Integer estimation) {
        return new ReferenceListEntity(scrumPokerSessions.stream()
            .map(scrumPokerSession -> {
                MutableIssue issue = issueManager.getIssueObject(scrumPokerSession.getIssueKey());
                return issue != null ? new ReferenceEntity(scrumPokerSession.getIssueKey(),
                    issue.getIssueType() != null ? issue.getIssueType().getCompleteIconUrl() : null,
                    issue.getSummary()) : null;
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toList()), estimation);
    }

}
