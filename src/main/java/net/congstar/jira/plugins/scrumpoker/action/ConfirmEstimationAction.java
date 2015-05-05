package net.congstar.jira.plugins.scrumpoker.action;

import com.atlassian.jira.web.action.JiraWebActionSupport;
import net.congstar.jira.plugins.scrumpoker.data.StoryPointFieldSupport;

import javax.servlet.http.HttpSession;

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

        HttpSession session = getHttpSession();
        String returnUrl = (String)session.getAttribute("returnUrl");
        session.removeAttribute("returnUrl");

        return getRedirect(returnUrl);
    }

}
