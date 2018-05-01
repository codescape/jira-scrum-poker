package de.codescape.jira.plugins.scrumpoker.rest.entities;

import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * REST representation of a card chosen by a user.
 */
@JsonSerialize
public class VoteEntity {

    @JsonSerialize
    private String user;

    @JsonSerialize
    private String vote;

    @JsonSerialize
    private boolean needToTalk;

    public VoteEntity(String user, String vote, boolean needToTalk) {
        this.user = user;
        this.vote = vote;
        this.needToTalk = needToTalk;
    }

    public String getUser() {
        return user;
    }

    public String getVote() {
        return vote;
    }

    public boolean isNeedToTalk() {
        return needToTalk;
    }

}
