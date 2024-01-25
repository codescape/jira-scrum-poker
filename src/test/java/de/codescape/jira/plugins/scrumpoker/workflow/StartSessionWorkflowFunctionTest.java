package de.codescape.jira.plugins.scrumpoker.workflow;

import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.sal.api.message.I18nResolver;
import com.opensymphony.module.propertyset.PropertySet;
import de.codescape.jira.plugins.scrumpoker.service.ErrorLogService;
import de.codescape.jira.plugins.scrumpoker.service.EstimateFieldService;
import de.codescape.jira.plugins.scrumpoker.service.ScrumPokerLicenseService;
import de.codescape.jira.plugins.scrumpoker.service.ScrumPokerSessionService;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Map;

import static org.mockito.Mockito.*;

public class StartSessionWorkflowFunctionTest {

    private static final String USER_KEY = "codescape";
    private static final String ISSUE_KEY = "ISSUE-1";
    private static final String LICENSE_ERROR = "some.license.error";
    private static final String LICENSE_ERROR_TEXT = "Some license error text.";

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private EstimateFieldService estimateFieldService;

    @Mock
    private ScrumPokerSessionService scrumPokerSessionService;

    @Mock
    private ErrorLogService errorLogService;

    @Mock
    private ScrumPokerLicenseService scrumPokerLicenseService;

    @Mock
    private I18nResolver i18nResolver;

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

    /* tests for execute() */

    @Test
    public void shouldNotStartSessionWithInvalidLicense() {
        expectPluginLicenseIsInvalid();
        when(i18nResolver.getText(LICENSE_ERROR)).thenReturn(LICENSE_ERROR_TEXT);

        startSessionWorkflowFunction.execute(transientVars, args, propertySet);

        verify(scrumPokerSessionService, never()).byIssueKey(anyString(), anyString());
        verify(errorLogService, times(1)).logError(LICENSE_ERROR_TEXT);
    }

    @Test
    public void shouldNotStartSessionForNonEstimableIssue() {
        expectPluginLicenseIsValid();
        expectIssueExists();
        expectIssueIsEstimable(false);

        startSessionWorkflowFunction.execute(transientVars, args, propertySet);

        verify(scrumPokerSessionService, never()).byIssueKey(anyString(), anyString());
    }

    @Test
    public void shouldNotStartSessionForEstimableIssueIfSessionAlreadyExists() {
        expectPluginLicenseIsValid();
        expectIssueExists();
        expectIssueIsEstimable(true);
        expectScrumPokerSessionExists(true);

        startSessionWorkflowFunction.execute(transientVars, args, propertySet);

        verify(scrumPokerSessionService).hasActiveSession(eq(ISSUE_KEY));
        verifyNoMoreInteractions(scrumPokerSessionService);
    }

    @Test
    public void shouldStartSessionForEstimableIssueIfNoSessionExists() {
        expectPluginLicenseIsValid();
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

    private void expectPluginLicenseIsValid() {
        when(scrumPokerLicenseService.hasValidLicense()).thenReturn(true);
    }

    private void expectPluginLicenseIsInvalid() {
        when(scrumPokerLicenseService.hasValidLicense()).thenReturn(false);
        when(scrumPokerLicenseService.getLicenseError()).thenReturn(LICENSE_ERROR);
    }

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
