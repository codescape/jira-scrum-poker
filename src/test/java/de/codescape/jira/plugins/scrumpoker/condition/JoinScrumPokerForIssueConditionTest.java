package de.codescape.jira.plugins.scrumpoker.condition;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.plugin.webfragment.model.JiraHelper;
import com.atlassian.jira.user.ApplicationUser;
import de.codescape.jira.plugins.scrumpoker.service.ScrumPokerSessionService;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

public class JoinScrumPokerForIssueConditionTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private ScrumPokerSessionService scrumPokerSessionService;

    @InjectMocks
    private JoinScrumPokerForIssueCondition joinScrumPokerForIssueCondition;

    @Mock
    private Issue issue;

    @Mock
    private ApplicationUser applicationUser;

    @Mock
    private JiraHelper jiraHelper;

    @Test
    public void shouldNotDisplayWhenNoActiveSessionForIssueExists() {
        when(issue.getKey()).thenReturn("KEY-1");
        when(scrumPokerSessionService.hasActiveSession(issue.getKey())).thenReturn(false);

        assertThat(joinScrumPokerForIssueCondition.shouldDisplay(applicationUser, issue, jiraHelper), is(false));
    }

    @Test
    public void shouldDisplayWhenActiveSessionForIssueExists() {
        when(issue.getKey()).thenReturn("KEY-1");
        when(scrumPokerSessionService.hasActiveSession(issue.getKey())).thenReturn(true);

        assertThat(joinScrumPokerForIssueCondition.shouldDisplay(applicationUser, issue, jiraHelper), is(true));
    }

}
