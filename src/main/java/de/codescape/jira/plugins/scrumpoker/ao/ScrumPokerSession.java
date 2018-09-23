package de.codescape.jira.plugins.scrumpoker.ao;

import net.java.ao.OneToMany;
import net.java.ao.RawEntity;
import net.java.ao.schema.NotNull;
import net.java.ao.schema.PrimaryKey;

import java.util.Date;

/**
 * Active Object to persist a Scrum poker session into the database.
 */
public interface ScrumPokerSession extends RawEntity<String> {

    /**
     * The unique key of the issue this Scrum poker session is started for.
     */
    @NotNull
    @PrimaryKey("issueKey")
    String getIssueKey();

    void setIssueKey(String issueKey);

    /**
     * The unique key of the user this Scrum poker session is started by.
     */
    String getCreatorUserKey();

    void setCreatorUserKey(String creatorUserKey);

    /**
     * The list of votes provided by users for this Scrum poker session.
     */
    @OneToMany(reverse = "getSession")
    ScrumPokerVote[] getVotes();

    /**
     * The visibility of the Scrum poker session defines whether the votes are revealed or hidden.
     */
    boolean isVisible();

    void setVisible(boolean visible);

    /**
     * The confirmed vote for this Scrum poker session if exists.
     */
    Integer getConfirmedVote();

    void setConfirmedVote(Integer confirmedVote);

    /**
     * The date this Scrum poker session is started.
     */
    Date getCreateDate();

    void setCreateDate(Date createDate);

    /**
     * The Scrum poker session can be cancelled.
     */
    boolean isCancelled();

    void setCancelled(boolean cancelled);

}
