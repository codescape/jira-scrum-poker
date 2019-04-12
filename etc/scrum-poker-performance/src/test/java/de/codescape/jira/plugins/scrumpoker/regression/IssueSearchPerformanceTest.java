package de.codescape.jira.plugins.scrumpoker.regression;

import de.codescape.jira.plugins.scrumpoker.BasePerformanceTest;
import de.codescape.jira.plugins.scrumpoker.pages.IssueSearchPage;
import org.junit.Test;

public class IssueSearchPerformanceTest extends BasePerformanceTest {

    @Test
    public void openIssueSearch() {
        withUserLoggedIn();

        for (int i = 0; i < pageRequests(); i++) {
            new IssueSearchPage(webDriver).openPage();
        }
    }

}
