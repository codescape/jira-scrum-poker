package net.congstar.jira.plugins.scrumpoker.action;

import com.atlassian.jira.user.util.UserManager;
import net.congstar.jira.plugins.scrumpoker.data.ScrumPokerStorage;
import net.congstar.jira.plugins.scrumpoker.model.ScrumPokerSession;

import java.util.List;

public class ShowSessionsAction extends ScrumPokerAction {

    private static final long serialVersionUID = 1L;

    private final ScrumPokerStorage scrumPokerStorage;
    private final UserManager userManager;

    public ShowSessionsAction(ScrumPokerStorage scrumPokerStorage, UserManager userManager) {
        this.scrumPokerStorage = scrumPokerStorage;
        this.userManager = userManager;
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

    public String getUsername(String key) {
        return userManager.getUserByKey(key).getDisplayName();
    }

}