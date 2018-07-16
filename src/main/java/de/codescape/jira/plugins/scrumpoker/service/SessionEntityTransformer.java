package de.codescape.jira.plugins.scrumpoker.service;

import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.user.util.UserManager;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerSession;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerVote;
import de.codescape.jira.plugins.scrumpoker.model.ScrumPokerCard;
import de.codescape.jira.plugins.scrumpoker.rest.entities.CardEntity;
import de.codescape.jira.plugins.scrumpoker.rest.entities.SessionEntity;
import de.codescape.jira.plugins.scrumpoker.rest.entities.VoteEntity;
import org.apache.commons.lang3.math.NumberUtils;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static de.codescape.jira.plugins.scrumpoker.model.ScrumPokerCard.*;
import static java.util.Arrays.*;
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
            .withBoundedVotes(createBoundedVotes(scrumPokerSession.getVotes()))
            .withVotes(createVotes(scrumPokerSession, userKey))
            .withCards(createCards(cardForUser(scrumPokerSession.getVotes(), userKey)))
            .withAllowReveal(!scrumPokerSession.isVisible() && scrumPokerSession.getVotes().length > 0)
            .withAllowReset(scrumPokerSession.isVisible() && scrumPokerSession.getVotes().length > 0)
            .withAgreementReached(createAgreementReached(scrumPokerSession))
            .withCreator(displayNameForUser(scrumPokerSession.getCreatorUserKey()))
            .withCreateDate(scrumPokerSession.getCreateDate());
    }

    private String cardForUser(ScrumPokerVote[] votes, String userKey) {
        Optional<ScrumPokerVote> match = stream(votes)
            .filter(vote -> vote.getUserKey().equals(userKey))
            .findFirst();
        return match.map(ScrumPokerVote::getVote).orElse(null);
    }

    private boolean createAgreementReached(ScrumPokerSession scrumPokerSession) {
        ScrumPokerVote[] votes = scrumPokerSession.getVotes();
        return scrumPokerSession.isVisible() &&
            votes.length > 1 &&
            getMaximumVote(votes).equals(getMinimumVote(votes)) &&
            stream(votes).noneMatch(scrumPokerVote -> scrumPokerVote.getVote().equals(QUESTION_MARK.getName()));
    }

    private List<CardEntity> createCards(String chosenValue) {
        return getDeck().stream()
            .map(card -> new CardEntity(card.getName(), card.getName().equals(chosenValue)))
            .collect(Collectors.toList());
    }

    private List<VoteEntity> createVotes(ScrumPokerSession scrumPokerSession, String userKey) {
        return stream(scrumPokerSession.getVotes())
            .map(vote -> new VoteEntity(
                displayNameForUser(vote.getUserKey()),
                scrumPokerSession.isVisible() ? vote.getVote() : "?",
                needToTalk(vote.getVote(), scrumPokerSession)))
            .collect(Collectors.toList());
    }

    private boolean needToTalk(String card, ScrumPokerSession scrumPokerSession) {
        if (!scrumPokerSession.isVisible())
            return false;
        if (createAgreementReached(scrumPokerSession))
            return false;
        if (scrumPokerSession.getVotes().length == 1)
            return false;
        if (!isNumber(card))
            return true;
        Integer current = Integer.valueOf(card);
        return current.equals(getMaximumVote(scrumPokerSession.getVotes()))
            || current.equals(getMinimumVote(scrumPokerSession.getVotes()));
    }

    private List<Integer> createBoundedVotes(ScrumPokerVote[] votes) {
        return getDeck().stream()
            .filter(scrumPokerCard -> isNumber(scrumPokerCard.getName()))
            .map(scrumPokerCard -> Integer.valueOf(scrumPokerCard.getName()))
            .filter(value -> value >= getMinimumVote(votes) && value <= getMaximumVote(votes))
            .collect(Collectors.toList());
    }

    private Integer getMinimumVote(ScrumPokerVote[] votes) {
        return numericValues(votes).stream().reduce(Integer::min).orElse(0);
    }

    private Integer getMaximumVote(ScrumPokerVote[] votes) {
        return numericValues(votes).stream().reduce(Integer::max).orElse(100);
    }

    private List<Integer> numericValues(ScrumPokerVote[] votes) {
        return stream(votes).map(ScrumPokerVote::getVote)
            .filter(NumberUtils::isNumber)
            .map(Integer::valueOf)
            .collect(Collectors.toList());
    }

    private String displayNameForUser(String key) {
        ApplicationUser user = userManager.getUserByKey(key);
        return user != null ? user.getDisplayName() : key;
    }

}
