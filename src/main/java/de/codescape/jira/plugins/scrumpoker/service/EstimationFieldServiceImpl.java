package de.codescape.jira.plugins.scrumpoker.service;

import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.UpdateIssueRequest;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.permission.ProjectPermissions;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Implementation of {@link EstimationFieldService} with access to Jira custom fields.
 */
@Component
public class EstimationFieldServiceImpl implements EstimationFieldService {

    private static final Logger log = LoggerFactory.getLogger(EstimationFieldServiceImpl.class);

    private final ScrumPokerSettingService scrumPokerSettingService;
    private final CustomFieldManager customFieldManager;
    private final JiraAuthenticationContext jiraAuthenticationContext;
    private final PermissionManager permissionManager;
    private final IssueManager issueManager;

    @Autowired
    public EstimationFieldServiceImpl(@ComponentImport JiraAuthenticationContext jiraAuthenticationContext,
                                      ScrumPokerSettingService scrumPokerSettingService,
                                      @ComponentImport IssueManager issueManager,
                                      @ComponentImport CustomFieldManager customFieldManager,
                                      @ComponentImport PermissionManager permissionManager) {
        this.scrumPokerSettingService = scrumPokerSettingService;
        this.issueManager = issueManager;
        this.customFieldManager = customFieldManager;
        this.jiraAuthenticationContext = jiraAuthenticationContext;
        this.permissionManager = permissionManager;
    }

    @Override
    public boolean save(String issueKey, Integer newValue) {
        ApplicationUser applicationUser = jiraAuthenticationContext.getLoggedInUser();
        MutableIssue issue = issueManager.getIssueByCurrentKey(issueKey);
        if (scrumPokerSettingService.load().isCheckPermissionToSaveEstimate() &&
            !permissionManager.hasPermission(ProjectPermissions.EDIT_ISSUES, issue, applicationUser)) {
            log.error("User {} is missing permissions to save estimation for issue {}.", applicationUser, issueKey);
            return false;
        }
        issue.setCustomFieldValue(findStoryPointField(), newValue.doubleValue());
        try {
            issueManager.updateIssue(applicationUser, issue, UpdateIssueRequest.builder().build());
            return true;
        } catch (RuntimeException e) {
            log.error("Unable to save estimation {} for issue {}.", newValue, issueKey, e);
            return false;
        }
    }

    @Override
    public CustomField findStoryPointField() {
        return customFieldManager.getCustomFieldObject(scrumPokerSettingService.load().getStoryPointField());
    }

}
