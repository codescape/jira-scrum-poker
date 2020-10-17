package de.codescape.jira.plugins.scrumpoker.upgrade;

import com.atlassian.activeobjects.test.TestActiveObjects;
import de.codescape.jira.plugins.scrumpoker.ScrumPokerTestDatabaseUpdater;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerSetting;
import de.codescape.jira.plugins.scrumpoker.service.GlobalSettingsServiceImpl;
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

import static de.codescape.jira.plugins.scrumpoker.upgrade.Upgrade11MigrateActivateScrumPoker.DEFAULT_PROJECT_ACTIVATION;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(ActiveObjectsJUnitRunner.class)
@Data(ScrumPokerTestDatabaseUpdater.class)
@Jdbc(Hsql.class)
@NameConverters
public class Upgrade11MigrateActivateScrumPokerTest {

    private static final String EXPECTED_VALUE = "true";

    @SuppressWarnings("unused")
    private EntityManager entityManager;
    private TestActiveObjects activeObjects;

    private Upgrade11MigrateActivateScrumPoker upgrade;

    @Before
    public void before() {
        activeObjects = new TestActiveObjects(entityManager);
        upgrade = new Upgrade11MigrateActivateScrumPoker(activeObjects);
    }

    @Test
    public void shouldReturnCorrectBuildNumber() {
        assertThat(upgrade.getBuildNumber(), is(equalTo(11)));
    }

    @Test
    public void shouldReturnShortDescriptionWithLessThan50Characters() {
        assertThat(upgrade.getShortDescription().length(), is(lessThan(50)));
    }

    @Test
    public void shouldMigrateOldSettingToNewKeyIfOldSettingExists() {
        ScrumPokerSetting scrumPokerSetting = activeObjects.create(ScrumPokerSetting.class,
            new DBParam("KEY", DEFAULT_PROJECT_ACTIVATION));
        scrumPokerSetting.setValue(EXPECTED_VALUE);
        scrumPokerSetting.save();

        upgrade.performUpgrade();

        ScrumPokerSetting[] results = queryForNewSetting();
        assertThat(results.length, is(equalTo(1)));
        assertThat(results[0].getValue(), is(equalTo(EXPECTED_VALUE)));

        ScrumPokerSetting[] resultsOld = queryForOldSetting();
        assertThat(resultsOld.length, is(equalTo(0)));
    }

    @Test
    public void shouldNotChangeOtherKeysAndValues() {
        ScrumPokerSetting scrumPokerSetting = activeObjects.create(ScrumPokerSetting.class,
            new DBParam("KEY", "someOtherSettingsKey"));
        scrumPokerSetting.setValue("someValue");
        scrumPokerSetting.save();

        upgrade.performUpgrade();

        ScrumPokerSetting[] results = queryForNewSetting();
        assertThat(results.length, is(equalTo(0)));

        ScrumPokerSetting[] resultsOld = queryForSetting("someOtherSettingsKey");
        assertThat(resultsOld.length, is(equalTo(1)));
        assertThat(resultsOld[0].getValue(), is(equalTo("someValue")));
    }

    private ScrumPokerSetting[] queryForOldSetting() {
        return queryForSetting(DEFAULT_PROJECT_ACTIVATION);
    }

    private ScrumPokerSetting[] queryForNewSetting() {
        return queryForSetting(GlobalSettingsServiceImpl.ACTIVATE_SCRUM_POKER);
    }

    private ScrumPokerSetting[] queryForSetting(String setting) {
        return activeObjects.find(ScrumPokerSetting.class, Query.select()
            .where("KEY = ?", setting));
    }

}
