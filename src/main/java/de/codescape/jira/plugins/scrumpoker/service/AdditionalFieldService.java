package de.codescape.jira.plugins.scrumpoker.service;

import com.atlassian.activeobjects.tx.Transactional;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.fields.CustomField;
import de.codescape.jira.plugins.scrumpoker.model.AdditionalField;
import webwork.action.Action;

import java.util.List;

/**
 * Service to allow Scrum Poker for Jira to display additional fields on session view for better estimations.
 */
@Transactional
public interface AdditionalFieldService {

    /**
     * Renders the HTML to display the value of the custom field for the given issue.
     *
     * @param customField field that contains the value
     * @param action      action to display the value for
     * @param issue       issue that contains the value
     * @return HTML to display the value of the custom field for the given issue
     */
    String renderFieldValue(CustomField customField, Action action, MutableIssue issue);

    /**
     * Returns all custom fields that qualify to be displayed as an additional field and marks those as selected
     * that are already chosen.
     *
     * @return list of all supported custom fields
     */
    List<AdditionalField> supportedCustomFields();

    /**
     * Returns all custom fields that are configured to be displayed as an additional field.
     *
     * @return list of all configured custom fields
     */
    List<CustomField> configuredCustomFields();

}
