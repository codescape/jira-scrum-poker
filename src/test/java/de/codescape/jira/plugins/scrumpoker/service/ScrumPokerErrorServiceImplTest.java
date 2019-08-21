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

import static de.codescape.jira.plugins.scrumpoker.ScrumPokerConstants.SCRUM_POKER_PLUGIN_KEY;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(ActiveObjectsJUnitRunner.class)
@Data(ScrumPokerTestDatabaseUpdater.class)
@Jdbc(Hsql.class)
@NameConverters
public class ScrumPokerErrorServiceImplTest {

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
    public void shouldSaveTheErrorToLogIntoTheDatabase() {
        when(buildUtilsInfo.getVersion()).thenReturn("7.13");
        when(pluginAccessor.getPlugin(SCRUM_POKER_PLUGIN_KEY)).thenReturn(scrumPokerPlugin);
        when(scrumPokerPlugin.getPluginInformation()).thenReturn(scrumPokerPluginInformation);
        when(scrumPokerPluginInformation.getVersion()).thenReturn("4.20");
        scrumPokerErrorService.logError("Let's add one error!", new RuntimeException("Ooops!"));
        assertThat(errorsFromDatabase().length, is(1));
        System.out.println("         ID: " + errorsFromDatabase()[0].getID());
        System.out.println("    Message: " + errorsFromDatabase()[0].getErrorMessage());
        System.out.println("       Jira: " + errorsFromDatabase()[0].getJiraVersion());
        System.out.println("     Plugin: " + errorsFromDatabase()[0].getScrumPokerVersion());
        System.out.println(" Stacktrace: " + errorsFromDatabase()[0].getStacktrace());
        System.out.println("  Timestamp: " + errorsFromDatabase()[0].getTimestamp());
        // TODO cleanup test
    }

    // TODO add tests for adding log entries

    // TODO add tests for getting all entries

    // TODO add tests for purging log entries

    private ScrumPokerError[] errorsFromDatabase() {
        return activeObjects.find(ScrumPokerError.class);
    }

}
