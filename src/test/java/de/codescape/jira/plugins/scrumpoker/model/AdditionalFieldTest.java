package de.codescape.jira.plugins.scrumpoker.model;

import com.atlassian.jira.issue.fields.CustomField;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.mock;

public class AdditionalFieldTest {

    @Test
    public void getCustomFieldReturnsCustomField() {
        CustomField customField = mock(CustomField.class);
        assertThat(new AdditionalField(customField, false).getCustomField(), is(equalTo(customField)));
    }

    @Test
    public void isSelectedReturnsSelection() {
        CustomField customField = mock(CustomField.class);
        assertThat(new AdditionalField(customField, true).isSelected(), is(true));
    }

    @Test
    public void equalsReturnsTrueForSameFieldAndSelection() {
        CustomField customField = mock(CustomField.class);
        assertEquals(new AdditionalField(customField, true), (new AdditionalField(customField, true)));
    }

    @Test
    public void equalsReturnsFalseForSameFieldAndDifferentSelection() {
        CustomField customField = mock(CustomField.class);
        assertNotEquals(new AdditionalField(customField, true), (new AdditionalField(customField, false)));
    }

    @Test
    public void equalsReturnsFalseForDifferentFieldAndSameSelection() {
        CustomField customField = mock(CustomField.class);
        CustomField otherField = mock(CustomField.class);
        assertNotEquals(new AdditionalField(customField, true), (new AdditionalField(otherField, true)));
    }

    @Test
    public void equalsReturnsFalseForDifferentFieldAndDifferentSelection() {
        CustomField customField = mock(CustomField.class);
        CustomField otherField = mock(CustomField.class);
        assertNotEquals(new AdditionalField(customField, true), (new AdditionalField(otherField, false)));
    }

}
