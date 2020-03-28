package de.codescape.jira.plugins.scrumpoker.upgrade;

import de.codescape.jira.plugins.scrumpoker.model.AllowRevealDeck;
import de.codescape.jira.plugins.scrumpoker.model.GlobalSettings;
import de.codescape.jira.plugins.scrumpoker.service.GlobalSettingsService;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class Upgrade03AllowRevealDeckTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private GlobalSettingsService globalSettingsService;

    @InjectMocks
    private Upgrade03AllowRevealDeck upgrade;

    @Mock
    private GlobalSettings globalSettings;

    @Test
    public void shouldPersistAllowRevealDeckInPluginSettings() {
        when(globalSettingsService.load()).thenReturn(globalSettings);
        upgrade.doUpgrade();
        verify(globalSettings).setAllowRevealDeck(AllowRevealDeck.EVERYONE);
        verify(globalSettingsService).persist(globalSettings);
    }

    @Test
    public void shouldReturnCorrectBuildNumber() {
        assertThat(upgrade.getBuildNumber(), is(equalTo(3)));
    }

    @Test
    public void shouldReturnShortDescriptionWithLessThan50Characters() {
        assertThat(upgrade.getShortDescription().length(), is(lessThan(50)));
    }

}
