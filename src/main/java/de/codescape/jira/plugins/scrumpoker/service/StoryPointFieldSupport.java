package de.codescape.jira.plugins.scrumpoker.service;

import com.atlassian.jira.issue.fields.CustomField;

/**
 * Component to communicate with the Jira story point field.
 */
public interface StoryPointFieldSupport {

    /**
     * Save the estimation for a given issue.
     */
    void save(String issueKey, Integer newValue);

    /**
     * Get the estimation for a given issue.
     */
    Integer getValue(String issueKey);

    /**
     * Return the story point custom field.
     */
    CustomField findStoryPointField();

}
