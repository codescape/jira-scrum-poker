package de.codescape.jira.plugins.scrumpoker.upgrade;

import de.codescape.jira.plugins.scrumpoker.service.ScrumPokerSettingService;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

public class Upgrade02DefaultProjectActivationTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();


    @Mock
    private ScrumPokerSettingService scrumPokerSettingService;

    @InjectMocks
    private Upgrade02DefaultProjectActivation upgrade;

    @Test
    public void shouldPersistDefaultProjectActivationInPluginSettings() {
        upgrade.doUpgrade();
        verify(scrumPokerSettingService).persistDefaultProjectActivation(true);
    }

    @Test
    public void shouldReturnCorrectBuildNumber() {
        assertThat(upgrade.getBuildNumber(), is(equalTo(2)));
    }

    @Test
    public void shouldReturnShortDescriptionWithLessThan50Characters() {
        assertThat(upgrade.getShortDescription().length(), is(lessThan(50)));
    }

}
