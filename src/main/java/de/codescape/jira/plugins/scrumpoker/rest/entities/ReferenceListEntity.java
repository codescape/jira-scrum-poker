package de.codescape.jira.plugins.scrumpoker.rest.entities;

import org.codehaus.jackson.annotate.JsonAutoDetect;

import java.util.List;

/**
 * REST representation of a list of reference issues to be displayed during a Scrum poker session.
 */
@JsonAutoDetect
public class ReferenceListEntity {

    private List<ReferenceEntity> references;
    private Integer estimation;
    private final boolean results;

    public ReferenceListEntity(List<ReferenceEntity> references, Integer estimation) {
        this.references = references;
        this.estimation = estimation;
        this.results = !references.isEmpty();
    }

    public List<ReferenceEntity> getReferences() {
        return references;
    }

    public Integer getEstimation() {
        return estimation;
    }

    public boolean isResults() {
        return results;
    }

}
