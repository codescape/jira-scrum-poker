package de.codescape.jira.plugins.scrumpoker.upgrade;

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

public class Upgrade07DisplayCommentsForIssueTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private GlobalSettingsService globalSettingsService;

    @InjectMocks
    private Upgrade07DisplayCommentsForIssue upgrade;

    @Mock
    private GlobalSettings globalSettings;

    @Test
    public void shouldPersistDefaultSettingForDisplayCommentsForIssueInPluginSettings() {
        when(globalSettingsService.load()).thenReturn(globalSettings);
        upgrade.doUpgrade();
        verify(globalSettings).setDisplayCommentsForIssue(GlobalSettings.DISPLAY_COMMENTS_FOR_ISSUE_DEFAULT);
        verify(globalSettingsService).persist(globalSettings);
    }

    @Test
    public void shouldReturnCorrectBuildNumber() {
        assertThat(upgrade.getBuildNumber(), is(equalTo(7)));
    }

    @Test
    public void shouldReturnShortDescriptionWithLessThan50Characters() {
        assertThat(upgrade.getShortDescription().length(), is(lessThan(50)));
    }

}
