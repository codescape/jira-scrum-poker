package net.congstar.jira.plugins.scrumpoker.action;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Map;

import net.congstar.jira.plugins.scrumpoker.data.DefaultPlanningPokerStorage;

import org.junit.Test;

public class PokerRangeTest {

	private final static String ISSUE_ONE = "one";
	private final static String ISSUE_TWO = "two";
	@Test
	public void votesMinMax() {
		DefaultPlanningPokerStorage storage = new DefaultPlanningPokerStorage();
		
		storage.update(ISSUE_ONE, "user1", "8");
		storage.update(ISSUE_ONE, "user2", "2");
		storage.update(ISSUE_ONE, "user3", "13");
		
		Map<String, String> cardsForIssue = storage.chosenCardsForIssue(ISSUE_ONE);
		assertThat(PokerUtil.getMinVoted(cardsForIssue), is("2"));
		assertThat(PokerUtil.getMaxVoted(cardsForIssue), is("13"));
		
		storage.update(ISSUE_ONE, "user4", "0.5");
		assertThat(PokerUtil.getMinVoted(cardsForIssue), is("0.5"));
		assertThat(PokerUtil.getMaxVoted(cardsForIssue), is("13"));
		
		storage.update(ISSUE_ONE, "user5", "100");
		assertThat(PokerUtil.getMinVoted(cardsForIssue), is("0.5"));
		assertThat(PokerUtil.getMaxVoted(cardsForIssue), is("100"));

	}
	
	@Test
	public void votesSorting() {
		DefaultPlanningPokerStorage storage = new DefaultPlanningPokerStorage();
		
		storage.update(ISSUE_ONE, "user2", "13");
		storage.update(ISSUE_ONE, "user3", "8");
		storage.update(ISSUE_ONE, "user1", "1");
		
		Map<String, String> cardsForIssue = storage.chosenCardsForIssue(ISSUE_ONE);
		
		Iterator<BigDecimal> sortedVotes = PokerUtil.getSortedVotes(cardsForIssue).iterator();
		assertThat(sortedVotes.next(),is(new BigDecimal(1)));
		assertThat(sortedVotes.next(),is(new BigDecimal(8)));
		assertThat(sortedVotes.next(),is(new BigDecimal(13)));

		storage = new DefaultPlanningPokerStorage();
		storage.update(ISSUE_ONE, "user2", "13");
		storage.update(ISSUE_ONE, "user1", "8");
		storage.update(ISSUE_ONE, "user1", "1");
		cardsForIssue = storage.chosenCardsForIssue(ISSUE_ONE);
		
		sortedVotes = PokerUtil.getSortedVotes(cardsForIssue).iterator();
		assertThat(sortedVotes.next(),is(new BigDecimal(1)));
		assertThat(sortedVotes.next(),is(new BigDecimal(13)));

		storage = new DefaultPlanningPokerStorage();
		storage.update(ISSUE_ONE, "user2", "13");
		storage.update(ISSUE_ONE, "user1", "8");
		storage.update(ISSUE_TWO, "user1", "1");
		cardsForIssue = storage.chosenCardsForIssue(ISSUE_ONE);
		
		sortedVotes = PokerUtil.getSortedVotes(cardsForIssue).iterator();
		assertThat(sortedVotes.next(),is(new BigDecimal(8)));
		assertThat(sortedVotes.next(),is(new BigDecimal(13)));

		storage = new DefaultPlanningPokerStorage();
		storage.update(ISSUE_ONE, "user1", "100");
		storage.update(ISSUE_ONE, "user2", "40");
		storage.update(ISSUE_ONE, "user3", "20");
		storage.update(ISSUE_ONE, "user4", "13");
		storage.update(ISSUE_ONE, "user5", "8");
		storage.update(ISSUE_ONE, "user6", "5");
		storage.update(ISSUE_ONE, "user7", "3");
		storage.update(ISSUE_ONE, "user8", "2");
		storage.update(ISSUE_ONE, "user9", "1");
		storage.update(ISSUE_ONE, "user10", "0.5");
		
		cardsForIssue = storage.chosenCardsForIssue(ISSUE_ONE);
		
		sortedVotes = PokerUtil.getSortedVotes(cardsForIssue).iterator();
		assertThat(sortedVotes.next(),is(new BigDecimal(0.5)));
		assertThat(sortedVotes.next(),is(new BigDecimal(1)));
		assertThat(sortedVotes.next(),is(new BigDecimal(2)));
		assertThat(sortedVotes.next(),is(new BigDecimal(3)));
		assertThat(sortedVotes.next(),is(new BigDecimal(5)));
		assertThat(sortedVotes.next(),is(new BigDecimal(8)));
		assertThat(sortedVotes.next(),is(new BigDecimal(13)));
		assertThat(sortedVotes.next(),is(new BigDecimal(20)));
		assertThat(sortedVotes.next(),is(new BigDecimal(40)));
		assertThat(sortedVotes.next(),is(new BigDecimal(100)));
		
		storage = new DefaultPlanningPokerStorage();
		storage.update(ISSUE_ONE, "user1", "1");
		storage.update(ISSUE_ONE, "user2", "2");
		storage.update(ISSUE_ONE, "user3", "3");
		storage.update(ISSUE_ONE, "user4", "5");
		storage.update(ISSUE_ONE, "user5", "8");
		storage.update(ISSUE_ONE, "user6", "13");
		storage.update(ISSUE_ONE, "user7", "20");
		storage.update(ISSUE_ONE, "user8", "40");
		storage.update(ISSUE_ONE, "user9", "100");
		storage.update(ISSUE_ONE, "user10", "0.5");
		
		cardsForIssue = storage.chosenCardsForIssue(ISSUE_ONE);
		
		sortedVotes = PokerUtil.getSortedVotes(cardsForIssue).iterator();
		assertThat(sortedVotes.next(),is(new BigDecimal(0.5)));
		assertThat(sortedVotes.next(),is(new BigDecimal(1)));
		assertThat(sortedVotes.next(),is(new BigDecimal(2)));
		assertThat(sortedVotes.next(),is(new BigDecimal(3)));
		assertThat(sortedVotes.next(),is(new BigDecimal(5)));
		assertThat(sortedVotes.next(),is(new BigDecimal(8)));
		assertThat(sortedVotes.next(),is(new BigDecimal(13)));
		assertThat(sortedVotes.next(),is(new BigDecimal(20)));
		assertThat(sortedVotes.next(),is(new BigDecimal(40)));
		assertThat(sortedVotes.next(),is(new BigDecimal(100)));		
	}

}
