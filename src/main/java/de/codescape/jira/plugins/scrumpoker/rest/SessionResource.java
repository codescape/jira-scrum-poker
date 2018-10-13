package de.codescape.jira.plugins.scrumpoker.rest;

import com.atlassian.jira.security.JiraAuthenticationContext;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerSession;
import de.codescape.jira.plugins.scrumpoker.rest.entities.SessionEntity;
import de.codescape.jira.plugins.scrumpoker.service.EstimationFieldService;
import de.codescape.jira.plugins.scrumpoker.service.ScrumPokerSessionService;
import de.codescape.jira.plugins.scrumpoker.service.SessionEntityTransformer;
import de.codescape.jira.plugins.scrumpoker.service.SessionReferenceTransformer;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

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
    private final SessionReferenceTransformer sessionReferenceTransformer;

    @Autowired
    public SessionResource(EstimationFieldService estimationFieldService,
                           JiraAuthenticationContext jiraAuthenticationContext,
                           ScrumPokerSessionService scrumPokerSessionService,
                           SessionEntityTransformer sessionEntityTransformer,
                           SessionReferenceTransformer sessionReferenceTransformer) {
        this.estimationFieldService = estimationFieldService;
        this.jiraAuthenticationContext = jiraAuthenticationContext;
        this.sessionEntityTransformer = sessionEntityTransformer;
        this.scrumPokerSessionService = scrumPokerSessionService;
        this.sessionReferenceTransformer = sessionReferenceTransformer;
    }

    /**
     * Retrieve a Scrum Poker session to display in the frontend.
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{issueKey}")
    public Response getSession(@PathParam("issueKey") String issueKey) {
        ScrumPokerSession scrumPokerSession = scrumPokerSessionService.byIssueKey(issueKey, currentUser());
        SessionEntity response = sessionEntityTransformer.build(scrumPokerSession, currentUser());
        return Response.ok(response).build();
    }

    /**
     * Update a card for the logged in user.
     */
    @POST
    @Path("/{issueKey}/card/{card}")
    public Response updateCard(@PathParam("issueKey") String issueKey, @PathParam("card") String card) {
        scrumPokerSessionService.addVote(issueKey, currentUser(), card);
        return Response.ok().build();
    }

    /**
     * Reveal all cards of the Scrum Poker session.
     */
    @POST
    @Path("/{issueKey}/reveal")
    public Response revealCards(@PathParam("issueKey") String issueKey) {
        scrumPokerSessionService.reveal(issueKey, currentUser());
        return Response.ok().build();
    }

    /**
     * Cancel the Scrum Poker session.
     */
    @POST
    @Path("/{issueKey}/cancel")
    public Response cancelSession(@PathParam("issueKey") String issueKey) {
        scrumPokerSessionService.cancel(issueKey, currentUser());
        return Response.ok().build();
    }

    /**
     * Reset the Scrum Poker session.
     */
    @POST
    @Path("/{issueKey}/reset")
    public Response resetSession(@PathParam("issueKey") String issueKey) {
        scrumPokerSessionService.reset(issueKey, currentUser());
        return Response.ok().build();
    }

    /**
     * Confirm the estimation of the Scrum Poker session.
     */
    @POST
    @Path("/{issueKey}/confirm/{estimation}")
    public Response confirmEstimation(@PathParam("issueKey") String issueKey,
                                      @PathParam("estimation") Integer estimation) {
        scrumPokerSessionService.confirm(issueKey, currentUser(), estimation);
        if (estimationFieldService.save(issueKey, estimation)) {
            return Response.ok().build();
        } else {
            return Response.serverError().build();
        }
    }

    /**
     * Retrieve reference issues for the given estimation.
     */
    @GET
    @Path("/reference/{estimation}")
    public Response getReferences(@PathParam("estimation") Integer estimation) {
        List<ScrumPokerSession> references = scrumPokerSessionService.references(currentUser(), estimation);
        return Response.ok(sessionReferenceTransformer.build(references, estimation)).build();
    }

    private String currentUser() {
        return jiraAuthenticationContext.getLoggedInUser().getKey();
    }

}
