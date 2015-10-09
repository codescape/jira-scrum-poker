package net.congstar.jira.plugins.scrumpoker.model;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class ScrumPokerSessionTest {

    @Test
    public void shouldShowTheDeckAfterRevealing() {
        ScrumPokerSession session = new ScrumPokerSession();

        session.updateCard("user1", "8");
        session.revealDeck();

        assertThat(session.isVisible(), is(true));
    }

    @Test
    public void shouldHideTheDeckAfterUpdatingCard() {
        ScrumPokerSession session = new ScrumPokerSession();

        session.updateCard("user1", "8");
        session.revealDeck();
        session.updateCard("user1", "5");

        assertThat(session.isVisible(), is(false));
    }

    @Test
    public void shouldNotRevealAnEmptyDeck() {
        ScrumPokerSession session = new ScrumPokerSession();

        session.revealDeck();

        assertThat(session.isVisible(), is(false));
    }

    @Test
    public void shouldAddCardIsUserHasNoCardYet() {
        ScrumPokerSession session = new ScrumPokerSession();

        session.updateCard("user1", "8");
        session.updateCard("user2", "3");

        assertThat(session.getCards().size(), is(2));
        assertThat(session.getCards().get("user1"), is("8"));
        assertThat(session.getCards().get("user2"), is("3"));
    }

    @Test
    public void shouldUpdateExistingCardForSameUser() {
        ScrumPokerSession session = new ScrumPokerSession();

        session.updateCard("user1", "8");
        session.updateCard("user1", "3");

        assertThat(session.getCards().size(), is(1));
        assertThat(session.getCards().get("user1"), is("3"));
    }

    @Test
    public void shouldClearAllCardsWhenResettingTheDeck() {
        ScrumPokerSession session = new ScrumPokerSession();

        session.updateCard("user1", "8");
        session.resetDeck();

        assertThat(session.getCards().size(), is(0));
    }

    @Test
    public void shouldCalculateMinimumAndMaximumWithSimpleListOfCards() {
        ScrumPokerSession session = new ScrumPokerSession();

        session.updateCard("user1", "8");
        session.updateCard("user2", "2");
        session.updateCard("user3", "13");

        assertThat(session.getMinimumVote(), is("2"));
        assertThat(session.getMaximumVote(), is("13"));
    }

    @Test
    public void shouldCalculateMinimumAndMaximumWithDuplicateCards() {
        ScrumPokerSession session = new ScrumPokerSession();

        session.updateCard("user1", "3");
        session.updateCard("user2", "3");
        session.updateCard("user3", "100");

        assertThat(session.getMinimumVote(), is("3"));
        assertThat(session.getMaximumVote(), is("100"));
    }

    @Test
    public void shouldCalculateMinimumAndMaximumIgnoringQuestionmark() {
        ScrumPokerSession session = new ScrumPokerSession();

        session.updateCard("user1", "8");
        session.updateCard("user2", "?");
        session.updateCard("user3", "3");

        assertThat(session.getMinimumVote(), is("3"));
        assertThat(session.getMaximumVote(), is("8"));
    }

}
