package net.congstar.jira.plugins.scrumpoker.action;

import net.congstar.jira.plugins.scrumpoker.data.ScrumPokerStorage;

/**
 * Persist a chosen card of a user for a given issue into the plugin storage and redirect to the Scrum poker page.
 */
public class ChooseCardAction extends ScrumPokerAction {

    private static final long serialVersionUID = 1L;

    private static final String PARAM_CHOSEN_CARD = "chosenCard";

    private final ScrumPokerStorage scrumPokerStorage;

    public ChooseCardAction(ScrumPokerStorage scrumPokerStorage) {
        this.scrumPokerStorage = scrumPokerStorage;
    }

    @Override
    protected String doExecute() throws Exception {
        String issueKey = getHttpRequest().getParameter(PARAM_ISSUE_KEY);
        String chosenCard = getHttpRequest().getParameter(PARAM_CHOSEN_CARD);
        scrumPokerStorage.sessionForIssue(issueKey, currentUserKey()).updateCard(currentUserKey(), chosenCard);
        return openScrumPokerForIssue(issueKey);
    }

}
