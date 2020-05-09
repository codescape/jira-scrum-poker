package de.codescape.jira.plugins.scrumpoker.rest.entities;

import org.codehaus.jackson.annotate.JsonAutoDetect;

/**
 * REST representation of selectable cards.
 */
@JsonAutoDetect
public class CardEntity {

    private final String value;
    private final boolean selected;
    private final boolean assignable;

    public CardEntity(String value, boolean selected, boolean assignable) {
        this.value = value;
        this.selected = selected;
        this.assignable = assignable;
    }

    public String getValue() {
        return value;
    }

    public boolean isSelected() {
        return selected;
    }

    public boolean isAssignable() {
        return assignable;
    }

}
