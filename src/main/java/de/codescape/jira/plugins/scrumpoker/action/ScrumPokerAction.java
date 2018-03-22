package de.codescape.jira.plugins.scrumpoker.action;

import com.atlassian.jira.web.action.JiraWebActionSupport;

/**
 * Base class providing convenience methods and variables for action implementations.
 */
abstract class ScrumPokerAction extends JiraWebActionSupport {

    private static final long serialVersionUID = 1L;

    /**
     * Name of the parameter containing the issue key.
     */
    static final String PARAM_ISSUE_KEY = "issueKey";

    /**
     * Open the Scrum poker view for the given issue key.
     *
     * @param issueKey issue key
     * @return redirect to Scrum poker view
     */
    String openScrumPokerForIssue(String issueKey) {
        return getRedirect("/secure/scrumPokerStart.jspa?issueKey=" + issueKey);
    }

    /**
     * Open the detail view for the given issue key.
     *
     * @param issueKey issue key
     * @return redirect to detail url
     */
    String openIssue(String issueKey) {
        return getRedirect("/browse/" + issueKey);
    }

    /**
     * Returns the key of the current user.
     *
     * @return key of current user
     */
    String currentUserKey() {
        return getLoggedInUser().getKey();
    }

}
