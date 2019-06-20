package de.codescape.jira.plugins.scrumpoker.ao;

import java.util.Date;

/**
 * Active Object to persist a user vote for a Scrum Poker session into the database.
 */
public interface ScrumPokerVote extends ScrumPokerEntity {

    /**
     * The Scrum Poker session this vote belongs to.
     */
    ScrumPokerSession getSession();

    void setSession(ScrumPokerSession scrumPokerSession);

    /**
     * The unique key of the user this vote is provided by.
     */
    String getUserKey();

    void setUserKey(String userKey);

    /**
     * The given vote of this user.
     */
    String getVote();

    void setVote(String vote);

    /**
     * The date this vote is provided.
     */
    Date getCreateDate();

    void setCreateDate(Date createDate);

}
