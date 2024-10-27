package de.codescape.jira.plugins.scrumpoker.rest.entities;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

/**
 * REST representation of a card chosen by a user.
 */
@JsonAutoDetect
public class VoteEntity {

    private final String user;
    private final String vote;
    private final boolean needToTalk;
    private final boolean needABreak;

    public VoteEntity(String user, String vote, boolean needToTalk, boolean needABreak) {
        this.user = user;
        this.vote = vote;
        this.needToTalk = needToTalk;
        this.needABreak = needABreak;
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

    public boolean isNeedABreak() {
        return needABreak;
    }

}
