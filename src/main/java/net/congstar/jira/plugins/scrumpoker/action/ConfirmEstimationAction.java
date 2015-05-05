package net.congstar.jira.plugins.scrumpoker.action;

import net.congstar.jira.plugins.scrumpoker.data.StoryPointFieldSupport;

public class ConfirmEstimationAction extends ScrumPokerAction {

    private static final long serialVersionUID = 1L;

    private final StoryPointFieldSupport storyPointFieldSupport;

    public ConfirmEstimationAction(StoryPointFieldSupport storyPointFieldSupport) {
        this.storyPointFieldSupport = storyPointFieldSupport;
    }

    @Override
    protected String doExecute() throws Exception {
        String issueKey = getHttpRequest().getParameter(PARAM_ISSUE_KEY);
        String finalVote = getHttpRequest().getParameter("finalVote");

        storyPointFieldSupport.save(issueKey, new Double(finalVote));

        String returnUrl = (String) getHttpSession().getAttribute(PARAM_RETURN_URL);
        getHttpSession().removeAttribute(PARAM_RETURN_URL);

        return getRedirect(returnUrl);
    }

}
