package net.congstar.jira.plugins.planningpoker.action;

import com.atlassian.jira.web.action.JiraWebActionSupport;

/**
 * Refresh the deck for a given issue.
 */
public class RefreshDeckAction extends JiraWebActionSupport {

    private static final long serialVersionUID = 1L;

    public RefreshDeckAction() {
    }

    @Override
    protected String doExecute() throws Exception {
        String issueKey = request.getParameter("issueKey");

        return getRedirect("/secure/startPlanningPoker.jspa?issueKey=" + issueKey);
    }

}
