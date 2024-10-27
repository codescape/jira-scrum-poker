package de.codescape.jira.plugins.scrumpoker.jql;

import com.atlassian.jira.JiraDataType;
import com.atlassian.jira.JiraDataTypes;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.jql.operand.QueryLiteral;
import com.atlassian.jira.jql.query.QueryCreationContext;
import com.atlassian.jira.plugin.jql.function.AbstractJqlFunction;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.util.MessageSet;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.query.clause.TerminalClause;
import com.atlassian.query.operand.FunctionOperand;
import de.codescape.jira.plugins.scrumpoker.service.ScrumPokerSessionService;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * JQL function that can be used to query for active Scrum Poker sessions.
 * <p>
 * Usage: <code>issue in activeScrumPokerSessions()</code>
 */
@Component
public class ActiveScrumPokerSessionsJqlFunction extends AbstractJqlFunction {

    public static final String FUNCTION_NAME = "activeScrumPokerSessions";

    private final ScrumPokerSessionService scrumPokerSessionService;
    private final IssueManager issueManager;

    @Inject
    public ActiveScrumPokerSessionsJqlFunction(ScrumPokerSessionService scrumPokerSessionService,
                                               @ComponentImport IssueManager issueManager) {
        this.scrumPokerSessionService = scrumPokerSessionService;
        this.issueManager = issueManager;
    }

    @Nonnull
    @Override
    public MessageSet validate(ApplicationUser applicationUser,
                               @Nonnull FunctionOperand functionOperand,
                               @Nonnull TerminalClause terminalClause) {
        return validateNumberOfArgs(functionOperand, 0);
    }

    @Nonnull
    @Override
    public List<QueryLiteral> getValues(@Nonnull QueryCreationContext queryCreationContext,
                                        @Nonnull FunctionOperand functionOperand,
                                        @Nonnull TerminalClause terminalClause) {
        return scrumPokerSessionService.recent().stream()
            .filter(session -> session.getConfirmedEstimate() == null)
            .filter(session -> !session.isCancelled())
            .map(scrumPokerSession -> issueManager.getIssueObject(scrumPokerSession.getIssueKey()))
            .filter(Objects::nonNull)
            .map(issue -> new QueryLiteral(functionOperand, issue.getId()))
            .collect(Collectors.toList());
    }

    @Override
    public int getMinimumNumberOfExpectedArguments() {
        return 0;
    }

    @Nonnull
    @Override
    public JiraDataType getDataType() {
        return JiraDataTypes.ISSUE;
    }

}
