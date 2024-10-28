package de.codescape.jira.plugins.scrumpoker.condition;

import com.atlassian.jira.plugin.webfragment.conditions.AbstractWebCondition;
import com.atlassian.jira.plugin.webfragment.model.JiraHelper;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.user.ApplicationUser;
import de.codescape.jira.plugins.scrumpoker.condition.helper.UserHasProjectAdministrationPermission;

import javax.inject.Inject;

/**
 * This condition ensures that the current user has either global administration or system administration rights or
 * has project administration rights for the current project explicitly.
 */
public class ProjectAdministrationCondition extends AbstractWebCondition {

    private final UserHasProjectAdministrationPermission userHasProjectAdministrationPermission;

    @Inject
    public ProjectAdministrationCondition(UserHasProjectAdministrationPermission userHasProjectAdministrationPermission) {
        this.userHasProjectAdministrationPermission = userHasProjectAdministrationPermission;
    }

    @Override
    public boolean shouldDisplay(ApplicationUser applicationUser, JiraHelper jiraHelper) {
        Project project = jiraHelper.getProject();
        return project != null && userHasProjectAdministrationPermission.verify(applicationUser, project);
    }

}
