package net.congstar.jira.plugins.scrumpoker.action;

import com.atlassian.jira.web.action.JiraWebActionSupport;

public abstract class ScrumPokerAction extends JiraWebActionSupport {

    private static final long serialVersionUID = 1L;

    /**
     * Name of the parameter containing the URL to redirect after a poker session.
     */
    protected static final String PARAM_RETURN_URL = "returnUrl";

    /**
     * Name of the parameter containing the issue key.
     */
    protected static final String PARAM_ISSUE_KEY = "issueKey";

    /**
     * http header containing the referrer
     */
    protected static final String PARAM_REFERRER_HEADER = "referer";
}
