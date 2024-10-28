package de.codescape.jira.plugins.scrumpoker.condition;

import com.atlassian.jira.plugin.webfragment.model.JiraHelper;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.user.ApplicationUser;
import de.codescape.jira.plugins.scrumpoker.condition.helper.UserHasProjectAdministrationPermission;
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
    private UserHasProjectAdministrationPermission userHasProjectAdministrationPermission;

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
    public void shouldRefuseAccessForUserWithMissingPermissions() {
        when(userHasProjectAdministrationPermission.verify(applicationUser, project)).thenReturn(false);
        assertThat(condition.shouldDisplay(applicationUser, jiraHelper), is(false));
    }

    @Test
    public void shouldGrantAccessForUserWithPermissions() {
        when(userHasProjectAdministrationPermission.verify(applicationUser, project)).thenReturn(true);
        assertThat(condition.shouldDisplay(applicationUser, jiraHelper), is(true));
    }

}
