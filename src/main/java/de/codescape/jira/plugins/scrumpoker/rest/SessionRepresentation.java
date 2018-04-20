package de.codescape.jira.plugins.scrumpoker.rest;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * REST representation of a Scrum Poker session.
 */
@XmlRootElement(name = "session")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class SessionRepresentation {

    private String issueKey;

    private List<CardRepresentation> cards;

    private Integer confirmedVote;

    private boolean visible;

    private List<Integer> boundedVotes;

    private boolean agreementReached;

    private List<VoteRepresentation> votes;

    private boolean allowReset;

    private boolean allowReveal;

    public SessionRepresentation withIssueKey(String issueKey) {
        this.issueKey = issueKey;
        return this;
    }

    public String getIssueKey() {
        return issueKey;
    }

    public SessionRepresentation withCards(List<CardRepresentation> cards) {
        this.cards = cards;
        return this;
    }

    public List<CardRepresentation> getCards() {
        return cards;
    }

    public SessionRepresentation withConfirmedVote(Integer confirmedVote) {
        this.confirmedVote = confirmedVote;
        return this;
    }

    public Integer getConfirmedVote() {
        return confirmedVote;
    }

    public boolean isConfirmedVoteExists() {
        return confirmedVote != null;
    }

    public SessionRepresentation withVisible(boolean visible) {
        this.visible = visible;
        return this;
    }

    public boolean isVisible() {
        return visible;
    }

    public SessionRepresentation withBoundedVotes(List<Integer> boundedVotes) {
        this.boundedVotes = boundedVotes;
        return this;
    }

    public List<Integer> getBoundedVotes() {
        return boundedVotes;
    }

    public SessionRepresentation withAgreementReached(boolean agreementReached) {
        this.agreementReached = agreementReached;
        return this;
    }

    public boolean isAgreementReached() {
        return agreementReached;
    }

    public SessionRepresentation withVotes(List<VoteRepresentation> votes) {
        this.votes = votes;
        return this;
    }

    public List<VoteRepresentation> getVotes() {
        return votes;
    }

    public SessionRepresentation withAllowReset(boolean allowReset) {
        this.allowReset = allowReset;
        return this;
    }

    public boolean isAllowReset() {
        return allowReset;
    }

    public SessionRepresentation withAllowReveal(boolean allowReveal) {
        this.allowReveal = allowReveal;
        return this;
    }

    public boolean isAllowReveal() {
        return allowReveal;
    }

}
