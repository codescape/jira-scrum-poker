package de.codescape.jira.plugins.scrumpoker.rest;

import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.user.ApplicationUser;
import de.codescape.jira.plugins.scrumpoker.model.ScrumPokerSession;
import de.codescape.jira.plugins.scrumpoker.persistence.ScrumPokerStorage;
import de.codescape.jira.plugins.scrumpoker.persistence.StoryPointFieldSupport;
import de.codescape.jira.plugins.scrumpoker.rest.entities.SessionRepresentation;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class SessionResourceTest {

    private static final String ISSUE_KEY = "ISSUE-1";
    private static final String USER_KEY = "userKey";
    private static final String CARD_VALUE = "5";
    private static final int ESTIMATION = 5;

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private JiraAuthenticationContext jiraAuthenticationContext;

    @Mock
    private ScrumPokerStorage scrumPokerStorage;

    @Mock
    private StoryPointFieldSupport storyPointFieldSupport;

    @InjectMocks
    private SessionResource sessionResource;

    @Mock
    private ApplicationUser applicationUser;

    @Mock
    private ScrumPokerSession scrumPokerSession;

    @Test
    public void getSessionShouldReturnSessionForTheGivenIssueKey() {
        expectCurrentUserIs(USER_KEY);
        expectCurrentSessionForUser(ISSUE_KEY, USER_KEY);

        SessionRepresentation sessionRepresentation = (SessionRepresentation) sessionResource.getSession(ISSUE_KEY).getEntity();

        assertThat(sessionRepresentation.getIssueKey(), is(equalTo(ISSUE_KEY)));
    }

    @Test
    public void updateCardShouldUpdateTheCardForTheCurrentUser() {
        expectCurrentUserIs(USER_KEY);
        expectCurrentSessionForUser(ISSUE_KEY, USER_KEY);

        sessionResource.updateCard(ISSUE_KEY, CARD_VALUE);

        verify(scrumPokerSession).updateCard(USER_KEY, CARD_VALUE);
    }

    @Test
    public void revealingCardShouldRevealCardsForUnderlyingSession() {
        expectCurrentUserIs(USER_KEY);
        expectCurrentSessionForUser(ISSUE_KEY, USER_KEY);

        sessionResource.revealCards(ISSUE_KEY);

        verify(scrumPokerSession, times(1)).revealDeck();
    }

    @Test
    public void resettingSessionShouldResetTheUnderlyingSession() {
        expectCurrentUserIs(USER_KEY);
        expectCurrentSessionForUser(ISSUE_KEY, USER_KEY);

        sessionResource.resetSession(ISSUE_KEY);

        verify(scrumPokerSession, times(1)).resetDeck();
    }

    @Test
    public void confirmingEstimationShouldConfirmEstimationInUnderlyingSessionAndPersistEstimation() {
        expectCurrentUserIs(USER_KEY);
        expectCurrentSessionForUser(ISSUE_KEY, USER_KEY);

        sessionResource.confirmEstimation(ISSUE_KEY, ESTIMATION);

        verify(scrumPokerSession, times(1)).confirm(ESTIMATION);
        verify(storyPointFieldSupport, times(1)).save(ISSUE_KEY, ESTIMATION);
    }

    private void expectCurrentUserIs(String userKey) {
        when(jiraAuthenticationContext.getLoggedInUser()).thenReturn(applicationUser);
        when(applicationUser.getKey()).thenReturn(userKey);
    }

    private void expectCurrentSessionForUser(String issueKey, String userKey) {
        when(scrumPokerStorage.sessionForIssue(issueKey, userKey)).thenReturn(scrumPokerSession);
        when(scrumPokerSession.getIssueKey()).thenReturn(issueKey);
    }

}
