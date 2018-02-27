package net.congstar.jira.plugins.scrumpoker.action;

import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.web.HttpServletVariables;
import net.congstar.jira.plugins.scrumpoker.data.ScrumPokerStorage;

/**
 * Persist a chosen card of a user for a given issue into the plugin storage and redirect to the Scrum poker page.
 */
public class ChooseCardAction extends ScrumPokerAction {

    private static final long serialVersionUID = 1L;
    static final String PARAM_CHOSEN_CARD = "chosenCard";

    private final ScrumPokerStorage scrumPokerStorage;
    private final JiraAuthenticationContext jiraAuthenticationContext;
    private final HttpServletVariables httpServletVariables;

    public ChooseCardAction(ScrumPokerStorage scrumPokerStorage, JiraAuthenticationContext jiraAuthenticationContext,
                            HttpServletVariables httpServletVariables) {
        this.scrumPokerStorage = scrumPokerStorage;
        this.jiraAuthenticationContext = jiraAuthenticationContext;
        this.httpServletVariables = httpServletVariables;
    }

    @Override
    protected String doExecute() throws Exception {
        String issueKey = httpServletVariables.getHttpRequest().getParameter(PARAM_ISSUE_KEY);
        String chosenCard = httpServletVariables.getHttpRequest().getParameter(PARAM_CHOSEN_CARD);
        String currentUserKey = jiraAuthenticationContext.getLoggedInUser().getKey();
        scrumPokerStorage.sessionForIssue(issueKey, currentUserKey).updateCard(currentUserKey, chosenCard);
        return openScrumPokerForIssue(issueKey);
    }

}
