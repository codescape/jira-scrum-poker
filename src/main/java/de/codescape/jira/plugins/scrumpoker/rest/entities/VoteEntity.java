package de.codescape.jira.plugins.scrumpoker.rest.entities;

import org.codehaus.jackson.annotate.JsonAutoDetect;

/**
 * REST representation of a card chosen by a user.
 */
@JsonAutoDetect
public class VoteEntity {

    private String user;
    private String vote;
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
