package net.congstar.jira.plugins.planningpoker.action;

import com.atlassian.jira.web.action.JiraWebActionSupport;
import net.congstar.jira.plugins.planningpoker.data.PlanningPokerStorage;

/**
 * Reveal all cards that are currently hidden for a specific issue.
 */
public class RevealDeckAction extends JiraWebActionSupport {

    private static final long serialVersionUID = 1L;

    private final PlanningPokerStorage planningPokerStorage;

    public RevealDeckAction(PlanningPokerStorage planningPokerStorage) {
        this.planningPokerStorage = planningPokerStorage;
    }

    @Override
    protected String doExecute() throws Exception {
        String issueKey = request.getParameter("issueKey");

        planningPokerStorage.revealDeck(issueKey);

        return getRedirect("/secure/startPlanningPoker.jspa?issueKey=" + issueKey);
    }

}
