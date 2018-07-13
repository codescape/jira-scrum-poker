package de.codescape.jira.plugins.scrumpoker.ao;

import net.java.ao.Entity;

import java.util.Date;

/**
 * Active Object to persist a user vote for a Scrum poker session into the database.
 */
public interface ScrumPokerVote extends Entity {

    ScrumPokerSession getSession();

    void setSession(ScrumPokerSession scrumPokerSession);

    String getUserKey();

    void setUserKey(String userKey);

    String getVote();

    void setVote(String vote);

    Date getCreateDate();

    void setCreateDate(Date createDate);

}
