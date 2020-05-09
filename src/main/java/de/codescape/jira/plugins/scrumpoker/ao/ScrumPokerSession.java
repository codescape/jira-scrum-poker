package de.codescape.jira.plugins.scrumpoker.ao;

import net.java.ao.OneToMany;
import net.java.ao.RawEntity;
import net.java.ao.schema.NotNull;
import net.java.ao.schema.PrimaryKey;

import java.util.Date;

/**
 * Active Object to persist a Scrum Poker session into the database.
 */
public interface ScrumPokerSession extends RawEntity<String> {

    /**
     * The unique key of the issue this Scrum Poker session is started for.
     */
    @NotNull
    @PrimaryKey("issueKey")
    String getIssueKey();

    void setIssueKey(String issueKey);

    /**
     * The unique key of the user this Scrum Poker session is started by.
     */
    String getCreatorUserKey();

    void setCreatorUserKey(String creatorUserKey);

    /**
     * The list of votes provided by users for this Scrum Poker session.
     */
    @OneToMany(reverse = "getSession")
    ScrumPokerVote[] getVotes();

    /**
     * The visibility of the Scrum Poker session defines whether the votes are revealed or hidden.
     */
    boolean isVisible();

    void setVisible(boolean visible);

    /**
     * @deprecated since 4.11 replaced by {@link #getConfirmedEstimate()}
     */
    @Deprecated
    Integer getConfirmedVote();

    /**
     * @deprecated since 4.11 replaced by {@link #setConfirmedEstimate(String)}
     */
    @Deprecated
    void setConfirmedVote(Integer confirmedVote);

    /**
     * The confirmed estimate for this Scrum Poker session if exists.
     */
    String getConfirmedEstimate();

    void setConfirmedEstimate(String estimate);

    /**
     * The unique key of the user who confirmed the estimation.
     */
    String getConfirmedUserKey();

    void setConfirmedUserKey(String userKey);

    /**
     * The date this Scrum Poker session got confirmed.
     */
    Date getConfirmedDate();

    void setConfirmedDate(Date confirmedDate);

    /**
     * The date this Scrum Poker session is started.
     */
    Date getCreateDate();

    void setCreateDate(Date createDate);

    /**
     * The date this Scrum Poker session was last updated.
     */
    Date getUpdateDate();

    void setUpdateDate(Date updateDate);

    /**
     * The Scrum Poker session can be cancelled.
     */
    boolean isCancelled();

    void setCancelled(boolean cancelled);

    /**
     * The card set used by this Scrum Poker session.
     */
    String getCardSet();

    void setCardSet(String cardSet);

}
