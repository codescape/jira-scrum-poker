package de.codescape.jira.plugins.scrumpoker.condition;

import com.atlassian.jira.permission.GlobalPermissionKey;
import com.atlassian.jira.permission.ProjectPermissions;
import com.atlassian.jira.plugin.webfragment.model.JiraHelper;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.security.GlobalPermissionManager;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.jira.user.ApplicationUser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

public class ProjectAdministrationConditionTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private GlobalPermissionManager globalPermissionManager;

    @Mock
    private PermissionManager permissionManager;

    @InjectMocks
    private ProjectAdministrationCondition condition;

    @Mock
    private ApplicationUser applicationUser;

    @Mock
    private Project project;

    @Mock
    private JiraHelper jiraHelper;

    @Before
    public void before() {
        when(jiraHelper.getProject()).thenReturn(project);
    }

    @Test
    public void shouldRefuseAccessForUserWithoutAnyAdministratorRole() {
        expectPermissions(false, false, false);
        assertThat(condition.shouldDisplay(applicationUser, jiraHelper), is(false));
    }

    @Test
    public void shouldGrantAccessForProjectAdministrator() {
        expectPermissions(false, false, true);
        assertThat(condition.shouldDisplay(applicationUser, jiraHelper), is(true));
    }

    @Test
    public void shouldGrantAccessForGlobalAdministrator() {
        expectPermissions(true, false, false);
        assertThat(condition.shouldDisplay(applicationUser, jiraHelper), is(true));
    }

    @Test
    public void shouldGrantAccessForSystemAdministrator() {
        expectPermissions(false, true, false);
        assertThat(condition.shouldDisplay(applicationUser, jiraHelper), is(true));
    }

    private void expectPermissions(boolean globalAdministrator, boolean systemAdministrator, boolean projectAdministrator) {
        when(globalPermissionManager.hasPermission(GlobalPermissionKey.SYSTEM_ADMIN, applicationUser)).thenReturn(systemAdministrator);
        if (!systemAdministrator) {
            when(globalPermissionManager.hasPermission(GlobalPermissionKey.ADMINISTER, applicationUser)).thenReturn(globalAdministrator);
            if (!globalAdministrator) {
                when(permissionManager.hasPermission(ProjectPermissions.ADMINISTER_PROJECTS, project, applicationUser)).thenReturn(projectAdministrator);
            }
        }
    }

}
