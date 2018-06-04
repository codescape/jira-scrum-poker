package de.codescape.jira.plugins.scrumpoker.action;

import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.fields.CustomField;
import de.codescape.jira.plugins.scrumpoker.persistence.ScrumPokerSettings;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

public class ConfigureScrumPokerActionTest {

    private static final String CUSTOM_FIELD_ID = "CustomFieldId";

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private CustomFieldManager customFieldManager;

    @Mock
    private ScrumPokerSettings scrumPokerSettings;

    @InjectMocks
    private ConfigureScrumPokerAction configureScrumPokerAction;

    @Mock
    private CustomField firstCustomField;

    @Mock
    private CustomField secondCustomField;

    @Test
    public void returnListOfCustomFieldsProvidedByCustomFieldManager() {
        when(customFieldManager.getCustomFieldObjects()).thenReturn(asList(firstCustomField, secondCustomField));
        assertThat(configureScrumPokerAction.getCustomFields(), hasItems(firstCustomField, secondCustomField));
    }

    @Test
    public void returnTheStoryPointFieldConfigured() {
        when(scrumPokerSettings.loadStoryPointFieldId()).thenReturn(CUSTOM_FIELD_ID);
        assertThat(configureScrumPokerAction.getStoryPointFieldId(), is(equalTo(CUSTOM_FIELD_ID)));
    }

}
