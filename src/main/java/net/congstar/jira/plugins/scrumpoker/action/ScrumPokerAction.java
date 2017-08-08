package net.congstar.jira.plugins.scrumpoker.action;

import com.atlassian.jira.web.action.JiraWebActionSupport;

public abstract class ScrumPokerAction extends JiraWebActionSupport {

    private static final long serialVersionUID = 1L;

    /**
     * Name of the parameter containing the issue key.
     */
    protected static final String PARAM_ISSUE_KEY = "issueKey";

    /**
     * Open the Scrum poker view for the given issue key.
     *
     * @param issueKey issue key
     * @return redirect to Scrum poker view
     */
    protected String openScrumPokerForIssue(String issueKey) {
        return getRedirect("/secure/scrumPokerStart.jspa?issueKey=" + issueKey);
    }

    /**
     * Open the detail view for the given issue key.
     *
     * @param issueKey issue key
     * @return redirect to detail url
     */
    protected String openIssue(String issueKey) {
        return getRedirect("/browse/" + issueKey);
    }

}
