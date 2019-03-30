package de.codescape.jira.plugins.scrumpoker.rest.entities;

import org.codehaus.jackson.annotate.JsonAutoDetect;

/**
 * REST representation of a bounded vote.
 */
@JsonAutoDetect
public class BoundedVoteEntity {

    private final String value;
    private final Long count;
    private final boolean assignable;

    public BoundedVoteEntity(String value, Long count, boolean assignable) {
        this.value = value;
        this.count = count;
        this.assignable = assignable;
    }

    public String getValue() {
        return value;
    }

    public Long getCount() {
        return count;
    }

    public boolean isAssignable() {
        return assignable;
    }

}
