package de.codescape.jira.plugins.scrumpoker.rest.entities;

import org.codehaus.jackson.annotate.JsonAutoDetect;

import java.util.List;

/**
 * REST representation of a Scrum Poker session.
 */
@JsonAutoDetect
public class SessionEntity {

    private String issueKey;
    private List<CardEntity> cards;
    private String confirmedEstimate;
    private String confirmedUser;
    private DateEntity confirmedDate;
    private boolean visible;
    private List<BoundedVoteEntity> boundedVotes;
    private boolean agreementReached;
    private boolean cancelled;
    private List<VoteEntity> votes;
    private Integer voteCount;
    private boolean allowReset;
    private boolean allowReveal;
    private boolean allowCancel;
    private boolean allowConfirm;
    private String creator;
    private DateEntity createDate;

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

    public SessionEntity withConfirmedEstimate(String confirmedEstimate) {
        this.confirmedEstimate = confirmedEstimate;
        return this;
    }

    public String getConfirmedEstimate() {
        return confirmedEstimate;
    }

    public boolean isConfirmedEstimateExists() {
        return confirmedEstimate != null;
    }

    public SessionEntity withConfirmedUser(String confirmedUser) {
        this.confirmedUser = confirmedUser;
        return this;
    }

    public String getConfirmedUser() {
        return confirmedUser;
    }

    public SessionEntity withConfirmedDate(DateEntity confirmedDate) {
        this.confirmedDate = confirmedDate;
        return this;
    }

    public DateEntity getConfirmedDate() {
        return confirmedDate;
    }

    public SessionEntity withVisible(boolean visible) {
        this.visible = visible;
        return this;
    }

    public boolean isVisible() {
        return visible;
    }

    public SessionEntity withBoundedVotes(List<BoundedVoteEntity> boundedVotes) {
        this.boundedVotes = boundedVotes;
        return this;
    }

    public List<BoundedVoteEntity> getBoundedVotes() {
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
        this.voteCount = votes.size();
        return this;
    }

    public List<VoteEntity> getVotes() {
        return votes;
    }

    public Integer getVoteCount() {
        return voteCount;
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

    public boolean isAllowConfirm() {
        return allowConfirm;
    }

    public SessionEntity withAllowConfirm(boolean allowConfirm) {
        this.allowConfirm = allowConfirm;
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

    public SessionEntity withCreateDate(DateEntity createDate) {
        this.createDate = createDate;
        return this;
    }

    public DateEntity getCreateDate() {
        return createDate;
    }

}
