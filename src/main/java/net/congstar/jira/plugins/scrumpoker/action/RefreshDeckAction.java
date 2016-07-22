package net.congstar.jira.plugins.scrumpoker.action;

/**
 * Refresh the deck for a given issue.
 */
public class RefreshDeckAction extends ScrumPokerAction {

    private static final long serialVersionUID = 1L;

    public RefreshDeckAction() {
    }

    @Override
    protected String doExecute() throws Exception {
        String issueKey = getHttpRequest().getParameter(PARAM_ISSUE_KEY);

        return getRedirect("/secure/scrumPokerStart.jspa?issueKey=" + issueKey);
    }

}
