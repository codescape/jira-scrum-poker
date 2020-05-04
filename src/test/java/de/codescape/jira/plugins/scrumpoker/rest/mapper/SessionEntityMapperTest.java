package de.codescape.jira.plugins.scrumpoker.rest.mapper;

import com.atlassian.jira.datetime.DateTimeFormatter;
import com.atlassian.jira.datetime.DateTimeStyle;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.permission.ProjectPermissions;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.user.util.UserManager;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerSession;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerVote;
import de.codescape.jira.plugins.scrumpoker.model.AllowRevealDeck;
import de.codescape.jira.plugins.scrumpoker.model.Card;
import de.codescape.jira.plugins.scrumpoker.model.GlobalSettings;
import de.codescape.jira.plugins.scrumpoker.rest.entities.SessionEntity;
import de.codescape.jira.plugins.scrumpoker.rest.entities.VoteEntity;
import de.codescape.jira.plugins.scrumpoker.service.CardSetService;
import de.codescape.jira.plugins.scrumpoker.service.GlobalSettingsService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Arrays;
import java.util.Date;

import static de.codescape.jira.plugins.scrumpoker.model.Card.COFFEE_BREAK;
import static de.codescape.jira.plugins.scrumpoker.model.Card.QUESTION_MARK;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class SessionEntityMapperTest {

    private static final String CURRENT_USER = "CURRENT_USER";
    private static final String ISSUE_KEY = "ISSUE-1";

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private UserManager userManager;

    @Mock
    private GlobalSettingsService globalSettingsService;

    @Mock
    private DateTimeFormatter dateTimeFormatter;

    @Mock
    private PermissionManager permissionManager;

    @Mock
    private IssueManager issueManager;

    @Mock
    private CardSetService cardSetService;

    @InjectMocks
    private SessionEntityMapper sessionEntityMapper;

    @Mock
    private ApplicationUser applicationUser;

    @Mock
    private GlobalSettings globalSettings;

    @Mock
    private MutableIssue issue;

    @Before
    public void before() {
        when(userManager.getUserByKey(anyString())).thenReturn(applicationUser);
        when(globalSettingsService.load()).thenReturn(globalSettings);
        when(globalSettings.getAllowRevealDeck()).thenReturn(AllowRevealDeck.EVERYONE);
        when(applicationUser.getDisplayName()).thenReturn("John Doe");
        DateTimeFormatter userSpecificDateTimeFormatter = mock(DateTimeFormatter.class);
        when(userSpecificDateTimeFormatter.withStyle(ArgumentMatchers.any(DateTimeStyle.class)))
            .thenReturn(userSpecificDateTimeFormatter);
        when(dateTimeFormatter.forLoggedInUser()).thenReturn(userSpecificDateTimeFormatter);
    }

    @Test
    public void shouldNotRequireConfirmedDateToBePresent() {
        ScrumPokerSession scrumPokerSession = scrumPokerSession(new ScrumPokerVote[]{}, false);
        scrumPokerSession.setConfirmedDate(null);

        SessionEntity sessionEntity = sessionEntityMapper.build(scrumPokerSession, CURRENT_USER);
        assertThat(sessionEntity.getConfirmedDate().getFormattedDate(), is(nullValue()));
        assertThat(sessionEntity.getConfirmedDate().getDisplayValue(), is(nullValue()));
    }

    @Test
    public void shouldNotRequireCreateDateToBePresent() {
        ScrumPokerSession scrumPokerSession = scrumPokerSession(new ScrumPokerVote[]{}, false);
        scrumPokerSession.setCreateDate(null);

        SessionEntity sessionEntity = sessionEntityMapper.build(scrumPokerSession, CURRENT_USER);
        assertThat(sessionEntity.getCreateDate().getFormattedDate(), is(nullValue()));
        assertThat(sessionEntity.getCreateDate().getDisplayValue(), is(nullValue()));
    }

    @Test
    public void shouldNotReturnAgreementForHiddenDeck() {
        ScrumPokerVote[] scrumPokerVotes = {scrumPokerVote("5"), scrumPokerVote("5")};
        ScrumPokerSession scrumPokerSession = scrumPokerSession(scrumPokerVotes, false);

        SessionEntity sessionEntity = sessionEntityMapper.build(scrumPokerSession, CURRENT_USER);
        assertThat(sessionEntity.isAgreementReached(), is(false));
    }

    @Test
    public void shouldReturnAgreementForVisibleDeckWithNonSpecialCards() {
        ScrumPokerVote[] scrumPokerVotes = {scrumPokerVote("5"), scrumPokerVote("5")};
        ScrumPokerSession scrumPokerSession = scrumPokerSession(scrumPokerVotes, true);

        SessionEntity sessionEntity = sessionEntityMapper.build(scrumPokerSession, CURRENT_USER);
        assertThat(sessionEntity.isAgreementReached(), is(true));
    }

    @Test
    public void shouldNotReturnAgreementForVisibleDeckWithDifferentVotes() {
        ScrumPokerVote[] scrumPokerVotes = {scrumPokerVote("5"), scrumPokerVote("3")};
        ScrumPokerSession scrumPokerSession = scrumPokerSession(scrumPokerVotes, true);

        SessionEntity sessionEntity = sessionEntityMapper.build(scrumPokerSession, CURRENT_USER);
        assertThat(sessionEntity.isAgreementReached(), is(false));
    }

    @Test
    public void shouldNotReturnAgreementForVisibleDeckWithSameSpecialCard() {
        ScrumPokerVote[] scrumPokerVotes = {
            scrumPokerVote(QUESTION_MARK.getValue()),
            scrumPokerVote(QUESTION_MARK.getValue())};
        ScrumPokerSession scrumPokerSession = scrumPokerSession(scrumPokerVotes, true);

        SessionEntity sessionEntity = sessionEntityMapper.build(scrumPokerSession, CURRENT_USER);
        assertThat(sessionEntity.isAgreementReached(), is(false));
    }

    @Test
    public void shouldReturnBoundedVotesWithCountForEachVoteInRange() {
        ScrumPokerVote[] scrumPokerVotes = {
            scrumPokerVote("5"),
            scrumPokerVote("5"),
            scrumPokerVote("13")};
        ScrumPokerSession scrumPokerSession = scrumPokerSession(scrumPokerVotes, true);
        when(cardSetService.getCardSet(ArgumentMatchers.any(ScrumPokerSession.class))).thenReturn(
            Arrays.asList(
                QUESTION_MARK,
                COFFEE_BREAK,
                new Card("0", true),
                new Card("1", true),
                new Card("2", true),
                new Card("3", true),
                new Card("5", true),
                new Card("8", true),
                new Card("13", true),
                new Card("20", true),
                new Card("40", true),
                new Card("100", true)));

        SessionEntity sessionEntity = sessionEntityMapper.build(scrumPokerSession, CURRENT_USER);
        assertThat(sessionEntity.getBoundedVotes().size(), is(equalTo(3)));
        assertThat(sessionEntity.getBoundedVotes(), allOf(
            hasItem(allOf(hasProperty("value", equalTo("5")), hasProperty("count", equalTo(2L)))),
            hasItem(allOf(hasProperty("value", equalTo("8")), hasProperty("count", equalTo(0L)))),
            hasItem(allOf(hasProperty("value", equalTo("13")), hasProperty("count", equalTo(1L))))
        ));
    }

    @Test
    public void shouldReturnBoundedVotesIncludingCoffeeBreakAndQuestionMark() {
        ScrumPokerVote[] scrumPokerVotes = {
            scrumPokerVote(COFFEE_BREAK.getValue()),
            scrumPokerVote(QUESTION_MARK.getValue()),
            scrumPokerVote("8")};
        ScrumPokerSession scrumPokerSession = scrumPokerSession(scrumPokerVotes, true);
        when(cardSetService.getCardSet(ArgumentMatchers.any(ScrumPokerSession.class))).thenReturn(
            Arrays.asList(
                QUESTION_MARK,
                COFFEE_BREAK,
                new Card("0", true),
                new Card("1", true),
                new Card("2", true),
                new Card("3", true),
                new Card("5", true),
                new Card("8", true),
                new Card("13", true),
                new Card("20", true),
                new Card("40", true),
                new Card("100", true)));

        SessionEntity sessionEntity = sessionEntityMapper.build(scrumPokerSession, CURRENT_USER);
        assertThat(sessionEntity.getBoundedVotes().size(), is(equalTo(3)));
        assertThat(sessionEntity.getBoundedVotes(), allOf(
            hasItem(allOf(hasProperty("value", equalTo(QUESTION_MARK.getValue())), hasProperty("count", equalTo(1L)))),
            hasItem(allOf(hasProperty("value", equalTo(COFFEE_BREAK.getValue())), hasProperty("count", equalTo(1L)))),
            hasItem(allOf(hasProperty("value", equalTo("8")), hasProperty("count", equalTo(1L))))
        ));
    }

    @Test
    public void shouldReturnBoundedVotesFromFirstVoteToLastVoteAndSpecialCardsWithMinimumOneVote() {
        ScrumPokerVote[] scrumPokerVotes = {
            scrumPokerVote("question"),
            scrumPokerVote("S"),
            scrumPokerVote("S"),
            scrumPokerVote("L")};
        ScrumPokerSession scrumPokerSession = scrumPokerSession(scrumPokerVotes, true);
        when(cardSetService.getCardSet(ArgumentMatchers.any(ScrumPokerSession.class))).thenReturn(
            Arrays.asList(
                QUESTION_MARK,
                COFFEE_BREAK,
                new Card("XS", true),
                new Card("S", true),
                new Card("M", true),
                new Card("L", true),
                new Card("XL", true),
                new Card("XXL", true)));

        SessionEntity sessionEntity = sessionEntityMapper.build(scrumPokerSession, CURRENT_USER);
        assertThat(sessionEntity.getBoundedVotes().size(), is(equalTo(4)));
        assertThat(sessionEntity.getBoundedVotes(), allOf(
            hasItem(allOf(hasProperty("value", equalTo("question")), hasProperty("count", equalTo(1L)))),
            hasItem(allOf(hasProperty("value", equalTo("S")), hasProperty("count", equalTo(2L)))),
            hasItem(allOf(hasProperty("value", equalTo("M")), hasProperty("count", equalTo(0L)))),
            hasItem(allOf(hasProperty("value", equalTo("L")), hasProperty("count", equalTo(1L))))
        ));
    }

    @Test
    public void shouldReturnBoundedVotesForOnlySpecialCardsProvided() {
        ScrumPokerVote[] scrumPokerVotes = {
            scrumPokerVote("question"),
            scrumPokerVote("coffee"),
            scrumPokerVote("question")};
        ScrumPokerSession scrumPokerSession = scrumPokerSession(scrumPokerVotes, true);
        when(cardSetService.getCardSet(ArgumentMatchers.any(ScrumPokerSession.class))).thenReturn(
            Arrays.asList(
                QUESTION_MARK,
                COFFEE_BREAK,
                new Card("S", true),
                new Card("M", true),
                new Card("L", true)));

        SessionEntity sessionEntity = sessionEntityMapper.build(scrumPokerSession, CURRENT_USER);
        assertThat(sessionEntity.getBoundedVotes().size(), is(equalTo(2)));
        assertThat(sessionEntity.getBoundedVotes(), allOf(
            hasItem(allOf(hasProperty("value", equalTo(QUESTION_MARK.getValue())), hasProperty("count", equalTo(2L)))),
            hasItem(allOf(hasProperty("value", equalTo(COFFEE_BREAK.getValue())), hasProperty("count", equalTo(1L))))
        ));
    }

    @Test
    public void isAllowRevealShouldDependOnCurrentVisibilityOfDeck() {
        reset(globalSettings);
        when(globalSettings.getAllowRevealDeck()).thenReturn(AllowRevealDeck.EVERYONE);
        ScrumPokerVote[] scrumPokerVotes = {scrumPokerVote("5")};

        ScrumPokerSession hiddenScrumPokerSession = scrumPokerSession(scrumPokerVotes, false);
        assertThat(sessionEntityMapper.build(hiddenScrumPokerSession, CURRENT_USER).isAllowReveal(), is(true));

        ScrumPokerSession visibleScrumPokerSession = scrumPokerSession(scrumPokerVotes, true);
        assertThat(sessionEntityMapper.build(visibleScrumPokerSession, CURRENT_USER).isAllowReveal(), is(false));
    }

    @Test
    public void isAllowRevealShouldDependOnExistenceOfVotes() {
        reset(globalSettings);
        when(globalSettings.getAllowRevealDeck()).thenReturn(AllowRevealDeck.EVERYONE);

        ScrumPokerVote[] oneScrumPokerVote = {scrumPokerVote("5")};
        ScrumPokerSession scrumPokerSessionWithVotes = scrumPokerSession(oneScrumPokerVote, false);
        assertThat(sessionEntityMapper.build(scrumPokerSessionWithVotes, CURRENT_USER).isAllowReveal(), is(true));

        ScrumPokerVote[] noScrumPokerVote = {};
        ScrumPokerSession scrumPokerSessionWithoutVotes = scrumPokerSession(noScrumPokerVote, false);
        assertThat(sessionEntityMapper.build(scrumPokerSessionWithoutVotes, CURRENT_USER).isAllowReveal(), is(false));
    }

    @Test
    public void isAllowRevealShouldDependOnAllowRevealSettingForSettingEveryone() {
        reset(globalSettings);
        when(globalSettings.getAllowRevealDeck()).thenReturn(AllowRevealDeck.EVERYONE);
        ScrumPokerVote[] scrumPokerVotesWithoutUser = {scrumPokerVote("5", "ANOTHER_USER"), scrumPokerVote("8", "ANOTHER_USER_2")};

        ScrumPokerSession scrumPokerSessionWithVotes = scrumPokerSession(scrumPokerVotesWithoutUser, false);
        assertThat(sessionEntityMapper.build(scrumPokerSessionWithVotes, CURRENT_USER).isAllowReveal(), is(true));
    }

    @Test
    public void isAllowRevealShouldDependOnAllowRevealSettingForSettingCreator() {
        reset(globalSettings);
        when(globalSettings.getAllowRevealDeck()).thenReturn(AllowRevealDeck.CREATOR);
        ScrumPokerVote[] scrumPokerVotesWithoutUser = {scrumPokerVote("5", "ANOTHER_USER")};

        ScrumPokerSession sessionWhereCurrentUserIsCreator = scrumPokerSession(scrumPokerVotesWithoutUser, false, CURRENT_USER);
        assertThat(sessionEntityMapper.build(sessionWhereCurrentUserIsCreator, CURRENT_USER).isAllowReveal(), is(true));

        ScrumPokerSession sessionWhereCurrentUserIsNotCreator = scrumPokerSession(scrumPokerVotesWithoutUser, false, "ANOTHER_USER");
        assertThat(sessionEntityMapper.build(sessionWhereCurrentUserIsNotCreator, CURRENT_USER).isAllowReveal(), is(false));
    }

    @Test
    public void isAllowRevealShouldDependOnAllowRevealSettingForSettingParticipant() {
        reset(globalSettings);
        when(globalSettings.getAllowRevealDeck()).thenReturn(AllowRevealDeck.PARTICIPANTS);

        ScrumPokerVote[] scrumPokerVotesWithoutUser = {scrumPokerVote("5", "ANOTHER_USER")};
        ScrumPokerSession sessionWhereCurrentUserIsCreator = scrumPokerSession(scrumPokerVotesWithoutUser, false, CURRENT_USER);
        assertThat(sessionEntityMapper.build(sessionWhereCurrentUserIsCreator, CURRENT_USER).isAllowReveal(), is(false));

        ScrumPokerVote[] scrumPokerVotesWithUser = {scrumPokerVote("5", "ANOTHER_USER"), scrumPokerVote("3", CURRENT_USER)};
        ScrumPokerSession sessionWhereCurrentUserIsNotCreator = scrumPokerSession(scrumPokerVotesWithUser, false, "ANOTHER_USER");
        assertThat(sessionEntityMapper.build(sessionWhereCurrentUserIsNotCreator, CURRENT_USER).isAllowReveal(), is(true));
    }

    @Test
    public void shouldSignalBreakIsNeededIfUserChoosesTheCoffeeCardAndDeckIsVisible() {
        ScrumPokerVote[] scrumPokerVotes = {scrumPokerVote(COFFEE_BREAK.getValue())};
        ScrumPokerSession scrumPokerSession = scrumPokerSession(scrumPokerVotes, true);
        SessionEntity sessionEntity = sessionEntityMapper.build(scrumPokerSession, CURRENT_USER);
        assertThat(sessionEntity.getVotes().stream().anyMatch(VoteEntity::isNeedABreak), is(true));
    }

    @Test
    public void shouldNotSignalBreakIsNeededIfUserChoosesTheCoffeeCardButDeckIsNotVisible() {
        ScrumPokerVote[] scrumPokerVotes = {scrumPokerVote(COFFEE_BREAK.getValue())};
        ScrumPokerSession scrumPokerSession = scrumPokerSession(scrumPokerVotes, false);
        SessionEntity sessionEntity = sessionEntityMapper.build(scrumPokerSession, CURRENT_USER);
        assertThat(sessionEntity.getVotes().stream().anyMatch(VoteEntity::isNeedABreak), is(false));
    }

    @Test
    public void shouldAlwaysAllowConfirmIfPermissionCheckIsDisabled() {
        when(globalSettings.isCheckPermissionToSaveEstimate()).thenReturn(false);
        ScrumPokerVote[] scrumPokerVotes = {scrumPokerVote("5", CURRENT_USER)};
        SessionEntity sessionEntity = sessionEntityMapper.build(scrumPokerSession(scrumPokerVotes, true, CURRENT_USER), CURRENT_USER);
        assertThat(sessionEntity.isAllowConfirm(), is(equalTo(true)));
    }

    @Test
    public void shouldAllowConfirmIfPermissionCheckIsEnabledAndUserHasPermission() {
        when(globalSettings.isCheckPermissionToSaveEstimate()).thenReturn(true);
        when(issueManager.getIssueObject(ISSUE_KEY)).thenReturn(issue);
        when(permissionManager.hasPermission(ProjectPermissions.EDIT_ISSUES, issue, applicationUser)).thenReturn(true);
        ScrumPokerVote[] scrumPokerVotes = {scrumPokerVote("5", CURRENT_USER)};
        SessionEntity sessionEntity = sessionEntityMapper.build(scrumPokerSession(scrumPokerVotes, true, CURRENT_USER), CURRENT_USER);
        assertThat(sessionEntity.isAllowConfirm(), is(equalTo(true)));
    }

    @Test
    public void shouldNotAllowConfirmIfPermissionCheckIsEnabledAndUserHasNoEditPermission() {
        when(globalSettings.isCheckPermissionToSaveEstimate()).thenReturn(true);
        when(issueManager.getIssueObject(ISSUE_KEY)).thenReturn(issue);
        when(permissionManager.hasPermission(ProjectPermissions.EDIT_ISSUES, issue, applicationUser)).thenReturn(false);
        ScrumPokerVote[] scrumPokerVotes = {scrumPokerVote("5", CURRENT_USER)};
        SessionEntity sessionEntity = sessionEntityMapper.build(scrumPokerSession(scrumPokerVotes, true, CURRENT_USER), CURRENT_USER);
        assertThat(sessionEntity.isAllowConfirm(), is(equalTo(false)));
    }

    private static ScrumPokerSession scrumPokerSession(ScrumPokerVote[] scrumPokerVotes, boolean visible) {
        return scrumPokerSession(scrumPokerVotes, visible, "CREATOR_USER");
    }

    private static ScrumPokerSession scrumPokerSession(ScrumPokerVote[] scrumPokerVotes, boolean visible, String creatorUserKey) {
        ScrumPokerSession scrumPokerSession = mock(ScrumPokerSession.class);
        when(scrumPokerSession.getVotes()).thenReturn(scrumPokerVotes);
        when(scrumPokerSession.getCreateDate()).thenReturn(new Date());
        when(scrumPokerSession.getCreatorUserKey()).thenReturn(creatorUserKey);
        when(scrumPokerSession.getIssueKey()).thenReturn(ISSUE_KEY);
        when(scrumPokerSession.isVisible()).thenReturn(visible);
        return scrumPokerSession;
    }

    private static ScrumPokerVote scrumPokerVote(String value) {
        return scrumPokerVote(value, "ANOTHER_USER");
    }

    private static ScrumPokerVote scrumPokerVote(String value, String userKey) {
        ScrumPokerVote vote = mock(ScrumPokerVote.class);
        when(vote.getVote()).thenReturn(value);
        when(vote.getUserKey()).thenReturn(userKey);
        return vote;
    }

}
