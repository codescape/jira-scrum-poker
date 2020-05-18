package de.codescape.jira.plugins.scrumpoker.service;

import com.atlassian.activeobjects.test.TestActiveObjects;
import de.codescape.jira.plugins.scrumpoker.ScrumPokerTestDatabaseUpdater;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerSetting;
import de.codescape.jira.plugins.scrumpoker.model.GlobalSettings;
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

import static de.codescape.jira.plugins.scrumpoker.model.GlobalSettings.ACTIVATE_SCRUM_POKER_DEFAULT;
import static de.codescape.jira.plugins.scrumpoker.model.GlobalSettings.SESSION_TIMEOUT_DEFAULT;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(ActiveObjectsJUnitRunner.class)
@Data(ScrumPokerTestDatabaseUpdater.class)
@Jdbc(Hsql.class)
@NameConverters
public class GlobalSettingsServiceImplTest {

    private static final String NEW_FIELD_NAME = "MY_NEW_FIELD";

    @SuppressWarnings("unused")
    private EntityManager entityManager;
    private TestActiveObjects activeObjects;

    private GlobalSettingsService globalSettingsService;

    @Before
    public void before() {
        activeObjects = new TestActiveObjects(entityManager);
        globalSettingsService = new GlobalSettingsServiceImpl(activeObjects);
    }

    @Test
    public void persistEstimateFieldShouldRemoveTheSettingIfNoValueIsGiven() {
        globalSettingsService.persist(new GlobalSettings());
        assertThat(globalSettingsService.load().getEstimateField(), nullValue());
    }

    @Test
    public void persistEstimateFieldShouldSaveTheSettingIfFieldIsGiven() {
        GlobalSettings globalSettings = new GlobalSettings();
        globalSettings.setEstimateField(NEW_FIELD_NAME);
        globalSettingsService.persist(globalSettings);
        assertThat(globalSettingsService.load().getEstimateField(), equalTo(NEW_FIELD_NAME));
    }

    @Test
    public void loadingSessionTimeoutAlwaysReturnsDefaultValueIfNoValueIsSet() {
        ScrumPokerSetting[] scrumPokerSettings = activeObjects.find(ScrumPokerSetting.class);
        Arrays.stream(scrumPokerSettings).forEach(activeObjects::delete);
        assertThat(globalSettingsService.load().getSessionTimeout(), is(equalTo(SESSION_TIMEOUT_DEFAULT)));
    }

    @Test
    public void persistSessionTimeoutShouldSaveTheValueIfValueIsGiven() {
        GlobalSettings globalSettings = new GlobalSettings();
        globalSettings.setSessionTimeout(128);
        globalSettingsService.persist(globalSettings);
        assertThat(globalSettingsService.load().getSessionTimeout(), is(equalTo(128)));
    }

    @Test
    public void persistDefaultProjectActivationShouldSaveTheSetting() {
        GlobalSettings globalSettings = new GlobalSettings();
        globalSettings.setActivateScrumPoker(true);
        globalSettingsService.persist(globalSettings);
        assertThat(globalSettingsService.load().isActivateScrumPoker(), is(equalTo(true)));

        globalSettings.setActivateScrumPoker(false);
        globalSettingsService.persist(globalSettings);
        assertThat(globalSettingsService.load().isActivateScrumPoker(), is(equalTo(false)));
    }

    @Test
    public void loadingDefaultProjectActivationAlwaysReturnsDefaultValueIfNoValueIsSet() {
        ScrumPokerSetting[] scrumPokerSettings = activeObjects.find(ScrumPokerSetting.class);
        Arrays.stream(scrumPokerSettings).forEach(activeObjects::delete);
        assertThat(globalSettingsService.load().isActivateScrumPoker(), is(equalTo(ACTIVATE_SCRUM_POKER_DEFAULT)));
    }

}
