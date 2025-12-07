package de.codescape.jira.plugins.scrumpoker.rest;

import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.user.ApplicationUser;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerSession;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerVote;
import de.codescape.jira.plugins.scrumpoker.helper.ScrumPokerPermissions;
import de.codescape.jira.plugins.scrumpoker.model.AllowRevealDeck;
import de.codescape.jira.plugins.scrumpoker.model.Card;
import de.codescape.jira.plugins.scrumpoker.model.GlobalSettings;
import de.codescape.jira.plugins.scrumpoker.rest.entities.SessionEntity;
import de.codescape.jira.plugins.scrumpoker.rest.mapper.SessionEntityMapper;
import de.codescape.jira.plugins.scrumpoker.service.CardSetService;
import de.codescape.jira.plugins.scrumpoker.service.EstimateFieldService;
import de.codescape.jira.plugins.scrumpoker.service.GlobalSettingsService;
import de.codescape.jira.plugins.scrumpoker.service.ScrumPokerSessionService;
import jakarta.ws.rs.core.Response;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

public class SessionResourceTest {

    private static final String ISSUE_KEY = "ISSUE-1";
    private static final String USER_KEY = "userKey";
    private static final String OTHER_USER_KEY = "otherUserKey";
    private static final String CARD_VALUE = "5";
    private static final String OTHER_CARD_VALUE = "3";
    private static final String ESTIMATE = "5";

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private JiraAuthenticationContext jiraAuthenticationContext;

    @Mock
    private ScrumPokerSessionService scrumPokerSessionService;

    @Mock
    private EstimateFieldService estimateFieldService;

    @Mock
    private SessionEntityMapper sessionEntityMapper;

    @Mock
    private CardSetService cardSetService;

    @Mock
    private GlobalSettingsService globalSettingsService;

    @Mock
    private ScrumPokerPermissions scrumPokerPermissions;

    @InjectMocks
    private SessionResource sessionResource;

    @Mock
    private ApplicationUser applicationUser;

    @Mock
    private ScrumPokerSession scrumPokerSession;

    @Mock
    private SessionEntity sessionEntity;

    @Mock
    private GlobalSettings globalSettings;

    /* test for getSession */

    @Test
    public void getSessionShouldReturnSessionWhenUserIsAllowedToSeeTheIssue() {
        expectCurrentUserIs(USER_KEY);
        expectCurrentSessionForUser(ISSUE_KEY, USER_KEY);
        expectCurrentUserMaySeeIssue(ISSUE_KEY, true);

        SessionEntity sessionEntity = (SessionEntity) sessionResource.getSession(ISSUE_KEY).getEntity();

        assertThat(sessionEntity.getIssueKey(), is(equalTo(ISSUE_KEY)));
    }

    @Test
    public void getSessionShouldReturnForbiddenWhenUserIsNotAllowedToSeeTheIssue() {
        expectCurrentUserIs(USER_KEY);
        expectCurrentSessionForUser(ISSUE_KEY, USER_KEY);
        expectCurrentUserMaySeeIssue(ISSUE_KEY, false);

        Response response = sessionResource.getSession(ISSUE_KEY);

        assertThat(response.getStatus(), is(equalTo(Response.Status.FORBIDDEN.getStatusCode())));
    }

    /* tests for updateCard */

    @Test
    public void updateCardShouldUpdateTheCardForTheCurrentUser() {
        expectCurrentUserIs(USER_KEY);
        expectCurrentUserMaySeeIssue(ISSUE_KEY, true);
        expectCurrentSessionForUser(ISSUE_KEY, USER_KEY);
        expectCardToBePartOfCardSet(CARD_VALUE);

        sessionResource.updateCard(ISSUE_KEY, CARD_VALUE);

        verify(scrumPokerSessionService).addVote(ISSUE_KEY, USER_KEY, CARD_VALUE);
    }

    @Test
    public void updateCardShouldRejectAnUnknownCard() {
        expectCurrentUserIs(USER_KEY);
        expectCurrentUserMaySeeIssue(ISSUE_KEY, true);
        expectCurrentSessionForUser(ISSUE_KEY, USER_KEY);
        expectCardToBePartOfCardSet(OTHER_CARD_VALUE);

        Response response = sessionResource.updateCard(ISSUE_KEY, CARD_VALUE);

        assertThat(response.getStatus(), is(equalTo(Response.Status.FORBIDDEN.getStatusCode())));
    }

    @Test
    public void updateCardShouldRejectUserWhoMayNotSeeIssue() {
        expectCurrentUserIs(USER_KEY);
        expectCurrentUserMaySeeIssue(ISSUE_KEY, false);

        Response response = sessionResource.updateCard(ISSUE_KEY, CARD_VALUE);

        assertThat(response.getStatus(), is(equalTo(Response.Status.FORBIDDEN.getStatusCode())));
    }

    /* tests for revealCards */

