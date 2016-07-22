package net.congstar.jira.plugins.scrumpoker.action;

import net.congstar.jira.plugins.scrumpoker.data.PlanningPokerStorage;

/**
 * Reveal all cards that are currently hidden for a specific issue.
 */
public class RevealDeckAction extends ScrumPokerAction {

    private static final long serialVersionUID = 1L;

    private final PlanningPokerStorage planningPokerStorage;

    public RevealDeckAction(PlanningPokerStorage planningPokerStorage) {
        this.planningPokerStorage = planningPokerStorage;
    }

    @Override
    protected String doExecute() throws Exception {
        String issueKey = getHttpRequest().getParameter(PARAM_ISSUE_KEY);

        planningPokerStorage.sessionForIssue(issueKey).revealDeck();

        return getRedirect("/secure/scrumPokerStart.jspa?issueKey=" + issueKey);
    }

}
