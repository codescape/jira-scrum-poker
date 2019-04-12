package de.codescape.jira.plugins.scrumpoker.plugin;

import de.codescape.jira.plugins.scrumpoker.BasePerformanceTest;
import de.codescape.jira.plugins.scrumpoker.pages.ScrumPokerSessionsPage;
import org.junit.Test;

public class ScrumPokerSessionsPerformanceTest extends BasePerformanceTest {

    @Test
    public void openScrumPokerSessions() {
        withUserLoggedIn();

        for (int i = 0; i < pageRequests(); i++) {
            new ScrumPokerSessionsPage(webDriver).openPage();
        }
    }

}
