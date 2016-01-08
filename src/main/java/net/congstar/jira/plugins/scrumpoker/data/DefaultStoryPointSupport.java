package net.congstar.jira.plugins.scrumpoker.data;

import java.text.NumberFormat;
import java.util.Map;

import net.congstar.jira.plugins.scrumpoker.action.ConfigureScrumPokerAction;

import com.atlassian.jira.bc.issue.IssueService;
import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.IssueInputParameters;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.util.ErrorCollection;
import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;

public class DefaultStoryPointSupport implements StoryPointFieldSupport {

    private final PluginSettingsFactory pluginSettingsFactory;

    private final CustomFieldManager customFieldManager;

    private final JiraAuthenticationContext context;
    
    private final IssueService issueService;

    public DefaultStoryPointSupport(JiraAuthenticationContext context, IssueService issueService, IssueManager issueManager, PluginSettingsFactory pluginSettingsFactory, CustomFieldManager customFieldManager) {
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

    @Override
    public CustomField findStoryPointField() {
        PluginSettings settings = pluginSettingsFactory.createGlobalSettings();
        String field = (String) settings.get(ConfigureScrumPokerAction.STORY_POINT_FIELD_NAME);
        String storyPointFieldName = field != null ? field : ConfigureScrumPokerAction.DEFAULT_FIELD_FOR_STORY_POINTS;

        for (CustomField customField : customFieldManager.getCustomFieldObjects()) {
            if (customField.getNameKey().equalsIgnoreCase(storyPointFieldName)) {
                return customField;
            }
        }

        throw new IllegalStateException("Could not find custom field for story points.");
    }

}
