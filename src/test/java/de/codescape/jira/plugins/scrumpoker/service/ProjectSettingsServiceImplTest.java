package de.codescape.jira.plugins.scrumpoker.service;

import com.atlassian.activeobjects.test.TestActiveObjects;
import de.codescape.jira.plugins.scrumpoker.ScrumPokerTestDatabaseUpdater;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerProject;
import net.java.ao.EntityManager;
import net.java.ao.Query;
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
    public void shouldPersistScrumPokerEnabledFlag() {
        projectSettingsService.persistActivateScrumPoker(1L, true);
        assertThat(scrumPokerProject(1L).isScrumPokerEnabled(), is(true));
    }

    @Test
    public void scrumPokerEnableFlagReturnsFalseIfProjectHasNoConfiguration() {
        assertThat(projectSettingsService.loadActivateScrumPoker(2L), is(false));
    }

    private ScrumPokerProject scrumPokerProject(Long projectId) {
        ScrumPokerProject[] scrumPokerProjects = activeObjects.find(ScrumPokerProject.class,
            Query.select().where("PROJECT_ID = ?", 1L).limit(1));
        if (scrumPokerProjects.length < 1) {
            throw new RuntimeException("ScrumPokerProject with ID " + projectId + " not found.");
        }
        return scrumPokerProjects[0];
    }

}
