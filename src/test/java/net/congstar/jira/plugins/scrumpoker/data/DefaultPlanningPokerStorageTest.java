package net.congstar.jira.plugins.scrumpoker.data;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DefaultPlanningPokerStorageTest {

    private static final String ISSUE_ONE = "ISSUE-1";
    private static final String USER_ONE = "USER-ONE";
    private static final String USER_TWO = "USER-TWO";

    private DefaultPlanningPokerStorage defaultPlanningPokerStorage;

    @Before
    public void before() {
        defaultPlanningPokerStorage = new DefaultPlanningPokerStorage();
    }

    @Test
    public void cards_for_issue_hold_one_entry_per_user() {
        // given
        defaultPlanningPokerStorage.update(ISSUE_ONE, USER_ONE, "8");
        defaultPlanningPokerStorage.update(ISSUE_ONE, USER_TWO, "5");

        // when
        defaultPlanningPokerStorage.update(ISSUE_ONE, USER_TWO, "8");

        // then
        assertThat(defaultPlanningPokerStorage.chosenCardsForIssue(ISSUE_ONE).size(), is(equalTo(2)));
    }

    @Test
    public void cards_for_issue_are_not_visible_after_choosing_new_card() {
        // given
        defaultPlanningPokerStorage.update(ISSUE_ONE, USER_ONE, "8");
        defaultPlanningPokerStorage.revealDeck(ISSUE_ONE);
        assertThat(defaultPlanningPokerStorage.isVisible(ISSUE_ONE), is(true));

        // when
        defaultPlanningPokerStorage.update(ISSUE_ONE, USER_ONE, "5");

        // then
        assertThat(defaultPlanningPokerStorage.isVisible(ISSUE_ONE), is(false));
    }

}