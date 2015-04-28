package net.congstar.jira.plugins.scrumpoker.action;

import com.atlassian.jira.web.action.JiraWebActionSupport;
import net.congstar.jira.plugins.scrumpoker.data.PlanningPokerStorage;

public class ResetDeckAction extends JiraWebActionSupport {

    private final PlanningPokerStorage planningPokerStorage;

    public ResetDeckAction(PlanningPokerStorage planningPokerStorage) {
        this.planningPokerStorage = planningPokerStorage;
    }

    @Override
    protected String doExecute() throws Exception {
        String issueKey = getHttpRequest().getParameter("issueKey");

        planningPokerStorage.resetDeck(issueKey);

        return getRedirect("/secure/startPlanningPoker.jspa?issueKey=" + issueKey);
    }

}
