package de.codescape.jira.plugins.scrumpoker.rest;

import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.user.util.UserManager;
import de.codescape.jira.plugins.scrumpoker.model.ScrumPokerCard;
import de.codescape.jira.plugins.scrumpoker.model.ScrumPokerSession;
import de.codescape.jira.plugins.scrumpoker.persistence.ScrumPokerStorage;
import de.codescape.jira.plugins.scrumpoker.persistence.StoryPointFieldSupport;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang.math.NumberUtils.isNumber;

/**
 * Implementation of the REST endpoint allowing to retrieve and modify a Scrum Poker session as a logged in user in the
 * frontend.
 */
@Path("/session")
public class SessionResource {

    private final ScrumPokerStorage scrumPokerStorage;
    private final StoryPointFieldSupport storyPointFieldSupport;
    private final JiraAuthenticationContext jiraAuthenticationContext;
    private final UserManager userManager;

    public SessionResource(ScrumPokerStorage scrumPokerStorage, StoryPointFieldSupport storyPointFieldSupport,
                           JiraAuthenticationContext jiraAuthenticationContext, UserManager userManager) {
        this.scrumPokerStorage = scrumPokerStorage;
        this.storyPointFieldSupport = storyPointFieldSupport;
        this.jiraAuthenticationContext = jiraAuthenticationContext;
        this.userManager = userManager;
    }

    /**
     * Retrieve a Scrum Poker session to display in the frontend.
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{issueKey}")
    public Response getSession(@PathParam("issueKey") String issueKey) {
        String userKey = jiraAuthenticationContext.getLoggedInUser().getKey();
        ScrumPokerSession scrumPokerSession = scrumPokerStorage.sessionForIssue(issueKey, userKey);
        String chosenValue = scrumPokerSession.getCards().get(userKey);
        ScrumPokerSessionRest response = createSession(scrumPokerSession, chosenValue);
        return Response.ok(response).build();
    }

    /**
     * Update a card for the logged in user.
     */
    @POST
    @Path("/{issueKey}/card/{card}")
    public Response updateCard(@PathParam("issueKey") String issueKey, @PathParam("card") String card) {
        String userKey = jiraAuthenticationContext.getLoggedInUser().getKey();
        scrumPokerStorage.sessionForIssue(issueKey, userKey).updateCard(userKey, card);
        return Response.ok().build();
    }

    /**
     * Reveal all cards of the Scrum Poker session.
     */
    @POST
    @Path("/{issueKey}/reveal")
    public Response revealCards(@PathParam("issueKey") String issueKey) {
        String userKey = jiraAuthenticationContext.getLoggedInUser().getKey();
        scrumPokerStorage.sessionForIssue(issueKey, userKey).revealDeck();
        return Response.ok().build();
    }

    /**
     * Reset the Scrum Poker session.
     */
    @POST
    @Path("/{issueKey}/reset")
    public Response resetSession(@PathParam("issueKey") String issueKey) {
        String userKey = jiraAuthenticationContext.getLoggedInUser().getKey();
        scrumPokerStorage.sessionForIssue(issueKey, userKey).resetDeck();
        return Response.ok().build();
    }

    /**
     * Confirm the estimation of the Scrum Poker session.
     */
    @POST
    @Path("/{issueKey}/confirm/{estimation}")
    public Response confirmEstimation(@PathParam("issueKey") String issueKey, @PathParam("estimation") Integer estimation) {
        String userKey = jiraAuthenticationContext.getLoggedInUser().getKey();
        scrumPokerStorage.sessionForIssue(issueKey, userKey).confirm(estimation);
        storyPointFieldSupport.save(issueKey, estimation);
        return Response.ok().build();
    }

    private ScrumPokerSessionRest createSession(ScrumPokerSession scrumPokerSession, String chosenValue) {
        return new ScrumPokerSessionRest()
            .withIssueKey(scrumPokerSession.getIssueKey())
            .withCards(allCardsAndChosenCardMarkedAsSelected(chosenValue))
            .withConfirmedVote(scrumPokerSession.getConfirmedVote())
            .withVisible(scrumPokerSession.isVisible())
            .withBoundedVotes(scrumPokerSession.getBoundedVotes())
            .withAgreementReached(scrumPokerSession.isAgreementReached() && scrumPokerSession.isVisible())
            .withVotes(allVotesWithUsers(scrumPokerSession))
            .withAllowReset(scrumPokerSession.isVisible() && !scrumPokerSession.getCards().isEmpty())
            .withAllowReveal(!scrumPokerSession.isVisible() && !scrumPokerSession.getCards().isEmpty());
    }

    private List<VotesRest> allVotesWithUsers(ScrumPokerSession scrumPokerSession) {
        return scrumPokerSession.getCards().entrySet().stream()
            .map(card ->
                new VotesRest(
                    userName(card.getKey()),
                    scrumPokerSession.isVisible() ? card.getValue() : "?",
                    needToTalk(card.getValue(), scrumPokerSession)))
            .collect(Collectors.toList());
    }

    private List<CardRest> allCardsAndChosenCardMarkedAsSelected(String chosenValue) {
        return ScrumPokerCard.getDeck().stream()
            .map(card -> new CardRest(card.getName(), card.getName().equals(chosenValue)))
            .collect(Collectors.toList());
    }

    private boolean needToTalk(String vote, ScrumPokerSession scrumPokerSession) {
        return scrumPokerSession.isVisible() && !scrumPokerSession.isAgreementReached() && (!isNumber(vote) ||
            (isNumber(vote) && (Integer.valueOf(vote).equals(scrumPokerSession.getMinimumVote()) ||
                Integer.valueOf(vote).equals(scrumPokerSession.getMaximumVote()))));
    }

    private String userName(String entry) {
        ApplicationUser user = userManager.getUserByKey(entry);
        return user != null ? user.getDisplayName() : entry;
    }

}
