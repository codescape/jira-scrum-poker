package de.codescape.jira.plugins.scrumpoker.regression;

import de.codescape.jira.plugins.scrumpoker.BasePerformanceTest;
import de.codescape.jira.plugins.scrumpoker.pages.IssueDetailPage;
import org.junit.Test;

import static de.codescape.jira.plugins.scrumpoker.MavenProperties.getProperties;

public class IssueDetailPerformanceTest extends BasePerformanceTest {

    @Test
    public void openIssueDetail() {
        withUserLoggedIn();

        String sampleProject = getProperties().getProperty("sampleProject");
        String issueKey = sampleProject + "-1";

        for (int i = 0; i < pageRequests(); i++) {
            new IssueDetailPage(webDriver, issueKey).openPage();
        }
    }

}
