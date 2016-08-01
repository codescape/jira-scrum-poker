package net.congstar.jira.plugins.scrumpoker.model;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class ScrumPokerSessionTest {

    private ScrumPokerSession session;

    @Before
    public void before() {
        session = new ScrumPokerSession();
    }

    @Test
    public void shouldShowTheDeckAfterRevealing() {
        session.updateCard("user1", "8");
        session.revealDeck();

        assertThat(session.isVisible(), is(true));
    }

    @Test
    public void shouldHideTheDeckAfterUpdatingCard() {
        session.updateCard("user1", "8");
        session.revealDeck();
        session.updateCard("user1", "5");

        assertThat(session.isVisible(), is(false));
    }

    @Test
    public void shouldNotRevealAnEmptyDeck() {
        session.revealDeck();

        assertThat(session.isVisible(), is(false));
    }

    @Test
    public void shouldAddCardIsUserHasNoCardYet() {
        session.updateCard("user1", "8");
        session.updateCard("user2", "3");

        assertThat(session.getCards().size(), is(2));
        assertThat(session.getCards().get("user1"), is("8"));
        assertThat(session.getCards().get("user2"), is("3"));
    }

    @Test
    public void shouldUpdateExistingCardForSameUser() {
        session.updateCard("user1", "8");
        session.updateCard("user1", "3");

        assertThat(session.getCards().size(), is(1));
        assertThat(session.getCards().get("user1"), is("3"));
    }

    @Test
    public void shouldClearAllCardsWhenResettingTheDeck() {
        session.updateCard("user1", "8");
        session.resetDeck();

        assertThat(session.getCards().size(), is(0));
    }

    @Test
    public void shouldCalculateMinimumAndMaximumWithSimpleListOfCards() {
        session.updateCard("user1", "8");
        session.updateCard("user2", "2");
        session.updateCard("user3", "13");

        assertThat(session.getMinimumVote(), is("2"));
        assertThat(session.getMaximumVote(), is("13"));
    }

    @Test
    public void shouldCalculateMinimumAndMaximumWithDuplicateCards() {
        session.updateCard("user1", "3");
        session.updateCard("user2", "3");
        session.updateCard("user3", "100");

        assertThat(session.getMinimumVote(), is("3"));
        assertThat(session.getMaximumVote(), is("100"));
    }

    @Test
    public void shouldCalculateMinimumAndMaximumIgnoringQuestionmark() {
        session.updateCard("user1", "8");
        session.updateCard("user2", "?");
        session.updateCard("user3", "3");

        assertThat(session.getMinimumVote(), is("3"));
        assertThat(session.getMaximumVote(), is("8"));
    }

    @Test
    public void shouldCalculateBoundedVotesForNormalRange() {
        session.updateCard("user1", "8");
        session.updateCard("user2", "?");
        session.updateCard("user3", "3");

        assertThat(session.getBoundedVotes(), is(equalTo(Arrays.asList("3", "5", "8"))));
    }

    @Test
    public void shouldCalculateBoundedVotesForWideRange() {
        session.updateCard("user1", "2");
        session.updateCard("user2", "?");
        session.updateCard("user3", "40");

        assertThat(session.getBoundedVotes(), is(equalTo(Arrays.asList("2", "3", "5", "8", "13", "20", "40"))));
    }

    @Test
    public void shouldCalculateBoundedVotesForExactMatch() {
        session.updateCard("user1", "2");
        session.updateCard("user2", "2");
        session.updateCard("user3", "2");

        assertThat(session.getBoundedVotes(), is(equalTo(Arrays.asList("2"))));
    }

}
