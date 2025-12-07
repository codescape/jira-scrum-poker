package de.codescape.jira.plugins.scrumpoker.rest;

import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerSession;
import de.codescape.jira.plugins.scrumpoker.helper.ScrumPokerPermissions;
import de.codescape.jira.plugins.scrumpoker.model.AllowRevealDeck;
import de.codescape.jira.plugins.scrumpoker.model.Card;
import de.codescape.jira.plugins.scrumpoker.rest.entities.SessionEntity;
import de.codescape.jira.plugins.scrumpoker.rest.mapper.SessionEntityMapper;
import de.codescape.jira.plugins.scrumpoker.rest.mapper.SessionReferenceMapper;
import de.codescape.jira.plugins.scrumpoker.service.CardSetService;
import de.codescape.jira.plugins.scrumpoker.service.EstimateFieldService;
import de.codescape.jira.plugins.scrumpoker.service.GlobalSettingsService;
import de.codescape.jira.plugins.scrumpoker.service.ScrumPokerSessionService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Arrays;
import java.util.List;

/**
 * Implementation of the REST endpoint allowing to retrieve and modify a Scrum Poker session as a logged-in user in the
 * frontend.
 */
@Path("/session")
public class SessionResource {

    private final EstimateFieldService estimateFieldService;
    private final JiraAuthenticationContext jiraAuthenticationContext;
    private final ScrumPokerSessionService scrumPokerSessionService;
    private final SessionEntityMapper sessionEntityMapper;
    private final SessionReferenceMapper sessionReferenceMapper;
    private final ScrumPokerPermissions scrumPokerPermissions;
    private final CardSetService cardSetService;
    private final GlobalSettingsService globalSettingsService;

    @Inject
    public SessionResource(@ComponentImport JiraAuthenticationContext jiraAuthenticationContext,
                           EstimateFieldService estimateFieldService,
                           ScrumPokerSessionService scrumPokerSessionService,
                           SessionEntityMapper sessionEntityMapper,
                           SessionReferenceMapper sessionReferenceMapper,
                           ScrumPokerPermissions scrumPokerPermissions,
                           CardSetService cardSetService,
                           GlobalSettingsService globalSettingsService) {
        this.jiraAuthenticationContext = jiraAuthenticationContext;
        this.estimateFieldService = estimateFieldService;
        this.scrumPokerSessionService = scrumPokerSessionService;
        this.sessionEntityMapper = sessionEntityMapper;
        this.sessionReferenceMapper = sessionReferenceMapper;
        this.scrumPokerPermissions = scrumPokerPermissions;
        this.cardSetService = cardSetService;
        this.globalSettingsService = globalSettingsService;
    }

