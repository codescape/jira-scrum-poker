package net.congstar.jira.plugins.scrumpoker.data;

import com.atlassian.jira.issue.fields.CustomField;

/**
 * Component to communicate with the Jira story point field.
 */
public interface StoryPointFieldSupport {

    /**
     * Save the estimation for a given issue.
     */
    void save(String issueKey, Double newValue);

    /**
     * Get the estimation for a given issue.
     */
    Double getValue(String issueKey);

    /**
     * Return the story point custom field.
     */
    CustomField findStoryPointField();

}