    @Test
    public void revealingCardShouldRevealCardsForUnderlyingSession() {
        expectCurrentUserIs(USER_KEY);
        expectCurrentUserMaySeeIssue(ISSUE_KEY, true);
        expectCurrentSessionForUser(ISSUE_KEY, USER_KEY);
        when(scrumPokerSession.isVisible()).thenReturn(false);
        when(scrumPokerSession.getVotes()).thenReturn(new ScrumPokerVote[]{Mockito.mock(ScrumPokerVote.class)});
        when(globalSettingsService.load()).thenReturn(globalSettings);
        when(globalSettings.getAllowRevealDeck()).thenReturn(AllowRevealDeck.EVERYONE);

        sessionResource.revealCards(ISSUE_KEY);

        verify(scrumPokerSessionService, times(1)).reveal(ISSUE_KEY, USER_KEY);
    }

    /* tests for resetSession */

    @Test
    public void resettingSessionShouldResetTheUnderlyingSession() {
        expectCurrentUserIs(USER_KEY);
        expectCurrentUserMaySeeIssue(ISSUE_KEY, true);
        expectCurrentSessionForUser(ISSUE_KEY, USER_KEY);
        when(scrumPokerSession.isVisible()).thenReturn(true);
        when(scrumPokerSession.getVotes()).thenReturn(new ScrumPokerVote[]{Mockito.mock(ScrumPokerVote.class)});

        sessionResource.resetSession(ISSUE_KEY);

        verify(scrumPokerSessionService, times(1)).reset(ISSUE_KEY, USER_KEY);
    }

    /* tests for cancelSession */

    @Test
    public void cancellingSessionShouldCancelTheUnderlyingSession() {
        expectCurrentUserIs(USER_KEY);
        expectCurrentUserMaySeeIssue(ISSUE_KEY, true);
        expectCurrentSessionForUser(ISSUE_KEY, USER_KEY);
        when(scrumPokerSession.getCreatorUserKey()).thenReturn(USER_KEY);

        sessionResource.cancelSession(ISSUE_KEY);

        verify(scrumPokerSessionService, times(1)).cancel(ISSUE_KEY, USER_KEY);
    }

    @Test
    public void cancellingSessionShouldRejectUserWhoIsNotTheCreator() {
        expectCurrentUserIs(USER_KEY);
        expectCurrentUserMaySeeIssue(ISSUE_KEY, true);
        expectCurrentSessionForUser(ISSUE_KEY, USER_KEY);
        when(scrumPokerSession.getCreatorUserKey()).thenReturn(OTHER_USER_KEY);

        Response response = sessionResource.cancelSession(ISSUE_KEY);

        assertThat(response.getStatus(), is(equalTo(Response.Status.FORBIDDEN.getStatusCode())));
    }

    /* tests for confirmEstimation */

    @Test
    public void confirmingEstimateShouldConfirmEstimateInUnderlyingSessionAndPersistEstimate() {
        expectCurrentUserIs(USER_KEY);
        expectCurrentUserMaySeeIssue(ISSUE_KEY, true);
        expectCurrentSessionForUser(ISSUE_KEY, USER_KEY);
        when(scrumPokerSession.isVisible()).thenReturn(true);
        expectCardToBePartOfCardSet(ESTIMATE);
        when(globalSettingsService.load()).thenReturn(globalSettings);
        when(globalSettings.isCheckPermissionToSaveEstimate()).thenReturn(false);

        sessionResource.confirmEstimation(ISSUE_KEY, ESTIMATE);

        verify(scrumPokerSessionService, times(1)).confirm(ISSUE_KEY, USER_KEY, ESTIMATE);
        verify(estimateFieldService, times(1)).save(ISSUE_KEY, ESTIMATE);
    }

    /* helpers */

    private void expectCurrentUserIs(String userKey) {
        when(jiraAuthenticationContext.getLoggedInUser()).thenReturn(applicationUser);
        when(applicationUser.getKey()).thenReturn(userKey);
    }

    private void expectCurrentSessionForUser(String issueKey, String userKey) {
        when(scrumPokerSessionService.byIssueKey(issueKey, userKey)).thenReturn(scrumPokerSession);
        when(sessionEntityMapper.build(scrumPokerSession, USER_KEY)).thenReturn(sessionEntity);
        when(sessionEntity.getIssueKey()).thenReturn(ISSUE_KEY);
        when(scrumPokerSessionService.hasActiveSession(ISSUE_KEY)).thenReturn(true);
    }

    private void expectCurrentUserMaySeeIssue(String issueKey, boolean maySeeIssue) {
        when(scrumPokerPermissions.currentUserIsAllowedToSeeIssue(issueKey)).thenReturn(maySeeIssue);
    }

    private void expectCardToBePartOfCardSet(String cardValue) {
        when(cardSetService.getCardSet(scrumPokerSession)).thenReturn(List.of(new Card(cardValue, true)));
    }

}
