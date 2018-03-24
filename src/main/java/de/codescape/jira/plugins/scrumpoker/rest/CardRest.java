package de.codescape.jira.plugins.scrumpoker.rest;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * REST representation of selectable cards.
 */
@XmlRootElement(name = "ScrumPokerSession")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class CardRest {

    private String value;

    private boolean selected;

    public CardRest(String value, boolean selected) {
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
