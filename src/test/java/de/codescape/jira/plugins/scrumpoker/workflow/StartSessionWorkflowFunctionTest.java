package de.codescape.jira.plugins.scrumpoker.workflow;


import com.atlassian.jira.issue.MutableIssue;
import com.opensymphony.module.propertyset.PropertySet;
import de.codescape.jira.plugins.scrumpoker.service.EstimateFieldService;
import de.codescape.jira.plugins.scrumpoker.service.ScrumPokerSessionService;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class StartSessionWorkflowFunctionTest {

    private static final String USER_KEY = "codescape";
    private static final String ISSUE_KEY = "ISSUE-1";

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private EstimateFieldService estimateFieldService;

    @Mock
    private ScrumPokerSessionService scrumPokerSessionService;

    @InjectMocks
    private StartSessionWorkflowFunction startSessionWorkflowFunction;

    @Mock
    @SuppressWarnings("rawtypes")
    private Map transientVars;

    @Mock
    @SuppressWarnings("rawtypes")
    private Map args;

    @Mock
    private PropertySet propertySet;

    @Mock
    private MutableIssue issue;

    @Test
    public void shouldNotStartSessionForNonEstimableIssue() {
        expectIssueExists();
        expectIssueIsEstimable(false);

        startSessionWorkflowFunction.execute(transientVars, args, propertySet);

        verify(scrumPokerSessionService, never()).byIssueKey(anyString(), anyString());
        verifyNoInteractions(scrumPokerSessionService);
    }

    @Test
    public void shouldNotStartSessionForEstimableIssueIfSessionAlreadyExists() {
        expectIssueExists();
        expectIssueIsEstimable(true);
        expectScrumPokerSessionExists(true);

        startSessionWorkflowFunction.execute(transientVars, args, propertySet);

        verify(scrumPokerSessionService).hasActiveSession(eq(ISSUE_KEY));
        verifyNoMoreInteractions(scrumPokerSessionService);
    }

    @Test
    public void shouldStartSessionForEstimableIssueIfNoSessionExists() {
        expectIssueExists();
        expectIssueIsEstimable(true);
        expectScrumPokerSessionExists(false);
        expectUserKeyExists();

        startSessionWorkflowFunction.execute(transientVars, args, propertySet);

        verify(scrumPokerSessionService).hasActiveSession(anyString());
        verify(scrumPokerSessionService).byIssueKey(eq(ISSUE_KEY), eq(USER_KEY));
        verifyNoMoreInteractions(scrumPokerSessionService);
    }

    /* supporting methods */

    private void expectUserKeyExists() {
        when(args.get("userKey")).thenReturn(USER_KEY);
    }

    private void expectScrumPokerSessionExists(boolean sessionExists) {
        when(scrumPokerSessionService.hasActiveSession(anyString())).thenReturn(sessionExists);
    }

    private void expectIssueIsEstimable(boolean estimableIssue) {
        when(estimateFieldService.isEstimable(eq(issue))).thenReturn(estimableIssue);
    }

    private void expectIssueExists() {
        when(transientVars.get(eq("issue"))).thenReturn(issue);
        when(issue.getKey()).thenReturn(ISSUE_KEY);
    }

}
