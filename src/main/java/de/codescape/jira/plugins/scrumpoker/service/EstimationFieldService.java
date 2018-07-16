package de.codescape.jira.plugins.scrumpoker.service;

import com.atlassian.jira.bc.issue.IssueService;
import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.IssueInputParameters;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;

import javax.inject.Inject;
import javax.inject.Named;
import java.text.NumberFormat;

/**
 * Component to access and write the value of the configured custom field. This is typically the Story Point field
 * provided by Jira Software.
 */
@Named
@Scanned
public class EstimationFieldService {

    private final ScrumPokerSettingsService scrumPokerSettingsService;

    @ComponentImport
    private final CustomFieldManager customFieldManager;

    @ComponentImport
    private final JiraAuthenticationContext context;

    @ComponentImport
    private final IssueService issueService;

    @Inject
    public EstimationFieldService(JiraAuthenticationContext context,
                                  ScrumPokerSettingsService scrumPokerSettingsService,
                                  IssueService issueService,
                                  CustomFieldManager customFieldManager) {
        this.scrumPokerSettingsService = scrumPokerSettingsService;
        this.customFieldManager = customFieldManager;
        this.context = context;
        this.issueService = issueService;
    }

    /**
     * Save the estimation for a given issue.
     */
    public void save(String issueKey, Integer newValue) {
        ApplicationUser applicationUser = context.getLoggedInUser();
        MutableIssue issue = issueService.getIssue(applicationUser, issueKey).getIssue();
        IssueInputParameters issueInputParameters = issueService.newIssueInputParameters();
        issueInputParameters.addCustomFieldValue(findStoryPointField().getIdAsLong(), formatAsNumber(newValue));

        IssueService.UpdateValidationResult validationResult = issueService.validateUpdate(applicationUser,
            issue.getId(), issueInputParameters);
        if (!validationResult.getErrorCollection().hasAnyErrors()) {
            issueService.update(applicationUser, validationResult);
        }
    }

    /**
     * Return the story point custom field.
     */
    public CustomField findStoryPointField() {
        return customFieldManager.getCustomFieldObject(scrumPokerSettingsService.loadStoryPointFieldId());
    }

    private String formatAsNumber(Integer newValue) {
        return NumberFormat.getInstance().format(newValue);
    }

}
