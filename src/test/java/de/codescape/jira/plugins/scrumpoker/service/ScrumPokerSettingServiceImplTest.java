package de.codescape.jira.plugins.scrumpoker.service;

import com.atlassian.activeobjects.test.TestActiveObjects;
import de.codescape.jira.plugins.scrumpoker.ScrumPokerTestDatabaseUpdater;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerSetting;
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

import static de.codescape.jira.plugins.scrumpoker.service.ScrumPokerSettingServiceImpl.SESSION_TIMEOUT_DEFAULT;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(ActiveObjectsJUnitRunner.class)
@Data(ScrumPokerTestDatabaseUpdater.class)
@Jdbc(Hsql.class)
@NameConverters
public class ScrumPokerSettingServiceImplTest {

    private static final String NEW_FIELD_NAME = "MY_NEW_FIELD";

    private EntityManager entityManager;
    private TestActiveObjects activeObjects;

    private ScrumPokerSettingService scrumPokerSettingService;

    @Before
    public void before() {
        activeObjects = new TestActiveObjects(entityManager);
        scrumPokerSettingService = new ScrumPokerSettingServiceImpl(activeObjects);
    }

    @Test
    public void persistingShouldRemoveTheSettingIfNoFieldIsGiven() {
        scrumPokerSettingService.persistSettings(null, "12");
        assertThat(scrumPokerSettingService.loadStoryPointField(), nullValue());
    }

    @Test
    public void persistingShouldSaveTheSettingIfFieldIsGiven() {
        scrumPokerSettingService.persistSettings(NEW_FIELD_NAME, "12");
        assertThat(scrumPokerSettingService.loadStoryPointField(), equalTo(NEW_FIELD_NAME));
    }

    @Test
    public void loadingSessionTimeoutAlwaysReturnsDefaultValueIfNoValueIsSet() {
        ScrumPokerSetting[] scrumPokerSettings = activeObjects.find(ScrumPokerSetting.class);
        Arrays.stream(scrumPokerSettings).forEach(activeObjects::delete);
        assertThat(scrumPokerSettingService.loadSessionTimeout(), is(equalTo(SESSION_TIMEOUT_DEFAULT)));
    }

}
