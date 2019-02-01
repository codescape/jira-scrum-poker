package de.codescape.jira.plugins.scrumpoker.upgrade;

import de.codescape.jira.plugins.scrumpoker.model.AllowRevealDeck;
import de.codescape.jira.plugins.scrumpoker.service.ScrumPokerSettingService;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

public class Upgrade03AllowRevealDeckTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private ScrumPokerSettingService scrumPokerSettingService;

    @InjectMocks
    private Upgrade03AllowRevealDeck upgrade;

    @Test
    public void shouldPersistAllowRevealDeckInPluginSettings() {
        upgrade.doUpgrade();
        verify(scrumPokerSettingService).persistAllowRevealDeck(AllowRevealDeck.EVERYONE);
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
