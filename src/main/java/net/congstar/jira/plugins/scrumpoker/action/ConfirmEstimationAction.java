package net.congstar.jira.plugins.scrumpoker.action;

import net.congstar.jira.plugins.scrumpoker.data.PlanningPokerStorage;
import net.congstar.jira.plugins.scrumpoker.data.StoryPointFieldSupport;

public class ConfirmEstimationAction extends ScrumPokerAction {

    private static final long serialVersionUID = 1L;

    private static final String PARAM_FINAL_VOTE = "finalVote";

    private final StoryPointFieldSupport storyPointFieldSupport;

    private final PlanningPokerStorage planningPokerStorage;

    public ConfirmEstimationAction(StoryPointFieldSupport storyPointFieldSupport,
                                   PlanningPokerStorage planningPokerStorage) {
        this.storyPointFieldSupport = storyPointFieldSupport;
        this.planningPokerStorage = planningPokerStorage;
    }

    @Override
    protected String doExecute() throws Exception {
        String issueKey = getHttpRequest().getParameter(PARAM_ISSUE_KEY);
        String finalVote = getHttpRequest().getParameter(PARAM_FINAL_VOTE);
        storyPointFieldSupport.save(issueKey, new Double(finalVote));
        planningPokerStorage.sessionForIssue(issueKey).confirm(finalVote);
        return openIssue(issueKey);
    }

}
