package de.codescape.jira.plugins.scrumpoker.regression;

import de.codescape.jira.plugins.scrumpoker.BasePerformanceTest;
import de.codescape.jira.plugins.scrumpoker.pages.AgileBoardPage;
import org.junit.Test;

import static de.codescape.jira.plugins.scrumpoker.MavenProperties.getProperties;

public class AgileBoardPerformanceTest extends BasePerformanceTest {

    @Test
    public void openAgileBoard() {
        withUserLoggedIn();

        String sampleProject = getProperties().getProperty("sampleProject");
        String boardId = getProperties().getProperty("boardId");

        for (int i = 0; i < pageRequests(); i++) {
            new AgileBoardPage(webDriver, sampleProject, boardId).openPage();
        }
    }

}