    /**
     * Retrieve a Scrum Poker session to display in the frontend.
     * <ul>
     *  <li>user is not allowed to see the issue -> FORBIDDEN</li>
     * </ul>
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{issueKey}")
    public Response getSession(@PathParam("issueKey") String issueKey) {
        // return forbidden for issues the user cannot see
        if (!scrumPokerPermissions.currentUserIsAllowedToSeeIssue(issueKey)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        // return the session data
        ScrumPokerSession scrumPokerSession = scrumPokerSessionService.byIssueKey(issueKey, currentUser());
        SessionEntity response = sessionEntityMapper.build(scrumPokerSession, currentUser());
        return Response.ok(response).build();
    }

    /**
     * Update a card for the logged-in user.
     * <ul>
     *  <li>user is not allowed to see the issue -> FORBIDDEN</li>
     *  <li>no active session exists -> NOT FOUND</li>
     *  <li>invalid card submitted -> FORBIDDEN</li>
     * </ul>
     */
    @POST
    @Path("/{issueKey}/card/{card}")
    public Response updateCard(@PathParam("issueKey") String issueKey, @PathParam("card") String card) {
        // return forbidden for issues the user cannot see
        if (!scrumPokerPermissions.currentUserIsAllowedToSeeIssue(issueKey)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        // return not-found for no missing session
        if (!scrumPokerSessionService.hasActiveSession(issueKey)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        // return forbidden if an invalid card is selected
        ScrumPokerSession scrumPokerSession = scrumPokerSessionService.byIssueKey(issueKey, currentUser());
        if (cardSetService.getCardSet(scrumPokerSession).stream().noneMatch(c -> c.getValue().equals(card))) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        // accept the vote
        scrumPokerSessionService.addVote(issueKey, currentUser(), card);
        return Response.ok().build();
    }

    /**
     * Reveal all cards of the Scrum Poker session.
     * <ul>
     *  <li>user is not allowed to see the issue -> FORBIDDEN</li>
     *  <li>no active session exists -> NOT FOUND</li>
     *  <li>cards are already revealed -> FORBIDDEN</li>
     *  <li>no votes exist to be revealed -> FORBIDDEN</li>
     *  <li>current user is not allowed to reveal the cards -> FORBIDDEN</li>
     * </ul>
     */
    @POST
    @Path("/{issueKey}/reveal")
    public Response revealCards(@PathParam("issueKey") String issueKey) {
        // return forbidden for issues the user cannot see
        if (!scrumPokerPermissions.currentUserIsAllowedToSeeIssue(issueKey)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        // return not-found for no missing session
        if (!scrumPokerSessionService.hasActiveSession(issueKey)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        // return forbidden if cards are already revealed or no votes exist at all
        ScrumPokerSession scrumPokerSession = scrumPokerSessionService.byIssueKey(issueKey, currentUser());
        if (scrumPokerSession.isVisible() || scrumPokerSession.getVotes().length == 0) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        // return forbidden if settings do not allow the current user to reveal the deck
        AllowRevealDeck allowRevealDeck = globalSettingsService.load().getAllowRevealDeck();
        if (!(allowRevealDeck.equals(AllowRevealDeck.EVERYONE) ||
            allowRevealDeck.equals(AllowRevealDeck.CREATOR) && scrumPokerSession.getCreatorUserKey().equals(currentUser()) ||
            allowRevealDeck.equals(AllowRevealDeck.PARTICIPANTS) && Arrays.stream(scrumPokerSession.getVotes())
                .anyMatch(vote -> vote.getUserKey().equals(currentUser())))) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        // reveal the cards
        scrumPokerSessionService.reveal(issueKey, currentUser());
        return Response.ok().build();
    }

    /**
     * Cancel the Scrum Poker session.
     * <ul>
     *  <li>user is not allowed to see the issue -> FORBIDDEN</li>
     *  <li>no active session exists -> NOT FOUND</li>
     *  <li>user is not the creator of the session -> FORBIDDEN</li>
     * </ul>
     */
    @POST
    @Path("/{issueKey}/cancel")
    public Response cancelSession(@PathParam("issueKey") String issueKey) {
        // return forbidden for issues the user cannot see
        if (!scrumPokerPermissions.currentUserIsAllowedToSeeIssue(issueKey)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        // return not-found for missing session
        if (!scrumPokerSessionService.hasActiveSession(issueKey)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        // return forbidden if current user to cancel the session is not the creator
        ScrumPokerSession scrumPokerSession = scrumPokerSessionService.byIssueKey(issueKey, currentUser());
        if (scrumPokerSession.getCreatorUserKey() != null && !scrumPokerSession.getCreatorUserKey().equals(currentUser())) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        // cancel the session‚
        scrumPokerSessionService.cancel(issueKey, currentUser());
        return Response.ok().build();
    }

    /**
     * Reset the Scrum Poker session.
     * <ul>
     *  <li>user is not allowed to see the issue -> FORBIDDEN</li>
     *  <li>no active session exists -> NOT FOUND</li>
     *  <li>session is not visible or has no votes -> FORBIDDEN</li>
     * </ul>
     */
    @POST
    @Path("/{issueKey}/reset")
    public Response resetSession(@PathParam("issueKey") String issueKey) {
        // return forbidden for issues the user cannot see
        if (!scrumPokerPermissions.currentUserIsAllowedToSeeIssue(issueKey)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        // return not-found for missing session
        if (!scrumPokerSessionService.hasActiveSession(issueKey)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        // return forbidden if session reset is not allowed
        ScrumPokerSession scrumPokerSession = scrumPokerSessionService.byIssueKey(issueKey, currentUser());
        if (!(scrumPokerSession.isVisible() && scrumPokerSession.getVotes().length > 0)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        // reset the session
        scrumPokerSessionService.reset(issueKey, currentUser());
        return Response.ok().build();
    }

    /**
     * Confirm the estimate of the Scrum Poker session.
     * <ul>
     *  <li>user is not allowed to see the issue -> FORBIDDEN</li>
     *  <li>no active session exists -> NOT FOUND</li>
     *  <li>cards are not revealed -> FORBIDDEN</li>
     *  <li>session is cancelled -> FORBIDDEN</li>
     *  <li>estimate is already confirmed -> FORBIDDEN</li>
     *  <li>estimate is not assignable -> FORBIDDEN</li>
     *  <li>estimate does not belong to the card set -> FORBIDDEN</li>
     *  <li>user is required but missing write permission to the issue -> FORBIDDEN</li>
     * </ul>
     */
    @POST
    @Path("/{issueKey}/confirm/{estimate}")
    public Response confirmEstimation(@PathParam("issueKey") String issueKey,
                                      @PathParam("estimate") String estimate) {
        // return forbidden for issues the user cannot see
        if (!scrumPokerPermissions.currentUserIsAllowedToSeeIssue(issueKey)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        // return not-found for missing session
        if (!scrumPokerSessionService.hasActiveSession(issueKey)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        // return forbidden if cards are not revealed, session is cancelled or estimate is already confirmed
        ScrumPokerSession scrumPokerSession = scrumPokerSessionService.byIssueKey(issueKey, currentUser());
        if (!scrumPokerSession.isVisible() || scrumPokerSession.isCancelled() || scrumPokerSession.getConfirmedEstimate() != null) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        // return forbidden if an invalid card or a non-assignable card is selected
        if (cardSetService.getCardSet(scrumPokerSession).stream().filter(Card::isAssignable).noneMatch(c -> c.getValue().equals(estimate))) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        // return forbidden if user is required but missing write permission to the issue
        if (globalSettingsService.load().isCheckPermissionToSaveEstimate() &&
            !scrumPokerPermissions.currentUserIsAllowedToEditIssue(issueKey)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        // confirm the estimate
        scrumPokerSessionService.confirm(issueKey, currentUser(), estimate);
        if (estimateFieldService.save(issueKey, estimate)) {
            return Response.ok().build();
        } else {
            return Response.serverError().build();
        }
    }

    /**
     * Retrieve reference issues for the given estimate.
     */
    @GET
    @Path("/reference/{estimate}")
    public Response getReferences(@PathParam("estimate") String estimate) {
        List<ScrumPokerSession> references = scrumPokerSessionService.references(currentUser(), estimate);
        return Response.ok(sessionReferenceMapper.build(references, estimate)).build();
    }

    /* helper functions */

    private String currentUser() {
        return jiraAuthenticationContext.getLoggedInUser().getKey();
    }

}
