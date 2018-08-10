package de.codescape.jira.plugins.scrumpoker.service;

import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.user.util.UserManager;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerSession;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerVote;
import de.codescape.jira.plugins.scrumpoker.rest.entities.CardEntity;
import de.codescape.jira.plugins.scrumpoker.rest.entities.SessionEntity;
import de.codescape.jira.plugins.scrumpoker.rest.entities.VoteEntity;
import org.apache.commons.lang3.math.NumberUtils;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static de.codescape.jira.plugins.scrumpoker.model.ScrumPokerCard.QUESTION_MARK;
import static de.codescape.jira.plugins.scrumpoker.model.ScrumPokerCard.getDeck;
import static java.util.Arrays.stream;
import static org.apache.commons.lang3.math.NumberUtils.isNumber;

/**
 * Service that allows to transform a {@link ScrumPokerSession} into a {@link SessionEntity}. This service creates a
 * model that can be used and transferred as a REST resource and is optimized for a logic less templating mechanism.
 */
@Scanned
@Named
public class SessionEntityTransformer {

    @ComponentImport
    private final UserManager userManager;

    @Inject
    public SessionEntityTransformer(UserManager userManager) {
        this.userManager = userManager;
    }

    /**
     * Transform a given {@link ScrumPokerSession} into a {@link SessionEntity}.
     *
     * @param scrumPokerSession {@link ScrumPokerSession} to transform
     * @param userKey           key of the user
     * @return transformed {@link SessionEntity}
     */
    public SessionEntity build(ScrumPokerSession scrumPokerSession, String userKey) {
        return new SessionEntity()
            .withIssueKey(scrumPokerSession.getIssueKey())
            .withConfirmedVote(scrumPokerSession.getConfirmedVote())
            .withVisible(scrumPokerSession.isVisible())
            .withCancelled(scrumPokerSession.isCancelled())
            .withBoundedVotes(boundedVotes(scrumPokerSession.getVotes()))
            .withVotes(votes(scrumPokerSession))
            .withCards(cards(scrumPokerSession, userKey))
            .withAllowReveal(allowReveal(scrumPokerSession))
            .withAllowReset(allowReset(scrumPokerSession))
            .withAllowCancel(allowCancel(scrumPokerSession, userKey))
            .withAgreementReached(agreementReached(scrumPokerSession))
            .withCreator(displayName(scrumPokerSession.getCreatorUserKey()))
            .withCreateDate(scrumPokerSession.getCreateDate());
    }

    /**
     * Cancellation of a Scrum poker session is only allowed for the user who started the session.
     */
    private boolean allowCancel(ScrumPokerSession scrumPokerSession, String userKey) {
        return scrumPokerSession.getCreatorUserKey() != null && scrumPokerSession.getCreatorUserKey().equals(userKey);
    }

    /**
     * Resetting a Scrum poker session is allowed when minimum one vote is given and the votes are visible.
     */
    private boolean allowReset(ScrumPokerSession scrumPokerSession) {
        return scrumPokerSession.isVisible() && scrumPokerSession.getVotes().length > 0;
    }

    /**
     * Revealing a Scrum poker session is allowed when minimum one vote is given and the votes are hidden.
     */
    private boolean allowReveal(ScrumPokerSession scrumPokerSession) {
        return !scrumPokerSession.isVisible() && scrumPokerSession.getVotes().length > 0;
    }

    /**
     * Returns that an agreement is reached when more than one vote is given, the votes are visible, there is no
     * question mark and the minimum and maximum vote are equal.
     */
    private boolean agreementReached(ScrumPokerSession scrumPokerSession) {
        ScrumPokerVote[] votes = scrumPokerSession.getVotes();
        return scrumPokerSession.isVisible() &&
            votes.length > 1 &&
            getMaximumVote(votes).equals(getMinimumVote(votes)) &&
            stream(votes).noneMatch(scrumPokerVote -> scrumPokerVote.getVote().equals(QUESTION_MARK.getName()));
    }

    /**
     * Returns the cards and if the current user has provided a vote highlights the selected card.
     */
    private List<CardEntity> cards(ScrumPokerSession scrumPokerSession, String userKey) {
        String chosenValue = cardForUser(scrumPokerSession.getVotes(), userKey);
        return getDeck().stream()
            .map(card -> new CardEntity(card.getName(), card.getName().equals(chosenValue)))
            .collect(Collectors.toList());
    }

    /**
     * Returns the value of the card the current user has selected or null if the user has not voted.
     */
    private String cardForUser(ScrumPokerVote[] votes, String userKey) {
        Optional<ScrumPokerVote> match = stream(votes)
            .filter(vote -> vote.getUserKey().equals(userKey))
            .findFirst();
        return match.map(ScrumPokerVote::getVote).orElse(null);
    }

    /**
     * Returns all votes and marks all votes that need to talk. If the deck is currently hidden only returns a question
     * mark instead of the correct values in order to not leak them to the clients.
     */
    private List<VoteEntity> votes(ScrumPokerSession scrumPokerSession) {
        return stream(scrumPokerSession.getVotes())
            .map(vote -> new VoteEntity(
                displayName(vote.getUserKey()),
                scrumPokerSession.isVisible() ? vote.getVote() : "?",
                needToTalk(vote.getVote(), scrumPokerSession)))
            .collect(Collectors.toList());
    }

    /**
     * Returns whether the current vote needs to talk or not depending on the status of the Scrum poker session and the
     * other votes provided.
     */
    private boolean needToTalk(String vote, ScrumPokerSession scrumPokerSession) {
        if (!scrumPokerSession.isVisible())
            return false;
        if (agreementReached(scrumPokerSession))
            return false;
        if (scrumPokerSession.getVotes().length == 1)
            return false;
        if (!isNumber(vote))
            return true;
        Integer current = Integer.valueOf(vote);
        return current.equals(getMaximumVote(scrumPokerSession.getVotes()))
            || current.equals(getMinimumVote(scrumPokerSession.getVotes()));
    }

    /**
     * Returns the list of cards between and including the minimum and maximum vote.
     */
    private List<Integer> boundedVotes(ScrumPokerVote[] votes) {
        return getDeck().stream()
            .filter(scrumPokerCard -> isNumber(scrumPokerCard.getName()))
            .map(scrumPokerCard -> Integer.valueOf(scrumPokerCard.getName()))
            .filter(value -> value >= getMinimumVote(votes) && value <= getMaximumVote(votes))
            .collect(Collectors.toList());
    }

    /**
     * Returns the minimum vote from the given list of votes.
     */
    private Integer getMinimumVote(ScrumPokerVote[] votes) {
        return numericValues(votes).stream().reduce(Integer::min).orElse(0);
    }

    /**
     * Returns the maximum vote from the given list of votes.
     */
    private Integer getMaximumVote(ScrumPokerVote[] votes) {
        return numericValues(votes).stream().reduce(Integer::max).orElse(100);
    }

    /**
     * Reduces the list of votes to only numeric votes removing all other values from the list.
     */
    private List<Integer> numericValues(ScrumPokerVote[] votes) {
        return stream(votes).map(ScrumPokerVote::getVote)
            .filter(NumberUtils::isNumber)
            .map(Integer::valueOf)
            .collect(Collectors.toList());
    }

    /**
     * Returns the display name of a user associated by the given user key.
     */
    private String displayName(String key) {
        ApplicationUser user = userManager.getUserByKey(key);
        return user != null ? user.getDisplayName() : key;
    }

}
