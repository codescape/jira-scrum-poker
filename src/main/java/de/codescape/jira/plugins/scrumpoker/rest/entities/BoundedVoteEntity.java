package de.codescape.jira.plugins.scrumpoker.rest.entities;

import org.codehaus.jackson.annotate.JsonAutoDetect;

import java.util.Objects;

/**
 * REST representation of a bounded vote.
 */
@JsonAutoDetect
public class BoundedVoteEntity {

    private final Integer value;
    private final Integer count;

    public BoundedVoteEntity(Integer value, Integer count) {
        this.value = value;
        this.count = count;
    }

    public Integer getValue() {
        return value;
    }

    public Integer getCount() {
        return count;
    }

}
