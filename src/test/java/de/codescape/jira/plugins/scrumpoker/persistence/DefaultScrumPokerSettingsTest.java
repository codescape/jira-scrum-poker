package de.codescape.jira.plugins.scrumpoker.persistence;

import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static de.codescape.jira.plugins.scrumpoker.persistence.DefaultScrumPokerSettings.SETTINGS_NAMESPACE;
import static de.codescape.jira.plugins.scrumpoker.persistence.DefaultScrumPokerSettings.STORY_POINT_FIELD;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DefaultScrumPokerSettingsTest {

    private static final String NEW_FIELD_NAME = "MY_NEW_FIELD";

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private PluginSettingsFactory pluginSettingsFactory;

    @InjectMocks
    private DefaultScrumPokerSettings defaultScrumPokerSettings;

    @Mock
    private PluginSettings pluginSettings;

    @Test
    public void persistingShouldRemoveTheSettingIfNoFieldIsGiven() {
        when(pluginSettingsFactory.createGlobalSettings()).thenReturn(pluginSettings);
        defaultScrumPokerSettings.persistStoryPointFieldId("");
        verify(pluginSettings).remove(SETTINGS_NAMESPACE + "." + STORY_POINT_FIELD);
    }

    @Test
    public void persistingShouldSaveTheSettingIfFieldIsGiven() {
        when(pluginSettingsFactory.createGlobalSettings()).thenReturn(pluginSettings);
        defaultScrumPokerSettings.persistStoryPointFieldId(NEW_FIELD_NAME);
        verify(pluginSettings).put(SETTINGS_NAMESPACE + "." + STORY_POINT_FIELD, NEW_FIELD_NAME);
    }

}
