package de.codescape.jira.plugins.scrumpoker.condition;

import com.atlassian.jira.permission.GlobalPermissionKey;
import com.atlassian.jira.permission.ProjectPermissions;
import com.atlassian.jira.plugin.webfragment.conditions.AbstractWebCondition;
import com.atlassian.jira.plugin.webfragment.model.JiraHelper;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.security.GlobalPermissionManager;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;

import javax.inject.Inject;

/**
 * This condition ensures that the current user has either global administration or system administration rights or
 * has project administration rights for the current project explicitly.
 */
public class ProjectAdministrationCondition extends AbstractWebCondition {

    private final GlobalPermissionManager globalPermissionManager;
    private final PermissionManager permissionManager;

    @Inject
    public ProjectAdministrationCondition(@ComponentImport GlobalPermissionManager globalPermissionManager,
                                          @ComponentImport PermissionManager permissionManager) {
        this.globalPermissionManager = globalPermissionManager;
        this.permissionManager = permissionManager;
    }

    @Override
    public boolean shouldDisplay(ApplicationUser applicationUser, JiraHelper jiraHelper) {
        Project project = jiraHelper.getProject();
        return project != null && userIsAllowedToAdministrateProject(applicationUser, project);
    }

    /**
     * Verify that the provided applicationUser has permission to administrate the provided project.
     *
     * @param applicationUser application user to be checked
     * @param project         project
     * @return <code>true</code> if user has permission to administrate the project, otherwise <code>false</code>
     */
    public boolean userIsAllowedToAdministrateProject(ApplicationUser applicationUser, Project project) {
        return globalPermissionManager.hasPermission(GlobalPermissionKey.SYSTEM_ADMIN, applicationUser) ||
            globalPermissionManager.hasPermission(GlobalPermissionKey.ADMINISTER, applicationUser) ||
            permissionManager.hasPermission(ProjectPermissions.ADMINISTER_PROJECTS, project, applicationUser);
    }

}
