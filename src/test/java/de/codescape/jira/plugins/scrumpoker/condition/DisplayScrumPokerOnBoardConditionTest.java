package de.codescape.jira.plugins.scrumpoker.condition;

import com.atlassian.jira.plugin.webfragment.model.JiraHelper;
import com.atlassian.jira.user.ApplicationUser;
import de.codescape.jira.plugins.scrumpoker.model.GlobalSettings;
import de.codescape.jira.plugins.scrumpoker.service.ScrumPokerSettingService;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

public class DisplayScrumPokerOnBoardConditionTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private ScrumPokerSettingService scrumPokerSettingService;

    @InjectMocks
    private DisplayScrumPokerOnBoardCondition displayScrumPokerOnBoardCondition;

    @Mock
    private ApplicationUser applicationUser;

    @Mock
    private JiraHelper jiraHelper;

    @Mock
    private GlobalSettings globalSettings;

    @Test
    public void shouldDisplayWhenSettingDisplayDropdownOnBoardsIsSetToTrue() {
        when(scrumPokerSettingService.load()).thenReturn(globalSettings);
        when(globalSettings.isDisplayDropdownOnBoards()).thenReturn(true);
        assertThat(displayScrumPokerOnBoardCondition.shouldDisplay(applicationUser, jiraHelper), is(true));
    }

    @Test
    public void shouldDisplayWhenSettingDisplayDropdownOnBoardsIsSetToFalse() {
        when(scrumPokerSettingService.load()).thenReturn(globalSettings);
        when(globalSettings.isDisplayDropdownOnBoards()).thenReturn(false);
        assertThat(displayScrumPokerOnBoardCondition.shouldDisplay(applicationUser, jiraHelper), is(false));
    }

}
