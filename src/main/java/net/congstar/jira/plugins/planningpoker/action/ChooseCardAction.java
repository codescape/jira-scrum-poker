package net.congstar.jira.plugins.planningpoker.action;

import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import net.congstar.jira.plugins.planningpoker.data.PlanningPokerStorage;

/**
 * Persist a chosen card of a user for a given issue into the plugin storage and redirect to the planning poker page.
 */
public class ChooseCardAction extends JiraWebActionSupport {

	private static final long serialVersionUID = 1L;

	private final PlanningPokerStorage planningPokerStorage;

    private final JiraAuthenticationContext jiraAuthenticationContext;

    public ChooseCardAction(PlanningPokerStorage planningPokerStorage, JiraAuthenticationContext jiraAuthenticationContext) {
        this.planningPokerStorage = planningPokerStorage;
        this.jiraAuthenticationContext = jiraAuthenticationContext;
    }

    @Override
    protected String doExecute() throws Exception {
        String chosenCard = request.getParameter("chosenCard");
        String issueKey = request.getParameter("issueKey");
        ApplicationUser user = jiraAuthenticationContext.getUser();

        planningPokerStorage.update(issueKey, user.getKey(), chosenCard);

        return getRedirect("/secure/startPlanningPoker.jspa?issueKey=" + issueKey);
    }

}
