package de.codescape.jira.plugins.scrumpoker.condition;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.plugin.webfragment.model.JiraHelper;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.user.ApplicationUser;
import de.codescape.jira.plugins.scrumpoker.model.GlobalSettings;
import de.codescape.jira.plugins.scrumpoker.service.EstimateFieldService;
import de.codescape.jira.plugins.scrumpoker.service.GlobalSettingsService;
import de.codescape.jira.plugins.scrumpoker.service.ProjectSettingsService;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ScrumPokerForIssueConditionTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private EstimateFieldService estimateFieldService;

    @Mock
    private GlobalSettingsService globalSettingsService;

    @Mock
    private ProjectSettingsService projectSettingsService;

    @Mock
    private ApplicationUser applicationUser;

    @Mock
    private JiraHelper jiraHelper;

    @InjectMocks
    private ScrumPokerForIssueCondition scrumPokerForIssueCondition;

    @Mock
    private CustomField estimateField;

    @Mock
    private CustomField someOtherField;

    @Mock
    private Issue issue;

    @Mock
    private Project project;

    @Mock
    private GlobalSettings globalSettings;

    @Test
    public void shouldDisplayForEditableIssueWithEstimateFieldAndGloballyActiveScrumPoker() {
        expectThatIssueContainsTheEstimateField();
        expectThatIssueIsEditable();
        expectScrumPokerGloballyActivated();
        assertThat(scrumPokerForIssueCondition.shouldDisplay(applicationUser, issue, jiraHelper), is(true));
    }

    @Test
    public void shouldDisplayForEditableIssueWithEstimateFieldAndGloballyInactiveScrumPokerButActiveForProject() {
        expectThatIssueContainsTheEstimateField();
        expectThatIssueIsEditable();
        expectScrumPokerNotGloballyActive();
        expectThatScrumPokerIsEnabledForProject();
        assertThat(scrumPokerForIssueCondition.shouldDisplay(applicationUser, issue, jiraHelper), is(true));
    }

    @Test
    public void shouldNotDisplayForEditableIssueWithEstimateFieldAndGloballyInactiveScrumPoker() {
        expectThatIssueContainsTheEstimateField();
        expectThatIssueIsEditable();
        expectScrumPokerNotGloballyActive();
        expectThatScrumPokerIsNotEnabledForProject();
        assertThat(scrumPokerForIssueCondition.shouldDisplay(applicationUser, issue, jiraHelper), is(false));
    }

    @Test
    public void shouldNotDisplayForNonEditableIssue() {
        expectThatIssueIsNotEditable();
        assertThat(scrumPokerForIssueCondition.shouldDisplay(applicationUser, issue, jiraHelper), is(false));
    }

    @Test
    public void shouldNotDisplayForEditableIssueWithoutEstimateField() {
        expectThatIssueDoesNotContainTheEstimateField();
        expectThatIssueIsEditable();
        assertThat(scrumPokerForIssueCondition.shouldDisplay(applicationUser, issue, jiraHelper), is(false));
    }

    private void expectThatScrumPokerIsEnabledForProject() {
        when(issue.getProjectObject()).thenReturn(project);
        when(project.getId()).thenReturn(42L);
        when(projectSettingsService.loadActivateScrumPoker(any())).thenReturn(true);
    }

    private void expectThatScrumPokerIsNotEnabledForProject() {
        when(issue.getProjectObject()).thenReturn(project);
        when(project.getId()).thenReturn(42L);
        when(projectSettingsService.loadActivateScrumPoker(any())).thenReturn(false);
    }

    private void expectThatIssueIsNotEditable() {
        when(issue.isEditable()).thenReturn(false);
    }

    private void expectThatIssueIsEditable() {
        when(issue.isEditable()).thenReturn(true);
    }

    private void expectThatIssueDoesNotContainTheEstimateField() {
        when(estimateFieldService.hasEstimateField(issue)).thenReturn(false);
    }

    private void expectThatIssueContainsTheEstimateField() {
        when(estimateFieldService.hasEstimateField(issue)).thenReturn(true);
    }

    private void expectScrumPokerGloballyActivated() {
        when(globalSettingsService.load()).thenReturn(globalSettings);
        when(globalSettings.isActivateScrumPoker()).thenReturn(true);
    }

    private void expectScrumPokerNotGloballyActive() {
        when(globalSettingsService.load()).thenReturn(globalSettings);
        when(globalSettings.isActivateScrumPoker()).thenReturn(false);
    }

}
