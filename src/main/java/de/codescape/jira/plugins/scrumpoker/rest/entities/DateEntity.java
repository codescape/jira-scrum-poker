package de.codescape.jira.plugins.scrumpoker.rest.entities;

import org.codehaus.jackson.annotate.JsonAutoDetect;

/**
 * REST representation of a date to be displayed during a Scrum poker session.
 */
@JsonAutoDetect
public class DateEntity {

    private final String displayValue;
    private final String formattedDate;

    public DateEntity(String displayValue, String formattedDate) {
        this.displayValue = displayValue;
        this.formattedDate = formattedDate;
    }

    public String getDisplayValue() {
        return displayValue;
    }

    public String getFormattedDate() {
        return formattedDate;
    }

}
