package de.codescape.jira.plugins.scrumpoker.data;

import com.atlassian.jira.bc.issue.IssueService;
import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.IssueInputParameters;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.util.ErrorCollection;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.HashMap;

import static org.mockito.Mockito.*;

public class DefaultStoryPointSupportTest {

    private static final String ISSUE_KEY = "ISSUE-0815";

    private static final Integer ESTIMATION = 5;

    private static final String CUSTOM_FIELD_ID = "11045";

    private static final long ISSUE_ID = 67L;

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private JiraAuthenticationContext jiraAuthenticationContext;

    @Mock
    private IssueService issueService;

    @Mock
    private CustomFieldManager customFieldManager;

    @Mock
    private ScrumPokerSettings scrumPokerSettings;

    @InjectMocks
    private DefaultStoryPointSupport defaultStoryPointSupport;

    @Mock
    private ApplicationUser applicationUser;

    @Mock
    private IssueService.IssueResult issueResult;

    @Mock
    private IssueInputParameters issueInputParameters;

    @Mock
    private CustomField customField;

    @Mock
    private MutableIssue issue;

    @Mock
    private IssueService.UpdateValidationResult updateValidationResult;

    @Mock
    private ErrorCollection validationErrorCollection;

    @Mock
    private IssueService.IssueResult updateIssueResult;

    @Mock
    private ErrorCollection updateErrorCollection;

    @Before
    public void before() {
        when(jiraAuthenticationContext.getLoggedInUser()).thenReturn(applicationUser);
        when(issueService.getIssue(applicationUser, ISSUE_KEY)).thenReturn(issueResult);
        when(issueResult.getIssue()).thenReturn(issue);
        when(issue.getId()).thenReturn(ISSUE_ID);
        when(issueService.newIssueInputParameters()).thenReturn(issueInputParameters);
        when(scrumPokerSettings.loadStoryPointFieldId()).thenReturn(CUSTOM_FIELD_ID);
        when(customFieldManager.getCustomFieldObject(CUSTOM_FIELD_ID)).thenReturn(customField);
        when(issueService.validateUpdate(applicationUser, ISSUE_ID, issueInputParameters)).thenReturn(updateValidationResult);
        when(updateValidationResult.getErrorCollection()).thenReturn(validationErrorCollection);
    }

    @Test
    public void withValidationErrorsIssueShouldNotBeUpdated() {
        when(validationErrorCollection.hasAnyErrors()).thenReturn(true);
        HashMap<String, String> errors = new HashMap<>();
        errors.put("KEY-1", "Message 1");
        errors.put("KEY-2", "Message 2");
        when(validationErrorCollection.getErrors()).thenReturn(errors);

        defaultStoryPointSupport.save(ISSUE_KEY, ESTIMATION);

        verify(issueService, never()).update(applicationUser, updateValidationResult);
    }

    @Test
    public void withoutValidationErrorsIssueShouldBeUpdated() {
        when(validationErrorCollection.hasAnyErrors()).thenReturn(false);
        when(issueService.update(applicationUser, updateValidationResult)).thenReturn(updateIssueResult);
        when(updateIssueResult.getErrorCollection()).thenReturn(updateErrorCollection);
        when(updateErrorCollection.hasAnyErrors()).thenReturn(false);

        defaultStoryPointSupport.save(ISSUE_KEY, ESTIMATION);

        verify(issueService, times(1)).update(applicationUser, updateValidationResult);
    }

}
