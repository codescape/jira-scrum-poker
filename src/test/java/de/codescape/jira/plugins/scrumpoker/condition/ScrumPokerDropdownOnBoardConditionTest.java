package de.codescape.jira.plugins.scrumpoker.condition;

import com.atlassian.jira.plugin.webfragment.model.JiraHelper;
import com.atlassian.jira.user.ApplicationUser;
import de.codescape.jira.plugins.scrumpoker.model.GlobalSettings;
import de.codescape.jira.plugins.scrumpoker.service.GlobalSettingsService;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

public class ScrumPokerDropdownOnBoardConditionTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private GlobalSettingsService globalSettingsService;

    @InjectMocks
    private ScrumPokerDropdownOnBoardCondition scrumPokerDropdownOnBoardCondition;

    @Mock
    private ApplicationUser applicationUser;

    @Mock
    private JiraHelper jiraHelper;

    @Mock
    private GlobalSettings globalSettings;

    @Test
    public void shouldDisplayWhenSettingDisplayDropdownOnBoardsIsSetToTrue() {
        when(globalSettingsService.load()).thenReturn(globalSettings);
        when(globalSettings.isDisplayDropdownOnBoards()).thenReturn(true);
        assertThat(scrumPokerDropdownOnBoardCondition.shouldDisplay(applicationUser, jiraHelper), is(true));
    }

    @Test
    public void shouldDisplayWhenSettingDisplayDropdownOnBoardsIsSetToFalse() {
        when(globalSettingsService.load()).thenReturn(globalSettings);
        when(globalSettings.isDisplayDropdownOnBoards()).thenReturn(false);
        assertThat(scrumPokerDropdownOnBoardCondition.shouldDisplay(applicationUser, jiraHelper), is(false));
    }

}
