package de.codescape.jira.plugins.scrumpoker.condition;

import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.plugin.webfragment.conditions.AbstractIssueWebCondition;
import com.atlassian.jira.plugin.webfragment.model.JiraHelper;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import de.codescape.jira.plugins.scrumpoker.service.EstimationFieldService;
import de.codescape.jira.plugins.scrumpoker.service.ProjectSettingService;
import de.codescape.jira.plugins.scrumpoker.service.ScrumPokerSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This condition is used to decide whether a button to start a Scrum poker session should be displayed or not for the
 * given issue. The issue must be editable and have the custom field that is configured for Scrum poker estimations.
 */
@Component
public class ScrumPokerForIssueCondition extends AbstractIssueWebCondition {

    private final CustomFieldManager customFieldManager;
    private final EstimationFieldService estimationFieldService;
    private final ScrumPokerSettingService scrumPokerSettingService;
    private final ProjectSettingService projectSettingService;

    @Autowired
    public ScrumPokerForIssueCondition(@ComponentImport CustomFieldManager customFieldManager,
                                       EstimationFieldService estimationFieldService,
                                       ScrumPokerSettingService scrumPokerSettingService,
                                       ProjectSettingService projectSettingService) {
        this.customFieldManager = customFieldManager;
        this.estimationFieldService = estimationFieldService;
        this.scrumPokerSettingService = scrumPokerSettingService;
        this.projectSettingService = projectSettingService;
    }

    @Override
    public boolean shouldDisplay(ApplicationUser applicationUser, Issue issue, JiraHelper jiraHelper) {
        return isEstimable(issue);
    }

    /**
     * Returns whether a Scrum poker session can be started for the given issue.
     */
    public boolean isEstimable(Issue issue) {
        return issue.isEditable() && hasStoryPointField(issue) && hasScrumPokerEnabled(issue.getProjectObject());
    }

    private boolean hasScrumPokerEnabled(Project project) {
        return scrumPokerSettingService.loadDefaultProjectActivation()
            || projectSettingService.loadScrumPokerEnabled(project.getId());
    }

    private boolean hasStoryPointField(Issue currentIssue) {
        CustomField storyPointField = estimationFieldService.findStoryPointField();
        return customFieldManager.getCustomFieldObjects(currentIssue).contains(storyPointField);
    }

}
