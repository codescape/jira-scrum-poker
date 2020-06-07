package de.codescape.jira.plugins.scrumpoker.service;

import com.atlassian.activeobjects.tx.Transactional;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.fields.CustomField;

import java.util.List;

/**
 * Service to access and write the value of the configured estimate field. This is typically the Story Point field
 * provided by Jira Software.
 */
@Transactional
public interface EstimateFieldService {

    /**
     * Persist the estimate for a given issue.
     *
     * @param issueKey key of the issue
     * @param estimate new estimate
     * @return <code>true</code> if value is persisted, otherwise <code>false</code>
     */
    boolean save(String issueKey, String estimate);

    /**
     * Return the estimate custom field.
     *
     * @return the estimate custom field
     */
    CustomField findEstimateField();

    /**
     * Returns whether an issue can be estimated. This means the issue is editable, has the estimate field and Scrum
     * Poker is either activated globally or for the project the issue is in.
     *
     * @param issue the issue
     * @return <code>true</code> if the issue can be estimated, otherwise <code>false</code>
     */
    boolean isEstimable(Issue issue);

    /**
     * Return a list of supported custom fields.
     *
     * @return list of supported custom fields
     */
    List<CustomField> supportedCustomFields();

}
