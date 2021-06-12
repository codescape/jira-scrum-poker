package de.codescape.jira.plugins.scrumpoker.jql;

import com.atlassian.jira.JiraDataTypes;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.jql.operand.QueryLiteral;
import com.atlassian.jira.jql.query.QueryCreationContext;
import com.atlassian.jira.plugin.jql.function.JqlFunctionModuleDescriptor;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.util.I18nHelper;
import com.atlassian.jira.util.MessageSet;
import com.atlassian.query.clause.TerminalClause;
import com.atlassian.query.operand.FunctionOperand;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerSession;
import de.codescape.jira.plugins.scrumpoker.service.ScrumPokerSessionService;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ActiveScrumPokerSessionsJqlFunctionTest {

    private static final String JQL_FUNCTION_ACCEPTS_NO_ARGUMENTS = "JQL function accepts no arguments!";

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private I18nHelper i18nHelper;

    @Mock
    private IssueManager issueManager;

    @Mock
    private ScrumPokerSessionService scrumPokerSessionService;

    @InjectMocks
    private ActiveScrumPokerSessionsJqlFunction jqlFunction;

    /* tests for validate() */

    @Test
    public void jqlFunctionValidationAcceptsZeroArguments() {
        withModuleDescriptorInitialized();
        MessageSet validation = jqlFunction.validate(
            mock(ApplicationUser.class),
            new FunctionOperand(ActiveScrumPokerSessionsJqlFunction.FUNCTION_NAME),
            mock(TerminalClause.class));
        assertThat(validation.getErrorMessages(), is(empty()));
    }

    @Test
    public void jqlFunctionValidationRejectsOneArgument() {
        withModuleDescriptorInitialized();
        expectErrorMessageFromValidation();
        MessageSet validationResults = jqlFunction.validate(
            mock(ApplicationUser.class),
            new FunctionOperand(ActiveScrumPokerSessionsJqlFunction.FUNCTION_NAME, "arg"),
            mock(TerminalClause.class));
        assertThat(validationResults.getErrorMessages(), contains(JQL_FUNCTION_ACCEPTS_NO_ARGUMENTS));
    }

    /* tests for getValues() */

    @Test
    public void jqlFunctionWorksForZeroActiveScrumPokerSessions() {
        List<QueryLiteral> activeScrumPokerSessions = jqlFunction.getValues(
            mock(QueryCreationContext.class),
            new FunctionOperand(ActiveScrumPokerSessionsJqlFunction.FUNCTION_NAME),
            mock(TerminalClause.class));
        assertThat(activeScrumPokerSessions.size(), is(0));
    }

    @Test
    public void jqlFunctionReturnsIdValueOfIssueKeyForActiveSession() {
        ScrumPokerSession scrumPokerSession = mock(ScrumPokerSession.class);
        when(scrumPokerSession.getIssueKey()).thenReturn("ISSUE-1");
        when(scrumPokerSession.isCancelled()).thenReturn(false);
        when(scrumPokerSession.getConfirmedEstimate()).thenReturn(null);
        when(scrumPokerSessionService.recent()).thenReturn(Collections.singletonList(scrumPokerSession));

        MutableIssue issue = mock(MutableIssue.class);
        when(issue.getId()).thenReturn(42L);
        when(issueManager.getIssueObject("ISSUE-1")).thenReturn(issue);

        List<QueryLiteral> activeScrumPokerSessions = jqlFunction.getValues(
            mock(QueryCreationContext.class),
            new FunctionOperand(ActiveScrumPokerSessionsJqlFunction.FUNCTION_NAME),
            mock(TerminalClause.class));
        assertThat(activeScrumPokerSessions.size(), is(1));
        assertThat(activeScrumPokerSessions.get(0).getLongValue(), is(equalTo(42L)));
    }

    /* tests for getMinimumNumberOfExpectedArguments() */

    @Test
    public void jqlFunctionRequiresNoArguments() {
        assertThat(jqlFunction.getMinimumNumberOfExpectedArguments(), is(equalTo(0)));
    }

    /* tests for getDataType() */

    @Test
    public void jqlFunctionCanOnlyBeUsedWithIssueSelector() {
        assertThat(jqlFunction.getDataType(), is(equalTo(JiraDataTypes.ISSUE)));
    }

    /* supporting methods */

    private void expectErrorMessageFromValidation() {
        when(i18nHelper.getText(eq("jira.jql.function.arg.incorrect.exact"), anyString(), eq("0"), eq("1")))
            .thenReturn(JQL_FUNCTION_ACCEPTS_NO_ARGUMENTS);
    }

    private void withModuleDescriptorInitialized() {
        JqlFunctionModuleDescriptor jqlFunctionModuleDescriptor = mock(JqlFunctionModuleDescriptor.class);
        when(jqlFunctionModuleDescriptor.getI18nBean()).thenReturn(i18nHelper);
        jqlFunction.init(jqlFunctionModuleDescriptor);
    }

}
