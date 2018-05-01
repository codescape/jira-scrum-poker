package de.codescape.jira.plugins.scrumpoker.rest.entities;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class CardEntityTest {

    private static final String CARD_VALUE = "5";

    @Test
    public void cardReturnsValuesItIsInitializedWith() {
        CardEntity cardEntity = new CardEntity(CARD_VALUE, true);
        assertThat(cardEntity.getValue(), is(equalTo(CARD_VALUE)));
        assertThat(cardEntity.isSelected(), is(equalTo(true)));
    }

    @Test
    public void jsonRepresentationContainsAllFields() throws Exception {
        String json = new ObjectMapper().writeValueAsString(new CardEntity(CARD_VALUE, false));
        assertThat(json, containsString("value"));
        assertThat(json, containsString("selected"));
    }

}
