package de.codescape.jira.plugins.scrumpoker.rest;

import com.atlassian.jira.security.JiraAuthenticationContext;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerSession;
import de.codescape.jira.plugins.scrumpoker.rest.entities.SessionEntity;
import de.codescape.jira.plugins.scrumpoker.service.EstimationFieldService;
import de.codescape.jira.plugins.scrumpoker.service.ScrumPokerSessionService;
import de.codescape.jira.plugins.scrumpoker.service.SessionEntityTransformer;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Implementation of the REST endpoint allowing to retrieve and modify a Scrum Poker session as a logged in user in the
 * frontend.
 */
@Path("/session")
public class SessionResource {

    private final EstimationFieldService estimationFieldService;
    private final JiraAuthenticationContext jiraAuthenticationContext;
    private final ScrumPokerSessionService scrumPokerSessionService;
    private final SessionEntityTransformer sessionEntityTransformer;

    public SessionResource(EstimationFieldService estimationFieldService,
                           JiraAuthenticationContext jiraAuthenticationContext,
                           ScrumPokerSessionService scrumPokerSessionService,
                           SessionEntityTransformer sessionEntityTransformer) {
        this.estimationFieldService = estimationFieldService;
        this.jiraAuthenticationContext = jiraAuthenticationContext;
        this.sessionEntityTransformer = sessionEntityTransformer;
        this.scrumPokerSessionService = scrumPokerSessionService;
    }

    /**
     * Retrieve a Scrum Poker session to display in the frontend.
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{issueKey}")
    public Response getSession(@PathParam("issueKey") String issueKey) {
        String userKey = jiraAuthenticationContext.getLoggedInUser().getKey();
        ScrumPokerSession scrumPokerSession = scrumPokerSessionService.byIssueKey(issueKey, userKey);
        SessionEntity response = sessionEntityTransformer.build(scrumPokerSession, userKey);
        return Response.ok(response).build();
    }

    /**
     * Update a card for the logged in user.
     */
    @POST
    @Path("/{issueKey}/card/{card}")
    public Response updateCard(@PathParam("issueKey") String issueKey, @PathParam("card") String card) {
        String userKey = jiraAuthenticationContext.getLoggedInUser().getKey();
        scrumPokerSessionService.addVote(issueKey, userKey, card);
        return Response.ok().build();
    }

    /**
     * Reveal all cards of the Scrum Poker session.
     */
    @POST
    @Path("/{issueKey}/reveal")
    public Response revealCards(@PathParam("issueKey") String issueKey) {
        String userKey = jiraAuthenticationContext.getLoggedInUser().getKey();
        scrumPokerSessionService.reveal(issueKey, userKey);
        return Response.ok().build();
    }

    /**
     * Reset the Scrum Poker session.
     */
    @POST
    @Path("/{issueKey}/reset")
    public Response resetSession(@PathParam("issueKey") String issueKey) {
        String userKey = jiraAuthenticationContext.getLoggedInUser().getKey();
        scrumPokerSessionService.reset(issueKey, userKey);
        return Response.ok().build();
    }

    /**
     * Confirm the estimation of the Scrum Poker session.
     */
    @POST
    @Path("/{issueKey}/confirm/{estimation}")
    public Response confirmEstimation(@PathParam("issueKey") String issueKey,
                                      @PathParam("estimation") Integer estimation) {
        String userKey = jiraAuthenticationContext.getLoggedInUser().getKey();
        scrumPokerSessionService.confirm(issueKey, userKey, estimation);
        estimationFieldService.save(issueKey, estimation);
        return Response.ok().build();
    }

}
