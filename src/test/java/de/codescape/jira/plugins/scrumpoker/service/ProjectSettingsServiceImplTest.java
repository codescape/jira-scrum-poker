package de.codescape.jira.plugins.scrumpoker.service;

import com.atlassian.activeobjects.test.TestActiveObjects;
import de.codescape.jira.plugins.scrumpoker.ScrumPokerTestDatabaseUpdater;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerProject;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

@RunWith(ActiveObjectsJUnitRunner.class)
@Data(ScrumPokerTestDatabaseUpdater.class)
@Jdbc(Hsql.class)
@NameConverters
public class ProjectSettingsServiceImplTest {

    @SuppressWarnings("unused")
    private EntityManager entityManager;
    private TestActiveObjects activeObjects;

    private ProjectSettingsService projectSettingsService;

    @Before
    public void before() {
        activeObjects = new TestActiveObjects(entityManager);
        projectSettingsService = new ProjectSettingsServiceImpl(activeObjects);
        deleteAllScrumPokerProjectConfigurations();
    }

    private void deleteAllScrumPokerProjectConfigurations() {
        ScrumPokerProject[] scrumPokerProjects = activeObjects.find(ScrumPokerProject.class);
        Arrays.stream(scrumPokerProjects).forEach(activeObjects::delete);
    }

    @Test
    public void shouldPersistProjectSettings() {
        projectSettingsService.persistSettings(1L, true, "someField", "1,2,3");

        ScrumPokerProject scrumPokerProject = projectSettingsService.loadSettings(1L);
        assertThat(scrumPokerProject.isScrumPokerEnabled(), is(true));
        assertThat(scrumPokerProject.getEstimateField(), is("someField"));
        assertThat(scrumPokerProject.getCardSet(), is("1,2,3"));
    }

    @Test
    public void loadSettingsReturnsNullIfNoProjectSpecificSettingsExist() {
        assertThat(projectSettingsService.loadSettings(2L), is(nullValue()));
    }

}
