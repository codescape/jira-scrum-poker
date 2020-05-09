package de.codescape.jira.plugins.scrumpoker.rest.entities;

import org.codehaus.jackson.annotate.JsonAutoDetect;

import java.util.List;

/**
 * REST representation of a list of reference issues to be displayed during a Scrum Poker session.
 */
@JsonAutoDetect
public class ReferenceListEntity {

    private final List<ReferenceEntity> references;
    private final String estimate;
    private final boolean results;

    public ReferenceListEntity(List<ReferenceEntity> references, String estimate) {
        this.references = references;
        this.estimate = estimate;
        this.results = !references.isEmpty();
    }

    public List<ReferenceEntity> getReferences() {
        return references;
    }

    public String getEstimate() {
        return estimate;
    }

    public boolean isResults() {
        return results;
    }

}
