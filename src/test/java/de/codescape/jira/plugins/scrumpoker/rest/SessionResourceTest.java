package de.codescape.jira.plugins.scrumpoker.rest;

import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.user.ApplicationUser;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerSession;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerVote;
import de.codescape.jira.plugins.scrumpoker.helper.ScrumPokerPermissions;
import de.codescape.jira.plugins.scrumpoker.model.AllowRevealDeck;
import de.codescape.jira.plugins.scrumpoker.model.Card;
import de.codescape.jira.plugins.scrumpoker.model.GlobalSettings;
import de.codescape.jira.plugins.scrumpoker.rest.entities.ReferenceListEntity;
import de.codescape.jira.plugins.scrumpoker.rest.entities.SessionEntity;
import de.codescape.jira.plugins.scrumpoker.rest.mapper.SessionEntityMapper;
import de.codescape.jira.plugins.scrumpoker.rest.mapper.SessionReferenceMapper;
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

import java.util.Arrays;
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
    private SessionReferenceMapper sessionReferenceMapper;

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
        givenCurrentUser(USER_KEY);
        givenCurrentUserCanSeeIssue(ISSUE_KEY, true);
        givenCurrentSessionCreatedByUser(ISSUE_KEY, USER_KEY);

        SessionEntity sessionEntity = (SessionEntity) sessionResource.getSession(ISSUE_KEY).getEntity();

        assertThat(sessionEntity.getIssueKey(), is(equalTo(ISSUE_KEY)));
    }

    @Test
    public void getSessionShouldReturnForbiddenWhenUserIsNotAllowedToSeeTheIssue() {
        givenCurrentUser(USER_KEY);
        givenCurrentUserCanSeeIssue(ISSUE_KEY, false);
        givenCurrentSessionCreatedByUser(ISSUE_KEY, OTHER_USER_KEY);

        Response response = sessionResource.getSession(ISSUE_KEY);

        assertThat(response.getStatus(), is(equalTo(Response.Status.FORBIDDEN.getStatusCode())));
    }

    /* tests for updateCard */

    @Test
    public void updateCardShouldUpdateTheCardForTheCurrentUser() {
        givenCurrentUser(USER_KEY);
        givenCurrentUserCanSeeIssue(ISSUE_KEY, true);
        givenCurrentSessionCreatedByUser(ISSUE_KEY, USER_KEY);
        givenCardsInCardSet(CARD_VALUE);

        sessionResource.updateCard(ISSUE_KEY, CARD_VALUE);

        verify(scrumPokerSessionService).addVote(ISSUE_KEY, USER_KEY, CARD_VALUE);
    }

    @Test
    public void updateCardShouldRejectAnUnknownCard() {
        givenCurrentUser(USER_KEY);
        givenCurrentUserCanSeeIssue(ISSUE_KEY, true);
        givenCurrentSessionCreatedByUser(ISSUE_KEY, USER_KEY);
        givenCardsInCardSet(OTHER_CARD_VALUE);

        Response response = sessionResource.updateCard(ISSUE_KEY, CARD_VALUE);

        assertThat(response.getStatus(), is(equalTo(Response.Status.FORBIDDEN.getStatusCode())));
    }

    @Test
    public void updateCardShouldRejectUserWhoMayNotSeeIssue() {
        givenCurrentUser(USER_KEY);
        givenCurrentUserCanSeeIssue(ISSUE_KEY, false);

        Response response = sessionResource.updateCard(ISSUE_KEY, CARD_VALUE);

        assertThat(response.getStatus(), is(equalTo(Response.Status.FORBIDDEN.getStatusCode())));
    }

    /* tests for revealCards */

    @Test
    public void revealingCardShouldRevealCardsForUnderlyingSession() {
        givenCurrentUser(USER_KEY);
        givenCurrentUserCanSeeIssue(ISSUE_KEY, true);
        givenCurrentSessionCreatedByUser(ISSUE_KEY, USER_KEY);
        givenCurrentSessionIsVisible(false);
        givenCurrentSessionHasVote();
        when(globalSettingsService.load()).thenReturn(globalSettings);
        when(globalSettings.getAllowRevealDeck()).thenReturn(AllowRevealDeck.EVERYONE);

        sessionResource.revealCards(ISSUE_KEY);

        verify(scrumPokerSessionService, times(1)).reveal(ISSUE_KEY, USER_KEY);
    }

    /* tests for resetSession */

    @Test
    public void resettingSessionShouldResetTheUnderlyingSession() {
        givenCurrentUser(USER_KEY);
        givenCurrentUserCanSeeIssue(ISSUE_KEY, true);
        givenCurrentSessionCreatedByUser(ISSUE_KEY, USER_KEY);
        givenCurrentSessionIsVisible(true);
        givenCurrentSessionHasVote();

        sessionResource.resetSession(ISSUE_KEY);

        verify(scrumPokerSessionService, times(1)).reset(ISSUE_KEY, USER_KEY);
    }

    /* tests for cancelSession */

    @Test
    public void cancellingSessionShouldCancelTheUnderlyingSession() {
        givenCurrentUser(USER_KEY);
        givenCurrentUserCanSeeIssue(ISSUE_KEY, true);
        givenCurrentSessionCreatedByUser(ISSUE_KEY, USER_KEY);

        sessionResource.cancelSession(ISSUE_KEY);

        verify(scrumPokerSessionService, times(1)).cancel(ISSUE_KEY, USER_KEY);
    }

    @Test
    public void cancellingSessionShouldRejectUserWhoIsNotTheCreator() {
        givenCurrentUser(USER_KEY);
        givenCurrentUserCanSeeIssue(ISSUE_KEY, true);
        givenCurrentSessionCreatedByUser(ISSUE_KEY, OTHER_USER_KEY);

        Response response = sessionResource.cancelSession(ISSUE_KEY);

        assertThat(response.getStatus(), is(equalTo(Response.Status.FORBIDDEN.getStatusCode())));
    }

    /* tests for confirmEstimation */

    @Test
    public void confirmingEstimateShouldConfirmEstimateInUnderlyingSessionAndPersistEstimate() {
        givenCurrentUser(USER_KEY);
        givenCurrentUserCanSeeIssue(ISSUE_KEY, true);
        givenCurrentSessionCreatedByUser(ISSUE_KEY, USER_KEY);
        givenCurrentSessionIsVisible(true);
        givenCardsInCardSet(ESTIMATE);
        when(globalSettingsService.load()).thenReturn(globalSettings);
        when(globalSettings.isCheckPermissionToSaveEstimate()).thenReturn(false);

        sessionResource.confirmEstimation(ISSUE_KEY, ESTIMATE);

        verify(scrumPokerSessionService, times(1)).confirm(ISSUE_KEY, USER_KEY, ESTIMATE);
        verify(estimateFieldService, times(1)).save(ISSUE_KEY, ESTIMATE);
    }

    /* tests for getReferences */

    @Test
    public void shouldReturnTheReferenceIssuesForTheUserWithTheSameEstimate() {
        givenCurrentUser(USER_KEY);
        List<ScrumPokerSession> referenceIssues = List.of(scrumPokerSession);
        when(scrumPokerSessionService.references(USER_KEY, ESTIMATE)).thenReturn(referenceIssues);
        when(sessionReferenceMapper.build(referenceIssues, ESTIMATE)).thenReturn(new ReferenceListEntity(List.of(), ESTIMATE));

        Response response = sessionResource.getReferences(ESTIMATE);

        assertThat(((ReferenceListEntity) response.getEntity()).getEstimate(), is(equalTo(ESTIMATE)));
    }

    /* helpers */

    private void givenCurrentUser(String userKey) {
        when(jiraAuthenticationContext.getLoggedInUser()).thenReturn(applicationUser);
        when(applicationUser.getKey()).thenReturn(userKey);
    }

    private void givenCurrentSessionCreatedByUser(String issueKey, String userKey) {
        when(scrumPokerSessionService.byIssueKey(eq(issueKey), anyString())).thenReturn(scrumPokerSession);
        when(sessionEntityMapper.build(scrumPokerSession, userKey)).thenReturn(sessionEntity);
        when(sessionEntity.getIssueKey()).thenReturn(issueKey);
        when(scrumPokerSession.getCreatorUserKey()).thenReturn(userKey);
        when(scrumPokerSessionService.hasActiveSession(issueKey)).thenReturn(true);
        when(scrumPokerSessionService.hasSession(issueKey)).thenReturn(true);
    }

    private void givenCurrentSessionIsVisible(boolean visible) {
        when(scrumPokerSession.isVisible()).thenReturn(visible);
    }

    private void givenCurrentUserCanSeeIssue(String issueKey, boolean maySeeIssue) {
        when(scrumPokerPermissions.currentUserIsAllowedToSeeIssue(issueKey)).thenReturn(maySeeIssue);
    }

    private void givenCardsInCardSet(String... cardValues) {
        when(cardSetService.getCardSet(scrumPokerSession)).thenReturn(
            Arrays.stream(cardValues).map(val -> new Card(val, true)).toList());
    }

    private void givenCurrentSessionHasVote() {
        when(scrumPokerSession.getVotes()).thenReturn(new ScrumPokerVote[]{Mockito.mock(ScrumPokerVote.class)});
    }

}
