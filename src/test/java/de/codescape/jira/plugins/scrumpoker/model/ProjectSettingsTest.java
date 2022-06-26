package de.codescape.jira.plugins.scrumpoker.model;

import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerProject;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

public class ProjectSettingsTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private ScrumPokerProject scrumPokerProject;

    @Test
    public void projectSettingsCanBeCreatedFromActiveObjectUsingAllFields() {
        when(scrumPokerProject.getCardSet()).thenReturn("the card set");
        when(scrumPokerProject.getActivateScrumPoker()).thenReturn(ProjectActivation.ACTIVATE);
        when(scrumPokerProject.getEstimateField()).thenReturn("the field");

        ProjectSettings projectSettings = new ProjectSettings(scrumPokerProject);

        assertThat(projectSettings.getCardSet(), is(equalTo("the card set")));
        assertThat(projectSettings.getActivateScrumPoker(), is(equalTo(ProjectActivation.ACTIVATE)));
        assertThat(projectSettings.getEstimateField(), is(equalTo("the field")));
    }

}
