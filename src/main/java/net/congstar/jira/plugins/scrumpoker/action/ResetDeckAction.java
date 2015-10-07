package net.congstar.jira.plugins.scrumpoker.action;

import net.congstar.jira.plugins.scrumpoker.data.PlanningPokerStorage;

public class ResetDeckAction extends ScrumPokerAction {

    private static final long serialVersionUID = 1L;

    private final PlanningPokerStorage planningPokerStorage;

    public ResetDeckAction(PlanningPokerStorage planningPokerStorage) {
        this.planningPokerStorage = planningPokerStorage;
    }

    @Override
    protected String doExecute() throws Exception {
        String issueKey = getHttpRequest().getParameter(PARAM_ISSUE_KEY);

        planningPokerStorage.sessionForIssue(issueKey).resetDeck();

        return getRedirect("/secure/startPlanningPoker.jspa?issueKey=" + issueKey);
    }

}
