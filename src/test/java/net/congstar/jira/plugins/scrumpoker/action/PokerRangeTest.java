package net.congstar.jira.plugins.scrumpoker.action;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import net.congstar.jira.plugins.scrumpoker.model.ScrumPokerSession;

import org.junit.Test;

public class PokerRangeTest {

    @Test
    public void minimumAndMaximumWithoutDuplicates() {
        ScrumPokerSession session = new ScrumPokerSession();

        session.updateCard("user1", "8");
        session.updateCard("user2", "2");
        session.updateCard("user3", "13");

        assertThat(PokerUtil.getMinVoted(session.getCards()), is("2"));
        assertThat(PokerUtil.getMaxVoted(session.getCards()), is("13"));
    }

    @Test
    public void minimumAndMaximumWithDuplicates() {
        ScrumPokerSession session = new ScrumPokerSession();

        session.updateCard("user1", "3");
        session.updateCard("user2", "3");
        session.updateCard("user3", "100");

        assertThat(PokerUtil.getMinVoted(session.getCards()), is("3"));
        assertThat(PokerUtil.getMaxVoted(session.getCards()), is("100"));
    }

    @Test
    public void minimumAndMaximumIgnoresQuestionmark() {
        ScrumPokerSession session = new ScrumPokerSession();

        session.updateCard("user1", "8");
        session.updateCard("user2", "?");
        session.updateCard("user3", "3");

        assertThat(PokerUtil.getMinVoted(session.getCards()), is("3"));
        assertThat(PokerUtil.getMaxVoted(session.getCards()), is("8"));
    }

}
