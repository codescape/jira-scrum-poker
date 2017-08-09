package net.congstar.jira.plugins.scrumpoker.action;

import net.congstar.jira.plugins.scrumpoker.data.ScrumPokerStorage;
import net.congstar.jira.plugins.scrumpoker.model.ScrumPokerSession;

import java.util.List;

public class ShowSessionsAction extends ScrumPokerAction {

    private static final long serialVersionUID = 1L;

    private final ScrumPokerStorage scrumPokerStorage;

    public ShowSessionsAction(ScrumPokerStorage scrumPokerStorage) {
        this.scrumPokerStorage = scrumPokerStorage;
    }

    @Override
    protected String doExecute() throws Exception {
        return "start";
    }

    public List<ScrumPokerSession> getOpenSessions() {
        return scrumPokerStorage.getOpenSessions();
    }

    public List<ScrumPokerSession> getClosedSessions() {
        return scrumPokerStorage.getClosedSessions();
    }

}