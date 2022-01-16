package de.codescape.jira.plugins.scrumpoker.upgrade;

import com.atlassian.activeobjects.test.TestActiveObjects;
import de.codescape.jira.plugins.scrumpoker.ScrumPokerTestDatabaseUpdater;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerProject;
import de.codescape.jira.plugins.scrumpoker.model.ProjectActivation;
import net.java.ao.DBParam;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(ActiveObjectsJUnitRunner.class)
@Data(ScrumPokerTestDatabaseUpdater.class)
@Jdbc(Hsql.class)
@NameConverters
public class Upgrade12MigrateScrumPokerEnabledTest {

    @SuppressWarnings("unused")
    private EntityManager entityManager;
    private TestActiveObjects activeObjects;

    private Upgrade12MigrateScrumPokerEnabled upgrade;

    @Before
    public void before() {
        activeObjects = new TestActiveObjects(entityManager);
        upgrade = new Upgrade12MigrateScrumPokerEnabled(activeObjects);
    }

    @Test
    public void shouldReturnCorrectBuildNumber() {
        assertThat(upgrade.getBuildNumber(), is(equalTo(12)));
    }

    @Test
    public void shouldReturnShortDescriptionWithLessThan50Characters() {
        assertThat(upgrade.getShortDescription().length(), is(lessThan(50)));
    }

    @Test
    public void shouldMigrateOldSettingEnabledStatusToNewEnabledStatus() {
        ScrumPokerProject scrumPokerProject = activeObjects.create(ScrumPokerProject.class,
            new DBParam("PROJECT_ID", 4711L));
        //noinspection deprecation
        scrumPokerProject.setScrumPokerEnabled(true);
        scrumPokerProject.save();

        upgrade.performUpgrade();

        ScrumPokerProject[] scrumPokerProjects = activeObjects.find(ScrumPokerProject.class, Query.select().where("PROJECT_ID = ?", 4711L));
        assertThat(scrumPokerProjects.length, is(1));
        assertThat(scrumPokerProjects[0].getActivateScrumPoker(), is(ProjectActivation.ACTIVATE));
    }

    @Test
    public void shouldMigrateOldSettingInheritedStatusToNewInheritedStatus() {
        ScrumPokerProject scrumPokerProject = activeObjects.create(ScrumPokerProject.class,
            new DBParam("PROJECT_ID", 4711L));
        //noinspection deprecation
        scrumPokerProject.setScrumPokerEnabled(false);
        scrumPokerProject.save();

        upgrade.performUpgrade();

        ScrumPokerProject[] scrumPokerProjects = activeObjects.find(ScrumPokerProject.class, Query.select().where("PROJECT_ID = ?", 4711L));
        assertThat(scrumPokerProjects.length, is(1));
        assertThat(scrumPokerProjects[0].getActivateScrumPoker(), is(ProjectActivation.INHERIT));
    }

}
