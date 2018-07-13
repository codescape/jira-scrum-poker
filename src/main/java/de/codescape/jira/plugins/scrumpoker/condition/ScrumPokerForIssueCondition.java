package de.codescape.jira.plugins.scrumpoker.condition;

import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.plugin.webfragment.conditions.AbstractIssueWebCondition;
import com.atlassian.jira.plugin.webfragment.model.JiraHelper;
import com.atlassian.jira.user.ApplicationUser;
import de.codescape.jira.plugins.scrumpoker.service.StoryPointFieldSupport;

/**
 * This condition is used to decide whether a button to start a Scrum poker session should be displayed or not for the
 * given issue. The issue must be editable and have the custom field that is configured for Scrum poker estimations.
 */
public class ScrumPokerForIssueCondition extends AbstractIssueWebCondition {

    private final CustomFieldManager customFieldManager;
    private final StoryPointFieldSupport storyPointFieldSupport;

    public ScrumPokerForIssueCondition(CustomFieldManager customFieldManager,
                                       StoryPointFieldSupport storyPointFieldSupport) {
        this.customFieldManager = customFieldManager;
        this.storyPointFieldSupport = storyPointFieldSupport;
    }

    @Override
    public boolean shouldDisplay(ApplicationUser applicationUser, Issue issue, JiraHelper jiraHelper) {
        return issue.isEditable() && hasStoryPointField(issue);
    }

    private boolean hasStoryPointField(Issue currentIssue) {
        CustomField storyPointField = storyPointFieldSupport.findStoryPointField();
        return customFieldManager.getCustomFieldObjects(currentIssue).contains(storyPointField);
    }

}
