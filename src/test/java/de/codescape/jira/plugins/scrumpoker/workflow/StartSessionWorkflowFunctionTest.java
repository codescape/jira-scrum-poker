package de.codescape.jira.plugins.scrumpoker.workflow;


import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.upm.api.license.PluginLicenseManager;
import com.atlassian.upm.api.license.entity.LicenseError;
import com.atlassian.upm.api.license.entity.PluginLicense;
import com.atlassian.upm.api.util.Option;
import com.opensymphony.module.propertyset.PropertySet;
import de.codescape.jira.plugins.scrumpoker.service.ErrorLogService;
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

    @Mock
    private ErrorLogService errorLogService;

    @Mock
    private PluginLicenseManager pluginLicenseManager;

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

    @Mock
    private PluginLicense pluginLicense;

    @Test
    public void shouldNotStartSessionForLicenseErrors() {
        expectPluginLicenseHasErrors();

        startSessionWorkflowFunction.execute(transientVars, args, propertySet);

        verify(scrumPokerSessionService, never()).byIssueKey(anyString(), anyString());
        verifyNoInteractions(scrumPokerSessionService);
        verify(errorLogService, times(1)).logError(anyString());
    }

    @Test
    public void shouldNotStartSessionForMissingLicense() {
        expectPluginLicenseIsMissing();

        startSessionWorkflowFunction.execute(transientVars, args, propertySet);

        verify(scrumPokerSessionService, never()).byIssueKey(anyString(), anyString());
        verifyNoInteractions(scrumPokerSessionService);
        verify(errorLogService, times(1)).logError(anyString());
    }

    @Test
    public void shouldNotStartSessionForNonEstimableIssue() {
        expectPluginLicenseIsValid();
        expectIssueExists();
        expectIssueIsEstimable(false);

        startSessionWorkflowFunction.execute(transientVars, args, propertySet);

        verify(scrumPokerSessionService, never()).byIssueKey(anyString(), anyString());
        verifyNoInteractions(scrumPokerSessionService);
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
        when(pluginLicenseManager.getLicense()).thenReturn(Option.option(pluginLicense));
        when(pluginLicense.getError()).thenReturn(Option.none());
    }

    private void expectPluginLicenseHasErrors() {
        when(pluginLicenseManager.getLicense()).thenReturn(Option.option(pluginLicense));
        when(pluginLicense.getError()).thenReturn(Option.option(LicenseError.EXPIRED));
    }

    private void expectPluginLicenseIsMissing() {
        when(pluginLicenseManager.getLicense()).thenReturn(Option.none());
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
