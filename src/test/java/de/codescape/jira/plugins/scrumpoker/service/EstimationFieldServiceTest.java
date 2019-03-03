package de.codescape.jira.plugins.scrumpoker.service;

import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.UpdateIssueRequest;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.user.ApplicationUser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class EstimationFieldServiceTest {

    private static final String ISSUE_KEY = "ISSUE-0815";
    private static final Integer ESTIMATION = 5;
    private static final String CUSTOM_FIELD_ID = "11045";

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private JiraAuthenticationContext jiraAuthenticationContext;

    @Mock
    private IssueManager issueManager;

    @Mock
    private CustomFieldManager customFieldManager;

    @Mock
    private ScrumPokerSettingService scrumPokerSettingService;

    @InjectMocks
    private EstimationFieldService estimationFieldService;

    @Mock
    private ApplicationUser applicationUser;

    @Mock
    private CustomField customField;

    @Mock
    private MutableIssue issue;

    @Before
    public void before() {
        when(jiraAuthenticationContext.getLoggedInUser()).thenReturn(applicationUser);
        when(issueManager.getIssueByCurrentKey(ISSUE_KEY)).thenReturn(issue);
        when(scrumPokerSettingService.loadStoryPointField()).thenReturn(CUSTOM_FIELD_ID);
        when(customFieldManager.getCustomFieldObject(CUSTOM_FIELD_ID)).thenReturn(customField);
    }

    @Test
    public void withErrorsDuringUpdateItShouldReturnFalse() {
        when(issueManager.updateIssue(eq(applicationUser), eq(issue), any(UpdateIssueRequest.class)))
            .thenThrow(new RuntimeException());
        assertThat(estimationFieldService.save(ISSUE_KEY, ESTIMATION), is(false));
    }

    @Test
    public void withSuccessfulUpdateItShouldReturnTrue() {
        when(issueManager.updateIssue(eq(applicationUser), eq(issue), any(UpdateIssueRequest.class)))
            .thenReturn(issue);
        assertThat(estimationFieldService.save(ISSUE_KEY, ESTIMATION), is(true));
    }

}
