package de.codescape.jira.plugins.scrumpoker.service;

import com.atlassian.activeobjects.tx.Transactional;
import com.atlassian.jira.issue.fields.CustomField;

/**
 * Service to access and write the value of the configured estimation field. This is typically the Story Point field
 * provided by Jira Software.
 */
@Transactional
public interface EstimationFieldService {

    /**
     * Persist the estimation for a given issue.
     *
     * @param issueKey key of the issue
     * @param newValue new estimation value
     * @return <code>true</code> if value is persisted, otherwise <code>false</code>
     */
    boolean save(String issueKey, Integer newValue);

    /**
     * Return the story point custom field.
     *
     * @return the story point custom field
     */
    CustomField findStoryPointField();

}
