package de.codescape.jira.plugins.scrumpoker.condition.helper;

import com.atlassian.jira.permission.GlobalPermissionKey;
import com.atlassian.jira.permission.ProjectPermissions;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.security.GlobalPermissionManager;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * This component allows to check for enough permissions to administrate a project. The current user has either global
 * administration or system administration rights or has project administration rights for the current project.
 */
@Component
public class UserHasProjectAdministrationPermission {

    private final GlobalPermissionManager globalPermissionManager;
    private final PermissionManager permissionManager;

    @Inject
    public UserHasProjectAdministrationPermission(@ComponentImport GlobalPermissionManager globalPermissionManager,
                                                  @ComponentImport PermissionManager permissionManager) {
        this.globalPermissionManager = globalPermissionManager;
        this.permissionManager = permissionManager;
    }

    /**
     * Returns true if the given user has enough permissions to administer the given project.
     *
     * @param applicationUser current user
     * @param project         current project
     * @return <code>true</code> if the user can administrate the project, otherwise <code>false</code>
     */
    public boolean verify(ApplicationUser applicationUser, Project project) {
        return globalPermissionManager.hasPermission(GlobalPermissionKey.SYSTEM_ADMIN, applicationUser) ||
            globalPermissionManager.hasPermission(GlobalPermissionKey.ADMINISTER, applicationUser) ||
            permissionManager.hasPermission(ProjectPermissions.ADMINISTER_PROJECTS, project, applicationUser);
    }

}
