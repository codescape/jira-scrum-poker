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
public class ScrumPokerSettingServiceImplTest {

    private static final String NEW_FIELD_NAME = "MY_NEW_FIELD";

    @SuppressWarnings("unused")
    private EntityManager entityManager;
    private TestActiveObjects activeObjects;

    private ScrumPokerSettingService scrumPokerSettingService;

    @Before
    public void before() {
        activeObjects = new TestActiveObjects(entityManager);
        scrumPokerSettingService = new ScrumPokerSettingServiceImpl(activeObjects);
    }

    @Test
    public void persistStoryPointFieldShouldRemoveTheSettingIfNoValueIsGiven() {
        scrumPokerSettingService.persist(new GlobalSettings());
        assertThat(scrumPokerSettingService.load().getStoryPointField(), nullValue());
    }

    @Test
    public void persistStoryPointFieldShouldSaveTheSettingIfFieldIsGiven() {
        GlobalSettings globalSettings = new GlobalSettings();
        globalSettings.setStoryPointField(NEW_FIELD_NAME);
        scrumPokerSettingService.persist(globalSettings);
        assertThat(scrumPokerSettingService.load().getStoryPointField(), equalTo(NEW_FIELD_NAME));
    }

    @Test
    public void loadingSessionTimeoutAlwaysReturnsDefaultValueIfNoValueIsSet() {
        ScrumPokerSetting[] scrumPokerSettings = activeObjects.find(ScrumPokerSetting.class);
        Arrays.stream(scrumPokerSettings).forEach(activeObjects::delete);
        assertThat(scrumPokerSettingService.load().getSessionTimeout(), is(equalTo(SESSION_TIMEOUT_DEFAULT)));
    }

    @Test
    public void persistSessionTimeoutShouldSaveTheValueIfValueIsGiven() {
        GlobalSettings globalSettings = new GlobalSettings();
        globalSettings.setSessionTimeout(128);
        scrumPokerSettingService.persist(globalSettings);
        assertThat(scrumPokerSettingService.load().getSessionTimeout(), is(equalTo(128)));
    }

    @Test
    public void persistDefaultProjectActivationShouldSaveTheSetting() {
        GlobalSettings globalSettings = new GlobalSettings();
        globalSettings.setDefaultProjectActivation(true);
        scrumPokerSettingService.persist(globalSettings);
        assertThat(scrumPokerSettingService.load().getDefaultProjectActivation(), is(equalTo(true)));

        globalSettings.setDefaultProjectActivation(false);
        scrumPokerSettingService.persist(globalSettings);
        assertThat(scrumPokerSettingService.load().getDefaultProjectActivation(), is(equalTo(false)));
    }

    @Test
    public void loadingDefaultProjectActivationAlwaysReturnsDefaultValueIfNoValueIsSet() {
        ScrumPokerSetting[] scrumPokerSettings = activeObjects.find(ScrumPokerSetting.class);
        Arrays.stream(scrumPokerSettings).forEach(activeObjects::delete);
        assertThat(scrumPokerSettingService.load().getDefaultProjectActivation(), is(equalTo(DEFAULT_PROJECT_ACTIVATION_DEFAULT)));
    }

}
