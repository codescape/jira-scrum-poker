package de.codescape.jira.plugins.scrumpoker.upgrade;

import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import de.codescape.jira.plugins.scrumpoker.model.GlobalSettings;
import de.codescape.jira.plugins.scrumpoker.service.GlobalSettingsService;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static de.codescape.jira.plugins.scrumpoker.ScrumPokerConstants.SCRUM_POKER_PLUGIN_KEY;
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
    private GlobalSettingsService globalSettingsService;

    @InjectMocks
    private Upgrade01RemovePluginSettings upgrade;

    @Mock
    private PluginSettings pluginSettings;

    @Mock
    private GlobalSettings globalSettings;

    @Test
    public void shouldDeleteSessionTimeoutIfItExistsInPluginSettings() {
        withPluginSettingsFactory();
        when(pluginSettings.get(SCRUM_POKER_PLUGIN_KEY + ".sessionTimeout")).thenReturn("12");
        when(globalSettingsService.load()).thenReturn(globalSettings);
        upgrade.doUpgrade();
        verify(pluginSettings).remove(SCRUM_POKER_PLUGIN_KEY + ".sessionTimeout");
    }

    @Test
    public void shouldDeleteStoryPointFieldIfItExistsInPluginSettings() {
        withPluginSettingsFactory();
        when(pluginSettings.get(SCRUM_POKER_PLUGIN_KEY + ".storyPointField")).thenReturn("customfield-10006");
        when(globalSettingsService.load()).thenReturn(globalSettings);
        upgrade.doUpgrade();
        verify(pluginSettings).remove(SCRUM_POKER_PLUGIN_KEY + ".storyPointField");
    }

    @Test
    public void shouldPersistStoryPointFieldIfExistsInPluginSettings() {
        withPluginSettingsFactory();
        when(pluginSettings.get(SCRUM_POKER_PLUGIN_KEY + ".storyPointField")).thenReturn("customfield-10006");
        when(globalSettingsService.load()).thenReturn(globalSettings);
        upgrade.doUpgrade();
        verify(globalSettings).setStoryPointField("customfield-10006");
        verify(globalSettingsService).persist(globalSettings);
    }

    @Test
    public void shouldPersistSessionTimeoutIfExistInPluginSettings() {
        withPluginSettingsFactory();
        when(pluginSettings.get(SCRUM_POKER_PLUGIN_KEY + ".sessionTimeout")).thenReturn("12");
        when(globalSettingsService.load()).thenReturn(globalSettings);
        upgrade.doUpgrade();
        verify(globalSettings).setSessionTimeout(12);
        verify(globalSettingsService).persist(globalSettings);
    }

    @Test
    public void shouldReturnCorrectBuildNumber() {
        assertThat(upgrade.getBuildNumber(), is(equalTo(1)));
    }

    @Test
    public void shouldReturnShortDescriptionWithLessThan50Characters() {
        assertThat(upgrade.getShortDescription().length(), is(lessThan(50)));
    }

    private void withPluginSettingsFactory() {
        when(pluginSettingsFactory.createGlobalSettings()).thenReturn(pluginSettings);
    }

}
