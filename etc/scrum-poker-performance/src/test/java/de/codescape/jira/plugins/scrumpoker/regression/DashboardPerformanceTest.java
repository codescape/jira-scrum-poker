package de.codescape.jira.plugins.scrumpoker.regression;

import de.codescape.jira.plugins.scrumpoker.BasePerformanceTest;
import de.codescape.jira.plugins.scrumpoker.pages.DashboardPage;
import org.junit.Test;

public class DashboardPerformanceTest extends BasePerformanceTest {

    @Test
    public void openDashboard() {
        withUserLoggedIn();

        for (int i = 0; i < pageRequests(); i++) {
            new DashboardPage(webDriver).openPage();
        }
    }

}
