package de.codescape.jira.plugins.scrumpoker.rest.mapper;

import com.atlassian.jira.datetime.DateTimeFormatter;
import com.atlassian.jira.datetime.DateTimeStyle;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.permission.ProjectPermissions;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.user.util.UserManager;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerSession;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerVote;
import de.codescape.jira.plugins.scrumpoker.model.AllowRevealDeck;
import de.codescape.jira.plugins.scrumpoker.model.ScrumPokerCard;
import de.codescape.jira.plugins.scrumpoker.rest.entities.*;
import de.codescape.jira.plugins.scrumpoker.service.ScrumPokerSettingService;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static de.codescape.jira.plugins.scrumpoker.model.ScrumPokerCard.getDeck;
import static java.util.Arrays.stream;
import static org.apache.commons.lang3.math.NumberUtils.isNumber;

/**
 * Service that allows to map a {@link ScrumPokerSession} to a {@link SessionEntity}. This service creates a model that
 * can be used and transferred as a REST resource and is optimized for a logic less templating mechanism.
 */
@Component
public class SessionEntityMapper {

    private final UserManager userManager;
    private final DateTimeFormatter dateTimeFormatter;
    private final ScrumPokerSettingService scrumPokerSettingService;
    private final PermissionManager permissionManager;
    private final IssueManager issueManager;

    @Autowired
    public SessionEntityMapper(@ComponentImport UserManager userManager,
                               @ComponentImport DateTimeFormatter dateTimeFormatter,
                               @ComponentImport PermissionManager permissionManager,
                               @ComponentImport IssueManager issueManager,
                               ScrumPokerSettingService scrumPokerSettingService) {
        this.userManager = userManager;
        this.dateTimeFormatter = dateTimeFormatter;
        this.permissionManager = permissionManager;
        this.issueManager = issueManager;
        this.scrumPokerSettingService = scrumPokerSettingService;
    }

    /**
     * Map a given {@link ScrumPokerSession} to a {@link SessionEntity}.
     *
     * @param scrumPokerSession {@link ScrumPokerSession} to transform
     * @param userKey           key of the user
     * @return transformed {@link SessionEntity}
     */
    public SessionEntity build(ScrumPokerSession scrumPokerSession, String userKey) {
        return new SessionEntity()
            .withIssueKey(scrumPokerSession.getIssueKey())
            .withConfirmedVote(scrumPokerSession.getConfirmedVote())
            .withConfirmedDate(dateEntity(scrumPokerSession.getConfirmedDate()))
            .withConfirmedUser(displayName(scrumPokerSession.getConfirmedUserKey()))
            .withVisible(scrumPokerSession.isVisible())
            .withCancelled(scrumPokerSession.isCancelled())
            .withBoundedVotes(boundedVotes(scrumPokerSession.getVotes()))
            .withVotes(votes(scrumPokerSession))
            .withCards(cards(scrumPokerSession, userKey))
            .withAllowReveal(allowReveal(scrumPokerSession, userKey))
            .withAllowReset(allowReset(scrumPokerSession))
            .withAllowCancel(allowCancel(scrumPokerSession, userKey))
            .withAllowConfirm(allowConfirm(scrumPokerSession, userKey))
            .withAgreementReached(agreementReached(scrumPokerSession))
            .withCreator(displayName(scrumPokerSession.getCreatorUserKey()))
            .withCreateDate(dateEntity(scrumPokerSession.getCreateDate()));
    }

    /**
     * Format dates using the relative representation as display value and the complete representation as hover
     * information.
     */
    private DateEntity dateEntity(Date date) {
        String displayValue = null;
        String formattedDate = null;
        if (date != null) {
            DateTimeFormatter dateTimeFormatter = this.dateTimeFormatter.forLoggedInUser();
            displayValue = dateTimeFormatter.withStyle(DateTimeStyle.RELATIVE).format(date);
            formattedDate = dateTimeFormatter.withStyle(DateTimeStyle.COMPLETE).format(date);
        }
        return new DateEntity(displayValue, formattedDate);
    }

    /**
     * Cancellation of a Scrum Poker session is only allowed for the user who started the session.
     */
    private boolean allowCancel(ScrumPokerSession scrumPokerSession, String userKey) {
        return scrumPokerSession.getCreatorUserKey() != null && scrumPokerSession.getCreatorUserKey().equals(userKey);
    }

    /**
     * Confirmation of estimation is only allowed if permission check is disabled or user has permission.
     */
    private boolean allowConfirm(ScrumPokerSession scrumPokerSession, String userKey) {
        return !scrumPokerSettingService.load().isCheckPermissionToSaveEstimate() ||
            permissionManager.hasPermission(
                ProjectPermissions.EDIT_ISSUES,
                issueManager.getIssueObject(scrumPokerSession.getIssueKey()),
                userManager.getUserByKey(userKey));
    }

