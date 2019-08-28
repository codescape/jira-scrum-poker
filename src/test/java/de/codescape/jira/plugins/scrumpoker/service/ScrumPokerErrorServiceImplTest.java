package de.codescape.jira.plugins.scrumpoker.service;

import com.atlassian.activeobjects.test.TestActiveObjects;
import com.atlassian.jira.util.BuildUtilsInfo;
import com.atlassian.plugin.Plugin;
import com.atlassian.plugin.PluginAccessor;
import com.atlassian.plugin.PluginInformation;
import de.codescape.jira.plugins.scrumpoker.ScrumPokerTestDatabaseUpdater;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerError;
import net.java.ao.EntityManager;
import net.java.ao.test.converters.NameConverters;
import net.java.ao.test.jdbc.Data;
import net.java.ao.test.jdbc.Hsql;
import net.java.ao.test.jdbc.Jdbc;
import net.java.ao.test.junit.ActiveObjectsJUnitRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static de.codescape.jira.plugins.scrumpoker.ScrumPokerConstants.SCRUM_POKER_PLUGIN_KEY;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(ActiveObjectsJUnitRunner.class)
@Data(ScrumPokerTestDatabaseUpdater.class)
@Jdbc(Hsql.class)
@NameConverters
public class ScrumPokerErrorServiceImplTest {

    private static final String JIRA_VERSION = "8.3.2";
    private static final String SCRUM_POKER_VERSION = "4.20";

    @SuppressWarnings("unused")
    private EntityManager entityManager;
    private TestActiveObjects activeObjects;

    private BuildUtilsInfo buildUtilsInfo;
    private PluginAccessor pluginAccessor;

    private ScrumPokerErrorService scrumPokerErrorService;

    private Plugin scrumPokerPlugin;
    private PluginInformation scrumPokerPluginInformation;

    @Before
    public void before() {
        scrumPokerPlugin = mock(Plugin.class);
        scrumPokerPluginInformation = mock(PluginInformation.class);
        activeObjects = new TestActiveObjects(entityManager);
        buildUtilsInfo = mock(BuildUtilsInfo.class);
        pluginAccessor = mock(PluginAccessor.class);
        scrumPokerErrorService = new ScrumPokerErrorServiceImpl(activeObjects, buildUtilsInfo, pluginAccessor);
        deleteAllScrumPokerErrors();
    }

    private void deleteAllScrumPokerErrors() {
        ScrumPokerError[] scrumPokerErrors = activeObjects.find(ScrumPokerError.class);
        Arrays.stream(scrumPokerErrors).forEach(activeObjects::delete);
    }

    @Test
    public void shouldSaveErrorWithAllDetails() {
        expectJiraVersion(JIRA_VERSION);
        expectScrumPokerVersion(SCRUM_POKER_VERSION);

        scrumPokerErrorService.logError("Let's add one error!", new RuntimeException("Ooops!"));
        assertThat(errorLog().length, is(1));
        assertThat(errorLog()[0], allOf(
            hasProperty("jiraVersion", equalTo(JIRA_VERSION)),
            hasProperty("scrumPokerVersion", equalTo(SCRUM_POKER_VERSION)),
            hasProperty("errorMessage", equalTo("Let's add one error!")),
            hasProperty("errorTimestamp", notNullValue()),
            hasProperty("stacktrace", containsString("Ooops!"))
        ));
    }

    @Test
    public void shouldSaveErrorWithMessageOnly() {
        scrumPokerErrorService.logError("Message!", null);
        assertThat(errorLog().length, is(1));
        assertThat(errorLog()[0].getErrorMessage(), equalTo("Message!"));
    }

    @Test
    public void shouldSaveErrorWithExceptionOnly() {
        scrumPokerErrorService.logError(null, new RuntimeException("Ooops!"));
        assertThat(errorLog().length, is(1));
        assertThat(errorLog()[0].getStacktrace(), allOf(
            containsString("Ooops!"),
            containsString("RuntimeException")
        ));
    }

    @Test
    public void shouldReturnAllErrorsWithNewestFirst() {
        scrumPokerErrorService.logError("First", null);
        scrumPokerErrorService.logError("Second", null);
        scrumPokerErrorService.logError("Third", null);
        List<ScrumPokerError> scrumPokerErrors = scrumPokerErrorService.listAll();
        assertThat(scrumPokerErrors.size(), is(3));
        assertThat(scrumPokerErrors.get(0).getErrorMessage(), equalTo("Third"));
        assertThat(scrumPokerErrors.get(1).getErrorMessage(), equalTo("Second"));
        assertThat(scrumPokerErrors.get(2).getErrorMessage(), equalTo("First"));
    }

    @Test
    public void shouldDeleteAllErrors() {
        IntStream.range(0, 50).forEach(i -> scrumPokerErrorService.logError("Some Error...", null));
        assertThat(errorLog().length, is(50));
        scrumPokerErrorService.emptyErrorLog();
        assertThat(errorLog().length, is(0));
    }

    private ScrumPokerError[] errorLog() {
        return activeObjects.find(ScrumPokerError.class);
    }

    private void expectScrumPokerVersion(String scrumPokerVersion) {
        when(pluginAccessor.getPlugin(SCRUM_POKER_PLUGIN_KEY)).thenReturn(scrumPokerPlugin);
        when(scrumPokerPlugin.getPluginInformation()).thenReturn(scrumPokerPluginInformation);
        when(scrumPokerPluginInformation.getVersion()).thenReturn(scrumPokerVersion);
    }

    private void expectJiraVersion(String jiraVersion) {
        when(buildUtilsInfo.getVersion()).thenReturn(jiraVersion);
    }

}
