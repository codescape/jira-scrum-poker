package de.codescape.jira.plugins.scrumpoker.rest.entities;

import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * REST representation of selectable cards.
 */
@JsonSerialize
public class CardEntity {

    @JsonSerialize
    private String value;

    @JsonSerialize
    private boolean selected;

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
