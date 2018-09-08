package de.codescape.jira.plugins.scrumpoker.service;

import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.user.util.UserManager;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerSession;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerVote;
import de.codescape.jira.plugins.scrumpoker.rest.entities.SessionEntity;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Date;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SessionEntityTransformerTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private UserManager userManager;

    @InjectMocks
    private SessionEntityTransformer sessionEntityTransformer;

    @Mock
    private ApplicationUser applicationUser;

    @Before
    public void before() {
        when(userManager.getUserByKey(anyString())).thenReturn(applicationUser);
        when(applicationUser.getDisplayName()).thenReturn("John Doe");
    }

    @Test
    public void shouldNotReturnAgreementForHiddenDeck() {
        ScrumPokerVote[] scrumPokerVotes = {scrumPokerVote("5"), scrumPokerVote("5")};
        ScrumPokerSession scrumPokerSession = scrumPokerSession(scrumPokerVotes, false);

        SessionEntity sessionEntity = sessionEntityTransformer.build(scrumPokerSession, "SOME_USER");
        assertThat(sessionEntity.isAgreementReached(), is(false));
    }

    @Test
    public void shouldReturnAgreementForVisibleDeck() {
        ScrumPokerVote[] scrumPokerVotes = {scrumPokerVote("5"), scrumPokerVote("5")};
        ScrumPokerSession scrumPokerSession = scrumPokerSession(scrumPokerVotes, true);

        SessionEntity sessionEntity = sessionEntityTransformer.build(scrumPokerSession, "SOME_USER");
        assertThat(sessionEntity.isAgreementReached(), is(true));
    }

    @Test
    public void shouldNotReturnAgreementForVisibleDeckWithDifferentVotes() {
        ScrumPokerVote[] scrumPokerVotes = {scrumPokerVote("5"), scrumPokerVote("3")};
        ScrumPokerSession scrumPokerSession = scrumPokerSession(scrumPokerVotes, true);

        SessionEntity sessionEntity = sessionEntityTransformer.build(scrumPokerSession, "SOME_USER");
        assertThat(sessionEntity.isAgreementReached(), is(false));
    }

    private ScrumPokerSession scrumPokerSession(ScrumPokerVote[] scrumPokerVotes, boolean visible) {
        ScrumPokerSession scrumPokerSession = mock(ScrumPokerSession.class);
        when(scrumPokerSession.getVotes()).thenReturn(scrumPokerVotes);
        when(scrumPokerSession.getCreateDate()).thenReturn(new Date());
        when(scrumPokerSession.getIssueKey()).thenReturn("ISSUE-1");
        when(scrumPokerSession.isVisible()).thenReturn(visible);
        return scrumPokerSession;
    }

    private ScrumPokerVote scrumPokerVote(String value) {
        ScrumPokerVote vote = mock(ScrumPokerVote.class);
        when(vote.getVote()).thenReturn(value);
        when(vote.getUserKey()).thenReturn("Another User");
        return vote;
    }

}
