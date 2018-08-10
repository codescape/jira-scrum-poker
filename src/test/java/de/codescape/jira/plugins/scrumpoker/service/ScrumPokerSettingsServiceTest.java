package de.codescape.jira.plugins.scrumpoker.service;

import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static de.codescape.jira.plugins.scrumpoker.service.ScrumPokerSettingsService.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ScrumPokerSettingsServiceTest {

    private static final String NEW_FIELD_NAME = "MY_NEW_FIELD";

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private PluginSettingsFactory pluginSettingsFactory;

    @InjectMocks
    private ScrumPokerSettingsService scrumPokerSettingsService;

    @Mock
    private PluginSettings pluginSettings;

    @Before
    public void before() {
        when(pluginSettingsFactory.createGlobalSettings()).thenReturn(pluginSettings);
    }

    @Test
    public void persistingShouldRemoveTheSettingIfNoFieldIsGiven() {
        scrumPokerSettingsService.persistSettings("", "12");
        verify(pluginSettings).remove(SETTINGS_NAMESPACE + "." + STORY_POINT_FIELD);
    }

    @Test
    public void persistingShouldSaveTheSettingIfFieldIsGiven() {
        scrumPokerSettingsService.persistSettings(NEW_FIELD_NAME, "12");
        verify(pluginSettings).put(SETTINGS_NAMESPACE + "." + STORY_POINT_FIELD, NEW_FIELD_NAME);
    }

    @Test
    public void loadingSessionTimeoutAlwaysReturnsDefaultValueIfNoValueIsSet() {
        when(pluginSettings.get(SESSION_TIMEOUT)).thenReturn(null);
        assertThat(scrumPokerSettingsService.loadSessionTimeout(), is(equalTo(SESSION_TIMEOUT_DEFAULT)));
    }

}
