package net.congstar.jira.plugins.scrumpoker.action;

import net.congstar.jira.plugins.scrumpoker.data.PlanningPokerStorage;

public class ShowSessionsAction extends ScrumPokerAction {

    private static final long serialVersionUID = 1L;

    private final PlanningPokerStorage planningPokerStorage;

    public ShowSessionsAction(PlanningPokerStorage planningPokerStorage) {
        this.planningPokerStorage = planningPokerStorage;
    }

    @Override
    protected String doExecute() throws Exception {
        return "start";
    }

    public PlanningPokerStorage getPlanningPokerStorage() {
        return planningPokerStorage;
    }

}