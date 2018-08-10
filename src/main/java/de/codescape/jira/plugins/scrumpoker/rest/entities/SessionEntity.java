package de.codescape.jira.plugins.scrumpoker.rest.entities;

import org.codehaus.jackson.annotate.JsonAutoDetect;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * REST representation of a Scrum Poker session.
 */
@JsonAutoDetect
public class SessionEntity {

    private String issueKey;
    private List<CardEntity> cards;
    private Integer confirmedVote;
    private boolean visible;
    private List<Integer> boundedVotes;
    private boolean agreementReached;
    private boolean cancelled;
    private List<VoteEntity> votes;
    private boolean allowReset;
    private boolean allowReveal;
    private boolean allowCancel;
    private String creator;
    private String createDate;

    public SessionEntity withIssueKey(String issueKey) {
        this.issueKey = issueKey;
        return this;
    }

    public String getIssueKey() {
        return issueKey;
    }

    public SessionEntity withCards(List<CardEntity> cards) {
        this.cards = cards;
        return this;
    }

    public List<CardEntity> getCards() {
        return cards;
    }

    public SessionEntity withConfirmedVote(Integer confirmedVote) {
        this.confirmedVote = confirmedVote;
        return this;
    }

    public Integer getConfirmedVote() {
        return confirmedVote;
    }

    public boolean isConfirmedVoteExists() {
        return confirmedVote != null;
    }

    public SessionEntity withVisible(boolean visible) {
        this.visible = visible;
        return this;
    }

    public boolean isVisible() {
        return visible;
    }

    public SessionEntity withBoundedVotes(List<Integer> boundedVotes) {
        this.boundedVotes = boundedVotes;
        return this;
    }

    public List<Integer> getBoundedVotes() {
        return boundedVotes;
    }

    public SessionEntity withAgreementReached(boolean agreementReached) {
        this.agreementReached = agreementReached;
        return this;
    }

    public boolean isAgreementReached() {
        return agreementReached;
    }

    public SessionEntity withCancelled(boolean cancelled) {
        this.cancelled = cancelled;
        return this;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public SessionEntity withVotes(List<VoteEntity> votes) {
        this.votes = votes;
        return this;
    }

    public List<VoteEntity> getVotes() {
        return votes;
    }

    public SessionEntity withAllowReset(boolean allowReset) {
        this.allowReset = allowReset;
        return this;
    }

    public boolean isAllowReset() {
        return allowReset;
    }

    public SessionEntity withAllowReveal(boolean allowReveal) {
        this.allowReveal = allowReveal;
        return this;
    }

    public boolean isAllowCancel() {
        return allowCancel;
    }

    public SessionEntity withAllowCancel(boolean allowCancel) {
        this.allowCancel = allowCancel;
        return this;
    }

    public boolean isAllowReveal() {
        return allowReveal;
    }

    public SessionEntity withCreator(String creator) {
        this.creator = creator;
        return this;
    }

    public String getCreator() {
        return creator;
    }

    public SessionEntity withCreateDate(Date createDate) {
        this.createDate = new SimpleDateFormat("dd.MM.yyyy - HH:mm:ss").format(createDate);
        return this;
    }

    public String getCreateDate() {
        return createDate;
    }

}
