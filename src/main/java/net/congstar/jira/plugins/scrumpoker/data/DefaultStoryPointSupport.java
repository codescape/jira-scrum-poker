package net.congstar.jira.plugins.scrumpoker.data;

import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import net.congstar.jira.plugins.scrumpoker.action.ConfigurePlanningPokerAction;

import com.atlassian.jira.bc.issue.IssueService;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.crowd.embedded.api.User;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.issue.IssueInputParameters;
import com.atlassian.jira.util.ErrorCollection;
import java.util.Map;
import java.text.NumberFormat;

public class DefaultStoryPointSupport implements StoryPointFieldSupport {

    private final IssueManager issueManager;

    private final PluginSettingsFactory pluginSettingsFactory;

    private final CustomFieldManager customFieldManager;

    private final IssueService issueService;

    private final JiraAuthenticationContext context;

    public DefaultStoryPointSupport(JiraAuthenticationContext context, IssueService issueService, IssueManager issueManager, PluginSettingsFactory pluginSettingsFactory, CustomFieldManager customFieldManager) {
        this.issueManager = issueManager;
        this.pluginSettingsFactory = pluginSettingsFactory;
        this.customFieldManager = customFieldManager;
        this.context = context;

        this.issueService = issueService;
    }

    @Override
    public void save(String issueKey, Double newValue) {
        ApplicationUser appuser = context.getUser();
        IssueService.IssueResult issueResult = issueService.getIssue(appuser, issueKey);
        MutableIssue issue = issueResult.getIssue();

        IssueInputParameters issueInputParameters = issueService.newIssueInputParameters();
        CustomField customField = findStoryPointField();
        issueInputParameters.addCustomFieldValue(customField.getId(), NumberFormat.getInstance().format(newValue));
        IssueService.UpdateValidationResult updateValidationResult = issueService.validateUpdate(appuser, issue.getId(), issueInputParameters);

        if (updateValidationResult.isValid()) {
            IssueService.IssueResult updateResult = issueService.update(appuser, updateValidationResult);
            if (!updateResult.isValid()) {
                System.out.println("**** Issue update FAILED!");
                ErrorCollection errors = updateResult.getErrorCollection();
                if (errors.hasAnyErrors()) {
                    Map<String, String> messages = errors.getErrors();
                    for (Map.Entry<String, String> message : messages.entrySet()) {
                        System.out.println(message.getKey() + " : " + message.getValue());
                    }
                }
            }
        } else {
            System.out.println("**** Update validation FAILED!");
            ErrorCollection errors = updateValidationResult.getErrorCollection();
            if (errors.hasAnyErrors()) {
                Map<String, String> messages = errors.getErrors();
                for (Map.Entry<String, String> message : messages.entrySet()) {
                    System.out.println(message.getKey() + " : " + message.getValue());
                }
            }
        }
    }

    @Override
    public Double getValue(String issueKey) {
        ApplicationUser appuser = context.getUser();
        IssueService.IssueResult issueResult = issueService.getIssue(appuser, issueKey);
        if (!issueResult.isValid()) {
            System.out.println("**** Problem finding issue");
            return null;
        }

        MutableIssue issue = issueResult.getIssue();

        return (Double) issue.getCustomFieldValue(findStoryPointField());
    }

    private CustomField findStoryPointField() {
        PluginSettings settings = pluginSettingsFactory.createGlobalSettings();
        String field = (String) settings.get(ConfigurePlanningPokerAction.STORY_POINT_FIELD_NAME);
        String storyPointFieldName = field != null ? field : ConfigurePlanningPokerAction.DEFAULT_FIELD_FOR_STORY_POINTS;

        for (CustomField customField : customFieldManager.getCustomFieldObjects()) {
            if (customField.getNameKey().equalsIgnoreCase(storyPointFieldName)) {
                return customField;
            }
        }

        return null;
    }

}
