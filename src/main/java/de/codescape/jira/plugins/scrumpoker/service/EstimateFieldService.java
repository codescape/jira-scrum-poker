package de.codescape.jira.plugins.scrumpoker.service;

import com.atlassian.activeobjects.tx.Transactional;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.fields.CustomField;

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
     * Return whether the given issue has the estimate field configured.
     *
     * @param issue issue
     * @return <code>true</code> if estimate field exists, otherwise <code>false</code>
     */
    boolean hasEstimateField(Issue issue);

}
