package de.codescape.jira.plugins.scrumpoker;

import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.plugin.webfragment.conditions.AbstractWebCondition;
import com.atlassian.jira.plugin.webfragment.model.JiraHelper;
import com.atlassian.jira.user.ApplicationUser;
import de.codescape.jira.plugins.scrumpoker.persistence.StoryPointFieldSupport;

/**
 * Only display the Scrum poker web element for issues that are editable and have the custom field to hold the story
 * points configured.
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
        return currentIssue.isEditable() && hasStoryPointField(currentIssue);
    }

    private boolean hasStoryPointField(Issue currentIssue) {
        CustomField storyPointField = storyPointFieldSupport.findStoryPointField();
        return customFieldManager.getCustomFieldObjects(currentIssue).contains(storyPointField);
    }

}
