package de.codescape.jira.plugins.scrumpoker.plugin;

import de.codescape.jira.plugins.scrumpoker.BasePerformanceTest;
import de.codescape.jira.plugins.scrumpoker.pages.ScrumPokerPage;
import org.junit.Test;

import static de.codescape.jira.plugins.scrumpoker.MavenProperties.getProperties;

public class StartScrumPokerPerformanceTest extends BasePerformanceTest {

    @Test
    public void openScrumPoker() {
        withUserLoggedIn();

        String sampleProject = getProperties().getProperty("sampleProject");
        String issueKey = sampleProject + "-1";

        for (int i = 0; i < pageRequests(); i++) {
            new ScrumPokerPage(webDriver, issueKey).openPage();
        }
    }

}
