package de.codescape.jira.plugins.scrumpoker.data;

import com.atlassian.jira.bc.issue.IssueService;
import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.IssueInputParameters;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.user.ApplicationUser;

import java.text.NumberFormat;

public class DefaultStoryPointSupport implements StoryPointFieldSupport {

    private final ScrumPokerSettings scrumPokerSettings;
    private final CustomFieldManager customFieldManager;
    private final JiraAuthenticationContext context;
    private final IssueService issueService;

    public DefaultStoryPointSupport(JiraAuthenticationContext context, ScrumPokerSettings scrumPokerSettings,
                                    IssueService issueService, CustomFieldManager customFieldManager) {
        this.scrumPokerSettings = scrumPokerSettings;
        this.customFieldManager = customFieldManager;
        this.context = context;
        this.issueService = issueService;
    }

    @Override
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

    private String formatAsNumber(Integer newValue) {
        return NumberFormat.getInstance().format(newValue);
    }

    @Override
    public Integer getValue(String issueKey) {
        ApplicationUser applicationUser = context.getLoggedInUser();
        IssueService.IssueResult issueResult = issueService.getIssue(applicationUser, issueKey);
        if (issueResult.getErrorCollection().hasAnyErrors()) {
            return null;
        }
        MutableIssue issue = issueResult.getIssue();
        return (Integer) issue.getCustomFieldValue(findStoryPointField());
    }

    @Override
    public CustomField findStoryPointField() {
        return customFieldManager.getCustomFieldObject(scrumPokerSettings.loadStoryPointFieldId());
    }

}
