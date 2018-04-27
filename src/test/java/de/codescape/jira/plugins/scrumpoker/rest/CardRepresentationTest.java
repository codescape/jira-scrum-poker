package de.codescape.jira.plugins.scrumpoker.rest;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class CardRepresentationTest {

    private static final String CARD_VALUE = "5";

    @Test
    public void cardReturnsValuesItIsInitializedWith() {
        CardRepresentation cardRepresentation = new CardRepresentation(CARD_VALUE, true);
        assertThat(cardRepresentation.getValue(), is(equalTo(CARD_VALUE)));
        assertThat(cardRepresentation.isSelected(), is(equalTo(true)));
    }

}
