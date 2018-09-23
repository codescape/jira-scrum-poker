package de.codescape.jira.plugins.scrumpoker.rest.entities;

import org.codehaus.jackson.annotate.JsonAutoDetect;

/**
 * REST representation of selectable cards.
 */
@JsonAutoDetect
public class CardEntity {

    private final String value;
    private final boolean selected;

    public CardEntity(String value, boolean selected) {
        this.value = value;
        this.selected = selected;
    }

    public String getValue() {
        return value;
    }

    public boolean isSelected() {
        return selected;
    }

}
