package de.codescape.jira.plugins.scrumpoker.condition;

import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.plugin.webfragment.conditions.AbstractIssueWebCondition;
import com.atlassian.jira.plugin.webfragment.model.JiraHelper;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import de.codescape.jira.plugins.scrumpoker.service.EstimateFieldService;
import de.codescape.jira.plugins.scrumpoker.service.GlobalSettingsService;
import de.codescape.jira.plugins.scrumpoker.service.ProjectSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This condition is used to decide whether a button to start a Scrum Poker session should be displayed or not for the
 * given issue. The issue must be editable and have the custom field that is configured for Scrum Poker estimations.
 */
@Component
public class ScrumPokerForIssueCondition extends AbstractIssueWebCondition {

    private final CustomFieldManager customFieldManager;
    private final EstimateFieldService estimateFieldService;
    private final GlobalSettingsService globalSettingsService;
    private final ProjectSettingsService projectSettingsService;

    @Autowired
    public ScrumPokerForIssueCondition(@ComponentImport CustomFieldManager customFieldManager,
                                       EstimateFieldService estimateFieldService,
                                       GlobalSettingsService globalSettingsService,
                                       ProjectSettingsService projectSettingsService) {
        this.customFieldManager = customFieldManager;
        this.estimateFieldService = estimateFieldService;
        this.globalSettingsService = globalSettingsService;
        this.projectSettingsService = projectSettingsService;
    }

    @Override
    public boolean shouldDisplay(ApplicationUser applicationUser, Issue issue, JiraHelper jiraHelper) {
        return isEstimable(issue);
    }

    /**
     * Returns whether a Scrum Poker session can be started for the given issue.
     */
    public boolean isEstimable(Issue issue) {
        return issue.isEditable() && hasEstimateField(issue) && hasScrumPokerEnabled(issue.getProjectObject());
    }

    private boolean hasScrumPokerEnabled(Project project) {
        return globalSettingsService.load().isDefaultProjectActivation()
            || projectSettingsService.loadScrumPokerEnabled(project.getId());
    }

    // TODO move method to EstimateFieldService
    private boolean hasEstimateField(Issue currentIssue) {
        CustomField estimateField = estimateFieldService.findEstimateField();
        return customFieldManager.getCustomFieldObjects(currentIssue).contains(estimateField);
    }

}
