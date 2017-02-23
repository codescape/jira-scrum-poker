package net.congstar.jira.plugins.scrumpoker.data;

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
import net.congstar.jira.plugins.scrumpoker.action.ConfigureScrumPokerAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.NumberFormat;
import java.util.Map;

public class DefaultStoryPointSupport implements StoryPointFieldSupport {

    private static final Logger log = LoggerFactory.getLogger(DefaultStoryPointSupport.class);

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
        ApplicationUser applicationUser = context.getUser();
        MutableIssue issue = issueService.getIssue(applicationUser, issueKey).getIssue();
        IssueInputParameters issueInputParameters = issueService.newIssueInputParameters();
        issueInputParameters.addCustomFieldValue(findStoryPointField().getId(), NumberFormat.getInstance().format(newValue));

        IssueService.UpdateValidationResult updateValidationResult = issueService.validateUpdate(applicationUser, issue.getId(), issueInputParameters);
        if (updateValidationResult.getErrorCollection().hasAnyErrors()) {
            logErrors(issue.getKey(), updateValidationResult.getErrorCollection());
        } else {
            IssueService.IssueResult updateResult = issueService.update(applicationUser, updateValidationResult);
            if (updateResult.getErrorCollection().hasAnyErrors()) {
                logErrors(issue.getKey(), updateResult.getErrorCollection());
            }
        }
    }

    private void logErrors(String issueKey, ErrorCollection errorCollection) {
        log.error("Failed to update issue {} with errors:", issueKey);
        if (errorCollection.hasAnyErrors()) {
            Map<String, String> errors = errorCollection.getErrors();
            for (Map.Entry<String, String> message : errors.entrySet()) {
                log.error("{}: {}", message.getKey(), message.getValue());
            }
        }
    }

    @Override
    public Double getValue(String issueKey) {
        ApplicationUser applicationUser = context.getUser();
        IssueService.IssueResult issueResult = issueService.getIssue(applicationUser, issueKey);
        if (issueResult.getErrorCollection().hasAnyErrors()) {
            log.warn("Could not find value for issue {}.", issueKey);
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
