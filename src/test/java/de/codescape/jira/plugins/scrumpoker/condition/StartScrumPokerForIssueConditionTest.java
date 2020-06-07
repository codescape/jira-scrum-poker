package de.codescape.jira.plugins.scrumpoker.condition;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.plugin.webfragment.model.JiraHelper;
import com.atlassian.jira.user.ApplicationUser;
import de.codescape.jira.plugins.scrumpoker.service.EstimateFieldService;
import de.codescape.jira.plugins.scrumpoker.service.ScrumPokerSessionService;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class StartScrumPokerForIssueConditionTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private EstimateFieldService estimateFieldService;

    @Mock
    private ScrumPokerSessionService scrumPokerSessionService;

    @InjectMocks
    private StartScrumPokerForIssueCondition startScrumPokerForIssueCondition;

    @Mock
    private ApplicationUser applicationUser;

    @Mock
    private JiraHelper jiraHelper;

    @Mock
    private Issue issue;

    @Test
    public void shouldDisplayForEstimableIssueWithoutActiveSession() {
        expectThatIssueIsEstimable();
        expectActiveScrumPokerSessionNotExists();
        assertThat(startScrumPokerForIssueCondition.shouldDisplay(applicationUser, issue, jiraHelper), is(true));
    }

    @Test
    public void shouldNotDisplayForEstimableIssueWithActiveSession() {
        expectThatIssueIsEstimable();
        expectActiveScrumPokerSessionExists();
        assertThat(startScrumPokerForIssueCondition.shouldDisplay(applicationUser, issue, jiraHelper), is(false));
    }

    @Test
    public void shouldNotDisplayForNonEstimableIssue() {
        expectThatIssueIsNotEstimable();
        assertThat(startScrumPokerForIssueCondition.shouldDisplay(applicationUser, issue, jiraHelper), is(false));
    }

    private void expectActiveScrumPokerSessionExists() {
        when(issue.getKey()).thenReturn("KEY-1");
        when(scrumPokerSessionService.hasActiveSession(eq("KEY-1"))).thenReturn(true);
    }

    private void expectActiveScrumPokerSessionNotExists() {
        when(issue.getKey()).thenReturn("KEY-1");
        when(scrumPokerSessionService.hasActiveSession(eq("KEY-1"))).thenReturn(false);
    }

    private void expectThatIssueIsNotEstimable() {
        when(estimateFieldService.isEstimable(issue)).thenReturn(false);
    }

    private void expectThatIssueIsEstimable() {
        when(estimateFieldService.isEstimable(issue)).thenReturn(true);
    }

}
