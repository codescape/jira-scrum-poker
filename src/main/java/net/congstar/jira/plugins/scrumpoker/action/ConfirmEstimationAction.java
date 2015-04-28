package net.congstar.jira.plugins.scrumpoker.action;

import com.atlassian.jira.web.action.JiraWebActionSupport;
import net.congstar.jira.plugins.scrumpoker.data.StoryPointFieldSupport;

public class ConfirmEstimationAction extends JiraWebActionSupport {

    private final StoryPointFieldSupport storyPointFieldSupport;

    public ConfirmEstimationAction(StoryPointFieldSupport storyPointFieldSupport) {
        this.storyPointFieldSupport = storyPointFieldSupport;
    }

    @Override
    protected String doExecute() throws Exception {
        String issueKey = getHttpRequest().getParameter("issueKey");
        String finalVote = getHttpRequest().getParameter("finalVote");

        storyPointFieldSupport.save(issueKey, new Double(finalVote));

        return getRedirect("/browse/" + issueKey);
    }

}
