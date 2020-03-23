package de.codescape.jira.plugins.scrumpoker.model;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class SpecialCardsTest {

    @Test
    public void questionMarkShouldHaveTheCorrectDatabaseIdentifier() {
        assertThat(SpecialCards.QUESTION_MARK, is(equalTo("question")));
    }

    @Test
    public void coffeeCardShouldHaveTheCorrectDatabaseIdentifier() {
        assertThat(SpecialCards.COFFEE_CARD, is(equalTo("coffee")));
    }

}
