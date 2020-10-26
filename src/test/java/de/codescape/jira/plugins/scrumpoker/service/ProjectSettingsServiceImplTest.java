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
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assume.assumeThat;

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

    /* tests for persistSettings() */

    @Test
    public void persistSettingsOverwritesExistingSettingsForProject() {
        projectSettingsService.persistSettings(1L, true, "someField", "1,2,3");
        projectSettingsService.persistSettings(1L, false, "anotherField", "1,2,3,5");

        ScrumPokerProject scrumPokerProject = projectSettingsService.loadSettings(1L);
        assertThat(scrumPokerProject.isScrumPokerEnabled(), is(false));
        assertThat(scrumPokerProject.getEstimateField(), is("anotherField"));
        assertThat(scrumPokerProject.getCardSet(), is("1,2,3,5"));
    }

    /* tests for loadSettings() */

    @Test
    public void shouldLoadPersistedSettingsForProject() {
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

    /* tests for removeSettings() */

    @Test
    public void removeSettingsResultsInProjectSpecificSettingsToBeDeleted() {
        projectSettingsService.persistSettings(1L, true, "someField", "1,2,3");
        assumeThat(projectSettingsService.loadSettings(1L), is(notNullValue()));

        projectSettingsService.removeSettings(1L);

        assertThat(projectSettingsService.loadSettings(1L), is(nullValue()));
    }

    /* tests for loadAll() */

    @Test
    public void loadAllReturnsAllProjectSpecificConfigurations() {
        projectSettingsService.persistSettings(1L, true, "A", "B");
        projectSettingsService.persistSettings(57L, true, "A", "B");
        projectSettingsService.persistSettings(191L, true, "A", "B");

        List<ScrumPokerProject> scrumPokerProjects = projectSettingsService.loadAll();

        assertThat(scrumPokerProjects.size(), is(3));
        assertThat(scrumPokerProjects, allOf(
            hasItem(hasProperty("projectId", equalTo(1L))),
            hasItem(hasProperty("projectId", equalTo(57L))),
            hasItem(hasProperty("projectId", equalTo(191L)))
        ));
    }

}
