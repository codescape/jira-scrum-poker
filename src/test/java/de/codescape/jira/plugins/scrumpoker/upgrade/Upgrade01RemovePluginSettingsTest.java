package de.codescape.jira.plugins.scrumpoker.upgrade;

import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import de.codescape.jira.plugins.scrumpoker.service.ScrumPokerSettingService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static de.codescape.jira.plugins.scrumpoker.upgrade.AbstractUpgradeTask.SCRUM_POKER_PLUGIN_KEY;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class Upgrade01RemovePluginSettingsTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private PluginSettingsFactory pluginSettingsFactory;

    @Mock
    private ScrumPokerSettingService scrumPokerSettingService;

    @InjectMocks
    private Upgrade01RemovePluginSettings upgrade;

    @Mock
    private PluginSettings pluginSettings;

    @Before
    public void before() {
        when(pluginSettingsFactory.createGlobalSettings()).thenReturn(pluginSettings);
    }

    @Test
    public void shouldDeleteSessionTimeoutIfItExistsInPluginSettings() {
        when(pluginSettings.get(SCRUM_POKER_PLUGIN_KEY + ".sessionTimeout")).thenReturn("12");
        upgrade.doUpgrade();
        verify(pluginSettings).remove(SCRUM_POKER_PLUGIN_KEY + ".sessionTimeout");
    }

    @Test
    public void shouldDeleteStoryPointFieldIfItExistsInPluginSettings() {
        when(pluginSettings.get(SCRUM_POKER_PLUGIN_KEY + ".storyPointField")).thenReturn("customfield-10006");
        upgrade.doUpgrade();
        verify(pluginSettings).remove(SCRUM_POKER_PLUGIN_KEY + ".storyPointField");
    }

    @Test
    public void shouldPersistBothSettingsIfStoryPointFieldExistsInPluginSettings() {
        when(pluginSettings.get(SCRUM_POKER_PLUGIN_KEY + ".storyPointField")).thenReturn("customfield-10006");
        upgrade.doUpgrade();
        verify(scrumPokerSettingService).persistSettings("customfield-10006", null);
    }

    @Test
    public void shouldPersistBothSettingsIfStoryPointFieldAndSessionTimeoutExistInPluginSettings() {
        when(pluginSettings.get(SCRUM_POKER_PLUGIN_KEY + ".storyPointField")).thenReturn("customfield-10006");
        when(pluginSettings.get(SCRUM_POKER_PLUGIN_KEY + ".sessionTimeout")).thenReturn("12");
        upgrade.doUpgrade();
        verify(scrumPokerSettingService).persistSettings("customfield-10006", "12");
    }

    @Test
    public void shouldReturnCorrectBuildNumber() {
        assertThat(upgrade.getBuildNumber(), is(equalTo(1)));
    }

    @Test
    public void shouldReturnShortDescriptionWithLessThan50Characters() {
        assertThat(upgrade.getShortDescription().length(), is(lessThan(50)));
    }

}
