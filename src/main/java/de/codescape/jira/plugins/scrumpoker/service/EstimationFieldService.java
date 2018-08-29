package de.codescape.jira.plugins.scrumpoker.service;

import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.UpdateIssueRequest;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Component to access and write the value of the configured custom field. This is typically the Story Point field
 * provided by Jira Software.
 */
@Named
@Scanned
public class EstimationFieldService {

    private static final Logger log = LoggerFactory.getLogger(EstimationFieldService.class);

    private final ScrumPokerSettingsService scrumPokerSettingsService;

    @ComponentImport
    private final CustomFieldManager customFieldManager;

    @ComponentImport
    private final JiraAuthenticationContext context;

    @ComponentImport
    private final IssueManager issueManager;

    @Inject
    public EstimationFieldService(JiraAuthenticationContext context,
                                  ScrumPokerSettingsService scrumPokerSettingsService,
                                  IssueManager issueManager,
                                  CustomFieldManager customFieldManager) {
        this.scrumPokerSettingsService = scrumPokerSettingsService;
        this.issueManager = issueManager;
        this.customFieldManager = customFieldManager;
        this.context = context;
    }

    /**
     * Save the estimation for a given issue.
     */
    public boolean save(String issueKey, Integer newValue) {
        ApplicationUser applicationUser = context.getLoggedInUser();
        MutableIssue issue = issueManager.getIssueByCurrentKey(issueKey);
        issue.setCustomFieldValue(findStoryPointField(), newValue.doubleValue());
        try {
            issueManager.updateIssue(applicationUser, issue, UpdateIssueRequest.builder().build());
            return true;
        } catch (RuntimeException e) {
            log.error("Could not save estimation {} for issue {}.", newValue, issueKey, e);
            return false;
        }
    }

    /**
     * Return the story point custom field.
     */
    public CustomField findStoryPointField() {
        return customFieldManager.getCustomFieldObject(scrumPokerSettingsService.loadStoryPointFieldId());
    }

}
