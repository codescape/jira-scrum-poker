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
import de.codescape.jira.plugins.scrumpoker.model.Card;
import de.codescape.jira.plugins.scrumpoker.rest.entities.*;
import de.codescape.jira.plugins.scrumpoker.service.CardSetService;
import de.codescape.jira.plugins.scrumpoker.service.GlobalSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static de.codescape.jira.plugins.scrumpoker.model.Card.*;
import static java.util.Arrays.stream;

/**
 * Service that allows to map a {@link ScrumPokerSession} to a {@link SessionEntity}. This service creates a model that
 * can be used and transferred as a REST resource and is optimized for a logic less templating mechanism.
 */
@Component
public class SessionEntityMapper {

    private final UserManager userManager;
    private final DateTimeFormatter dateTimeFormatter;
    private final GlobalSettingsService globalSettingsService;
    private final PermissionManager permissionManager;
    private final IssueManager issueManager;
    private final CardSetService cardSetService;

    @Autowired
    public SessionEntityMapper(@ComponentImport UserManager userManager,
                               @ComponentImport DateTimeFormatter dateTimeFormatter,
                               @ComponentImport PermissionManager permissionManager,
                               @ComponentImport IssueManager issueManager,
                               GlobalSettingsService globalSettingsService,
                               CardSetService cardSetService) {
        this.userManager = userManager;
        this.dateTimeFormatter = dateTimeFormatter;
        this.permissionManager = permissionManager;
        this.issueManager = issueManager;
        this.globalSettingsService = globalSettingsService;
        this.cardSetService = cardSetService;
    }

    /**
     * Map a given {@link ScrumPokerSession} to a {@link SessionEntity}.
     *
     * @param scrumPokerSession {@link ScrumPokerSession} to transform
     * @param userKey           key of the user
     * @return transformed {@link SessionEntity}
     */
    public SessionEntity build(ScrumPokerSession scrumPokerSession, String userKey) {
        List<Card> cardSet = cardSetService.getCardSet(scrumPokerSession);
        List<BoundedVoteEntity> boundedVotes = boundedVotes(scrumPokerSession, cardSet);
        return new SessionEntity()
            .withIssueKey(scrumPokerSession.getIssueKey())
            .withConfirmedEstimate(scrumPokerSession.getConfirmedEstimate())
            .withConfirmedDate(dateEntity(scrumPokerSession.getConfirmedDate()))
            .withConfirmedUser(displayNameForUser(scrumPokerSession.getConfirmedUserKey()))
            .withVisible(scrumPokerSession.isVisible())
            .withCancelled(scrumPokerSession.isCancelled())
            .withBoundedVotes(boundedVotes)
            .withVotes(votes(scrumPokerSession, boundedVotes))
            .withCards(cards(scrumPokerSession, cardSet, userKey))
            .withAllowReveal(allowReveal(scrumPokerSession, userKey))
            .withAllowReset(allowReset(scrumPokerSession))
            .withAllowCancel(allowCancel(scrumPokerSession, userKey))
            .withAllowConfirm(allowConfirm(scrumPokerSession, userKey))
            .withAgreementReached(agreementReached(scrumPokerSession))
            .withCreator(displayNameForUser(scrumPokerSession.getCreatorUserKey()))
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
            DateTimeFormatter formatterForUser = dateTimeFormatter.forLoggedInUser();
            displayValue = formatterForUser.withStyle(DateTimeStyle.RELATIVE).format(date);
            formattedDate = formatterForUser.withStyle(DateTimeStyle.COMPLETE).format(date);
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
        return !globalSettingsService.load().isCheckPermissionToSaveEstimate() ||
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
        // only hidden decks can be revealed
        if (scrumPokerSession.isVisible()) {
            return false;
        }
        // only decks with votes can be revealed
        if (scrumPokerSession.getVotes().length == 0) {
            return false;
        }
        // depending on configuration not all users are allowed to reveal the deck
        AllowRevealDeck allowRevealDeck = globalSettingsService.load().getAllowRevealDeck();
        return allowRevealDeck.equals(AllowRevealDeck.EVERYONE) ||
            allowRevealDeck.equals(AllowRevealDeck.CREATOR) && scrumPokerSession.getCreatorUserKey().equals(userKey) ||
            allowRevealDeck.equals(AllowRevealDeck.PARTICIPANTS) && Arrays.stream(scrumPokerSession.getVotes())
                .anyMatch(vote -> vote.getUserKey().equals(userKey));
    }

    /**
     * Returns that an agreement is reached when more than one vote is given, the votes are visible, there is only one
     * kind of a vote and this is not a special value.
     */
    private boolean agreementReached(ScrumPokerSession scrumPokerSession) {
        ScrumPokerVote[] votes = scrumPokerSession.getVotes();
        return scrumPokerSession.isVisible() &&
            votes.length > 1 &&
            stream(votes).noneMatch(scrumPokerVote -> isSpecialCardValue(scrumPokerVote.getVote())) &&
            stream(votes).collect(Collectors.groupingBy(ScrumPokerVote::getVote, Collectors.counting())).size() == 1;
    }

    /**
     * Returns the cards and if the current user has provided a vote highlights the selected card.
     */
    private List<CardEntity> cards(ScrumPokerSession scrumPokerSession, List<Card> cardSet, String userKey) {
        String chosenValue = cardForUser(scrumPokerSession.getVotes(), userKey);
        return cardSet.stream()
            .map(card -> new CardEntity(card.getValue(), card.getValue().equals(chosenValue), card.isAssignable()))
            .collect(Collectors.toList());
    }