    /**
     * Resetting a Scrum Poker session is allowed when minimum one vote is given and the votes are visible.
     */
    private boolean allowReset(ScrumPokerSession scrumPokerSession) {
        return scrumPokerSession.isVisible() && scrumPokerSession.getVotes().length > 0;
    }

    /**
     * Revealing a Scrum Poker session is allowed when minimum one vote is given and the votes are hidden.
     */
    private boolean allowReveal(ScrumPokerSession scrumPokerSession, String userKey) {
        AllowRevealDeck allowRevealDeck = scrumPokerSettingService.load().getAllowRevealDeck();
        boolean userMayReveal = allowRevealDeck.equals(AllowRevealDeck.EVERYONE) ||
            allowRevealDeck.equals(AllowRevealDeck.CREATOR) && scrumPokerSession.getCreatorUserKey().equals(userKey) ||
            allowRevealDeck.equals(AllowRevealDeck.PARTICIPANTS) && Arrays.stream(scrumPokerSession.getVotes())
                .anyMatch(scrumPokerVote -> scrumPokerVote.getUserKey().equals(userKey));
        return !scrumPokerSession.isVisible() && scrumPokerSession.getVotes().length > 0 && userMayReveal;
    }

    /**
     * Returns that an agreement is reached when more than one vote is given, the votes are visible, there is no
     * non-numeric value and the minimum and maximum vote are equal.
     */
    private boolean agreementReached(ScrumPokerSession scrumPokerSession) {
        ScrumPokerVote[] votes = scrumPokerSession.getVotes();
        return scrumPokerSession.isVisible() &&
            votes.length > 1 &&
            getMaximumVote(votes).equals(getMinimumVote(votes)) &&
            stream(votes).allMatch(scrumPokerVote -> isNumber(scrumPokerVote.getVote()));
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
                scrumPokerSession.isVisible() ? vote.getVote() : "question",
                needToTalk(vote.getVote(), scrumPokerSession),
                needABreak(vote.getVote(), scrumPokerSession)))
            .collect(Collectors.toList());
    }

    /**
     * Returns whether the current vote needs a break by having selected the coffee card.
     */
    private boolean needABreak(String vote, ScrumPokerSession scrumPokerSession) {
        return scrumPokerSession.isVisible() && ScrumPokerCard.COFFEE.getName().equals(vote);
    }

    /**
     * Returns whether the current vote needs to talk or not depending on the status of the Scrum Poker session and the
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
    private List<BoundedVoteEntity> boundedVotes(ScrumPokerVote[] votes) {
        Map<String, Long> voteDistribution = Arrays.stream(votes)
            .collect(Collectors.groupingBy(ScrumPokerVote::getVote, Collectors.counting()));
        return getDeck().stream()
            .map(scrumPokerCard -> createBoundedVote(scrumPokerCard, voteDistribution))
            .filter(boundedVoteEntity -> nonNumericValueWithVotes(boundedVoteEntity) ||
                numericValueWithVotesInBoundary(boundedVoteEntity, votes))
            .collect(Collectors.toList());
    }

    /**
     * Creates a bounded vote from the distribution of votes and the given card.
     */
    private BoundedVoteEntity createBoundedVote(ScrumPokerCard scrumPokerCard, Map<String, Long> distribution) {
        String value = scrumPokerCard.getName();
        return new BoundedVoteEntity(value, distribution.getOrDefault(value, 0L), isNumber(value));
    }

    /**
     * Verifies that the given bounded vote is numeric and inside the range of the minimum and maximum of all votes.
     */
    private boolean numericValueWithVotesInBoundary(BoundedVoteEntity boundedVote, ScrumPokerVote[] votes) {
        return isNumber(boundedVote.getValue()) && !numericValues(votes).isEmpty() &&
            Integer.parseInt(boundedVote.getValue()) >= getMinimumVote(votes) &&
            Integer.parseInt(boundedVote.getValue()) <= getMaximumVote(votes);
    }

    /**
     * Verifies that the given bounded vote is non-numeric and has minimum one vote.
     */
    private boolean nonNumericValueWithVotes(BoundedVoteEntity boundedVote) {
        return !isNumber(boundedVote.getValue()) && boundedVote.getCount() > 0;
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
        if (key == null) {
            return null;
        }
        ApplicationUser user = userManager.getUserByKey(key);
        return user != null ? user.getDisplayName() : key;
    }

}
