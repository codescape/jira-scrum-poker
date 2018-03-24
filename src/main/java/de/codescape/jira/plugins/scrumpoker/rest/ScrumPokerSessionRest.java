package de.codescape.jira.plugins.scrumpoker.rest;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * REST representation of a Scrum Poker session.
 */
@XmlRootElement(name = "ScrumPokerSession")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class ScrumPokerSessionRest {

    private String issueKey;

    private List<CardRest> cards;

    private Integer confirmedVote;

    private boolean visible;

    private List<Integer> boundedVotes;

    private boolean agreementReached;

    private List<VotesRest> votes;

    private boolean allowReset;

    private boolean allowReveal;

    public ScrumPokerSessionRest withIssueKey(String issueKey) {
        this.issueKey = issueKey;
        return this;
    }

    public String getIssueKey() {
        return issueKey;
    }

    public ScrumPokerSessionRest withCards(List<CardRest> cards) {
        this.cards = cards;
        return this;
    }

    public List<CardRest> getCards() {
        return cards;
    }

    public ScrumPokerSessionRest withConfirmedVote(Integer confirmedVote) {
        this.confirmedVote = confirmedVote;
        return this;
    }

    public Integer getConfirmedVote() {
        return confirmedVote;
    }

    public ScrumPokerSessionRest withVisible(boolean visible) {
        this.visible = visible;
        return this;
    }

    public boolean isVisible() {
        return visible;
    }

    public ScrumPokerSessionRest withBoundedVotes(List<Integer> boundedVotes) {
        this.boundedVotes = boundedVotes;
        return this;
    }

    public List<Integer> getBoundedVotes() {
        return boundedVotes;
    }

    public ScrumPokerSessionRest withAgreementReached(boolean agreementReached) {
        this.agreementReached = agreementReached;
        return this;
    }

    public boolean isAgreementReached() {
        return agreementReached;
    }

    public ScrumPokerSessionRest withVotes(List<VotesRest> votes) {
        this.votes = votes;
        return this;
    }

    public List<VotesRest> getVotes() {
        return votes;
    }

    public ScrumPokerSessionRest withAllowReset(boolean allowReset) {
        this.allowReset = allowReset;
        return this;
    }

    public boolean isAllowReset() {
        return allowReset;
    }

    public ScrumPokerSessionRest withAllowReveal(boolean allowReveal) {
        this.allowReveal = allowReveal;
        return this;
    }

    public boolean isAllowReveal() {
        return allowReveal;
    }

}
