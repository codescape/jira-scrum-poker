package net.congstar.jira.plugins.scrumpoker.data;

import com.atlassian.jira.bc.issue.IssueService;
import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.IssueInputParameters;
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

    public DefaultStoryPointSupport(JiraAuthenticationContext context, PluginSettingsFactory pluginSettingsFactory,
                                    IssueService issueService, CustomFieldManager customFieldManager) {
        this.pluginSettingsFactory = pluginSettingsFactory;
        this.customFieldManager = customFieldManager;
        this.context = context;
        this.issueService = issueService;
    }

    @Override
    public void save(String issueKey, Double newValue) {
        log.info("Saving estimation {} for issue {}...", newValue, issueKey);
        ApplicationUser applicationUser = context.getUser();
        MutableIssue issue = issueService.getIssue(applicationUser, issueKey).getIssue();
        IssueInputParameters issueInputParameters = issueService.newIssueInputParameters();
        issueInputParameters.addCustomFieldValue(findStoryPointField().getIdAsLong(), formatAsNumber(newValue));

        IssueService.UpdateValidationResult validationResult = issueService.validateUpdate(applicationUser,
                issue.getId(), issueInputParameters);
        if (validationResult.getErrorCollection().hasAnyErrors()) {
            logErrors(issue.getKey(), validationResult.getErrorCollection());
        } else {
            IssueService.IssueResult updateResult = issueService.update(applicationUser, validationResult);
            if (updateResult.getErrorCollection().hasAnyErrors()) {
                logErrors(issue.getKey(), updateResult.getErrorCollection());
            }
        }
    }

    private String formatAsNumber(Double newValue) {
        return NumberFormat.getInstance().format(newValue);
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
        String storyPointFieldId = (String) settings.get(ConfigureScrumPokerAction.STORY_POINT_FIELD);
        return customFieldManager.getCustomFieldObject(storyPointFieldId);
    }

}