    /**
     * Returns the value of the card the current user has selected or null if the user has not voted.
     */
    private String cardForUser(ScrumPokerVote[] votes, String userKey) {
        return stream(votes)
            .filter(vote -> vote.getUserKey().equals(userKey))
            .findFirst()
            .map(ScrumPokerVote::getVote)
            .orElse(null);
    }

    /**
     * Returns all votes and marks all votes that need to talk. If the deck is currently hidden only returns a question
     * mark instead of the correct values in order to not leak them to the clients.
     */
    private List<VoteEntity> votes(ScrumPokerSession scrumPokerSession, List<BoundedVoteEntity> boundedVotes) {
        return stream(scrumPokerSession.getVotes())
            .map(vote -> new VoteEntity(
                displayNameForUser(vote.getUserKey()),
                displayValue(vote, scrumPokerSession),
                needToTalk(vote, scrumPokerSession, boundedVotes),
                needABreak(vote, scrumPokerSession)))
            .collect(Collectors.toList());
    }

    /**
     * Returns the display value of a vote. As long as the cards are not revealed it always displays question marks.
     */
    private String displayValue(ScrumPokerVote vote, ScrumPokerSession scrumPokerSession) {
        return scrumPokerSession.isVisible() ? vote.getVote() : QUESTION_MARK.getValue();
    }

    /**
     * Returns whether the current vote needs a break by having selected the coffee card.
     */
    private boolean needABreak(ScrumPokerVote vote, ScrumPokerSession scrumPokerSession) {
        return scrumPokerSession.isVisible() && COFFEE_BREAK.getValue().equals(vote.getVote());
    }

    /**
     * Returns whether the current vote needs to talk or not depending on the status of the Scrum Poker session and the
     * other votes provided.
     */
    private boolean needToTalk(ScrumPokerVote vote, ScrumPokerSession scrumPokerSession, List<BoundedVoteEntity> boundedVotes) {
        if (!scrumPokerSession.isVisible())
            return false;
        if (agreementReached(scrumPokerSession))
            return false;
        if (scrumPokerSession.getVotes().length == 1)
            return false;
        if (!isAssignableToEstimateField(vote.getVote()))
            return true;
        return onBoundaries(vote.getVote(), boundedVotes);
    }

    /**
     * Returns whether the given vote is on the boundaries of the bounded votes while ignoring all special cards.
     */
    private boolean onBoundaries(String vote, List<BoundedVoteEntity> boundedVotes) {
        if (boundedVotes.isEmpty())
            return false;
        List<String> assignableVotes = boundedVotes.stream()
            .filter(BoundedVoteEntity::isAssignable)
            .map(BoundedVoteEntity::getValue)
            .collect(Collectors.toList());
        if (assignableVotes.isEmpty())
            return false;
        return vote.equals(assignableVotes.get(0)) || vote.equals(assignableVotes.get(assignableVotes.size() - 1));
    }

    /**
     * Returns the list of cards between and including the minimum and maximum vote.
     */
    private List<BoundedVoteEntity> boundedVotes(ScrumPokerSession scrumPokerSession, List<Card> cardSet) {
        Map<String, Long> voteDistribution = Arrays.stream(scrumPokerSession.getVotes())
            .collect(Collectors.groupingBy(ScrumPokerVote::getVote, Collectors.counting()));
        List<BoundedVoteEntity> cardSetWithVotes = cardSet.stream()
            .map(card -> createBoundedVote(card.getValue(), voteDistribution))
            .collect(Collectors.toList());
        return cardSetWithVotes.stream()
            .filter(boundedVote -> boundedVoteShouldDisplay(boundedVote, cardSetWithVotes))
            .collect(Collectors.toList());
    }

    /**
     * Returns whether a bounded vote should display or not. Every card with minimum one vote will be included and also
     * all values in between those cards with minimum one vote while not including special cards into this calculation.
     */
    private boolean boundedVoteShouldDisplay(BoundedVoteEntity boundedVote, List<BoundedVoteEntity> boundedVotes) {
        // Bounded vote should have a value itself...
        if (boundedVote.getCount() > 0) {
            return true;
        } else {
            // ...or should have a non special card before and after inside the list with minimum one vote
            int position = boundedVotes.indexOf(boundedVote);
            boolean hasVoteBefore = false;
            boolean hasVoteAfter = false;
            for (int i = 0; i < boundedVotes.size(); i++) {
                if (!isSpecialCardValue(boundedVotes.get(i).getValue())) {
                    hasVoteBefore = hasVoteBefore || (i < position && boundedVotes.get(i).getCount() > 0);
                    hasVoteAfter = hasVoteAfter || (i > position && boundedVotes.get(i).getCount() > 0);
                }
            }
            return hasVoteAfter && hasVoteBefore;
        }
    }

    /**
     * Creates a bounded vote from the distribution of votes and the given card.
     */
    private BoundedVoteEntity createBoundedVote(String value, Map<String, Long> distribution) {
        return new BoundedVoteEntity(value, distribution.getOrDefault(value, 0L),
            isAssignableToEstimateField(value));
    }

    /**
     * Returns the display name of a user associated by the given user key.
     */
    private String displayNameForUser(String key) {
        if (key == null) {
            return null;
        }
        ApplicationUser user = userManager.getUserByKey(key);
        return user != null ? user.getDisplayName() : key;
    }

    /**
     * Returns whether the given vote can be assigned to the estimate field.
     */
    private boolean isAssignableToEstimateField(String vote) {
        return !isSpecialCardValue(vote);
    }

}
