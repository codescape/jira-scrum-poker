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

    @NotNull
    @PrimaryKey("issueKey")
    String getIssueKey();

    void setIssueKey(String issueKey);

    String getCreatorUserKey();

    void setCreatorUserKey(String creatorUserKey);

    @OneToMany(reverse = "getSession")
    ScrumPokerVote[] getVotes();

    boolean isVisible();

    void setVisible(boolean visible);

    Integer getConfirmedVote();

    void setConfirmedVote(Integer confirmedVote);

    Date getCreateDate();

    void setCreateDate(Date createDate);

}
