package net.congstar.jira.plugins.scrumpoker;

import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.plugin.webfragment.conditions.AbstractWebCondition;
import com.atlassian.jira.plugin.webfragment.model.JiraHelper;
import com.atlassian.jira.user.ApplicationUser;
import net.congstar.jira.plugins.scrumpoker.data.StoryPointFieldSupport;

/**
 * Only display the planning poker web element for issues that have the custom field to hold the story points.
 */
public class StoryTypeCondition extends AbstractWebCondition {

    private final CustomFieldManager customFieldManager;

    private final StoryPointFieldSupport storyPointFieldSupport;

    public StoryTypeCondition(CustomFieldManager customFieldManager, StoryPointFieldSupport storyPointFieldSupport) {
        this.customFieldManager = customFieldManager;
        this.storyPointFieldSupport = storyPointFieldSupport;
    }

    @Override
    public boolean shouldDisplay(ApplicationUser applicationUser, JiraHelper jiraHelper) {
        Issue currentIssue = (Issue) jiraHelper.getContextParams().get("issue");
        return hasStoryPointField(currentIssue);
    }

    private boolean hasStoryPointField(Issue currentIssue) {
        CustomField storyPointField = storyPointFieldSupport.findStoryPointField();
        for (CustomField customField : customFieldManager.getCustomFieldObjects(currentIssue)) {
            if (customField.equals(storyPointField))
                return true;
        }
        return false;
    }

}
