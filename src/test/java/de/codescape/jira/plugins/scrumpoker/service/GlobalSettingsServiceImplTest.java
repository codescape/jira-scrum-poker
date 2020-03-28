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

import static de.codescape.jira.plugins.scrumpoker.model.GlobalSettings.DEFAULT_PROJECT_ACTIVATION_DEFAULT;
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
    public void persistStoryPointFieldShouldRemoveTheSettingIfNoValueIsGiven() {
        globalSettingsService.persist(new GlobalSettings());
        assertThat(globalSettingsService.load().getStoryPointField(), nullValue());
    }

    @Test
    public void persistStoryPointFieldShouldSaveTheSettingIfFieldIsGiven() {
        GlobalSettings globalSettings = new GlobalSettings();
        globalSettings.setStoryPointField(NEW_FIELD_NAME);
        globalSettingsService.persist(globalSettings);
        assertThat(globalSettingsService.load().getStoryPointField(), equalTo(NEW_FIELD_NAME));
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
        globalSettings.setDefaultProjectActivation(true);
        globalSettingsService.persist(globalSettings);
        assertThat(globalSettingsService.load().isDefaultProjectActivation(), is(equalTo(true)));

        globalSettings.setDefaultProjectActivation(false);
        globalSettingsService.persist(globalSettings);
        assertThat(globalSettingsService.load().isDefaultProjectActivation(), is(equalTo(false)));
    }

    @Test
    public void loadingDefaultProjectActivationAlwaysReturnsDefaultValueIfNoValueIsSet() {
        ScrumPokerSetting[] scrumPokerSettings = activeObjects.find(ScrumPokerSetting.class);
        Arrays.stream(scrumPokerSettings).forEach(activeObjects::delete);
        assertThat(globalSettingsService.load().isDefaultProjectActivation(), is(equalTo(DEFAULT_PROJECT_ACTIVATION_DEFAULT)));
    }

}
