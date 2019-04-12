package de.codescape.jira.plugins.scrumpoker.regression;

import de.codescape.jira.plugins.scrumpoker.BasePerformanceTest;
import de.codescape.jira.plugins.scrumpoker.pages.BrowseProjectPage;
import org.junit.Test;

import static de.codescape.jira.plugins.scrumpoker.MavenProperties.getProperties;

public class BrowseProjectPerformanceTest extends BasePerformanceTest {

    @Test
    public void openBrowseProject() {
        withUserLoggedIn();

        String sampleProject = getProperties().getProperty("sampleProject");

        for (int i = 0; i < pageRequests(); i++) {
            new BrowseProjectPage(webDriver, sampleProject).openPage();
        }
    }

}
