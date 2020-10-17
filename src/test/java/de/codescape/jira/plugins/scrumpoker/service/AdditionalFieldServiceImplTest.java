package de.codescape.jira.plugins.scrumpoker.service;

import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.fields.layout.field.FieldLayout;
import com.atlassian.jira.issue.fields.layout.field.FieldLayoutItem;
import com.atlassian.jira.issue.fields.layout.field.FieldLayoutManager;
import de.codescape.jira.plugins.scrumpoker.model.AdditionalField;
import de.codescape.jira.plugins.scrumpoker.model.GlobalSettings;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import webwork.action.Action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

public class AdditionalFieldServiceImplTest {

    private static final String MISSING_FIELD = "MISSING_FIELD";
    private static final String EXISTING_FIELD = "EXISTING_FIELD";

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private GlobalSettingsService globalSettingsService;

    @Mock
    private FieldLayoutManager fieldLayoutManager;

    @Mock
    private CustomFieldManager customFieldManager;

    @InjectMocks
    private AdditionalFieldServiceImpl service;

    @Mock
    private CustomField customField;

    @Mock
    private GlobalSettings globalSettings;

    @Mock
    private MutableIssue issue;

    @Mock
    private Action action;

    @Mock
    private FieldLayout fieldLayout;

    @Mock
    private FieldLayoutItem fieldLayoutItem;

    /* tests for supportedCustomFields() */

    @Test
    public void supportedCustomFieldsPreservesOrderOfConfiguredCustomFields() {
        when(globalSettingsService.load()).thenReturn(globalSettings);
        when(globalSettings.getAdditionalFields()).thenReturn("Third Field,Second Field");
        CustomField field1 = mockCustomField("First Field");
        CustomField field2 = mockCustomField("Second Field");
        CustomField field3 = mockCustomField("Third Field");
        CustomField field4 = mockCustomField("Fourth Field");
        ArrayList<CustomField> customFields = new ArrayList<>(Arrays.asList(field1, field2, field3, field4));
        when(customFieldManager.getCustomFieldObjects()).thenReturn(customFields);

        List<AdditionalField> fields = service.supportedCustomFields();

        assertThat(fields.size(), is(4));
        assertThat(fields, contains(
            new AdditionalField(field3, true),
            new AdditionalField(field2, true),
            new AdditionalField(field1, false),
            new AdditionalField(field4, false)));
    }

    @Test
    public void supportedCustomFieldsPreservesOrderOfExistingCustomFields() {
        when(globalSettingsService.load()).thenReturn(globalSettings);
        when(globalSettings.getAdditionalFields()).thenReturn(null);
        CustomField field1 = mockCustomField("First Field");
        CustomField field2 = mockCustomField("Second Field");
        CustomField field3 = mockCustomField("Third Field");
        CustomField field4 = mockCustomField("Fourth Field");
        ArrayList<CustomField> customFields = new ArrayList<>(Arrays.asList(field1, field2, field3, field4));
        when(customFieldManager.getCustomFieldObjects()).thenReturn(customFields);

        List<AdditionalField> fields = service.supportedCustomFields();

        assertThat(fields.size(), is(4));
        assertThat(fields, contains(
            new AdditionalField(field1, false),
            new AdditionalField(field2, false),
            new AdditionalField(field3, false),
            new AdditionalField(field4, false)));
    }

    /* tests for configuredCustomFields() */

    @Test
    public void configuredCustomFieldsIgnoresMissingFields() {
        when(globalSettingsService.load()).thenReturn(globalSettings);
        when(globalSettings.getAdditionalFields()).thenReturn(MISSING_FIELD);

        List<CustomField> fields = service.configuredCustomFields();

        assertThat(fields.size(), is(0));
    }

    @Test
    public void configuredCustomFieldsReturnsExistingFields() {
        when(globalSettingsService.load()).thenReturn(globalSettings);
        when(globalSettings.getAdditionalFields()).thenReturn(EXISTING_FIELD);
        when(customFieldManager.getCustomFieldObject(EXISTING_FIELD)).thenReturn(customField);

        List<CustomField> fields = service.configuredCustomFields();

        assertThat(fields.size(), is(1));
        assertThat(fields, contains(customField));
    }

    /* tests for renderFieldValue() */

    @Test
    public void renderFieldValue() {
        when(fieldLayoutManager.getFieldLayout(issue)).thenReturn(fieldLayout);
        when(fieldLayout.getFieldLayoutItem(customField)).thenReturn(fieldLayoutItem);
        when(customField.getViewHtml(fieldLayoutItem, action, issue)).thenReturn("<p>Result</p>");

        String result = service.renderFieldValue(customField, action, issue);

        assertThat(result, is(equalTo("<p>Result</p>")));
        verify(fieldLayoutManager, times(1)).getFieldLayout(issue);
        verify(fieldLayout, times(1)).getFieldLayoutItem(customField);
        verify(customField, times(1)).getViewHtml(fieldLayoutItem, action, issue);
    }

    /* supporting methods */

    private CustomField mockCustomField(String customFieldId) {
        CustomField customField = mock(CustomField.class);
        when(customField.getId()).thenReturn(customFieldId);
        return customField;
    }

}
