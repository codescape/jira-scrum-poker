package de.codescape.jira.plugins.scrumpoker.model;

import com.atlassian.jira.issue.fields.CustomField;

import java.util.Objects;

/**
 * An additional field represents a {@link CustomField} that can be displayed on the Scrum Poker session page. It is
 * marked as selected when it should be displayed.
 */
public class AdditionalField {

    private final CustomField customField;
    private final boolean selected;

    public AdditionalField(CustomField customField, boolean selected) {
        this.customField = customField;
        this.selected = selected;
    }

    public CustomField getCustomField() {
        return customField;
    }

    public boolean isSelected() {
        return selected;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AdditionalField that = (AdditionalField) o;
        return selected == that.selected &&
            customField.equals(that.customField);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customField, selected);
    }

}
