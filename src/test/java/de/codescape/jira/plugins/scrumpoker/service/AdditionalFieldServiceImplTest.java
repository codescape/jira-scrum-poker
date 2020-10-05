package de.codescape.jira.plugins.scrumpoker.service;

import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.fields.layout.field.FieldLayout;
import com.atlassian.jira.issue.fields.layout.field.FieldLayoutItem;
import com.atlassian.jira.issue.fields.layout.field.FieldLayoutManager;
import de.codescape.jira.plugins.scrumpoker.model.GlobalSettings;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import webwork.action.Action;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;

public class AdditionalFieldServiceImplTest {

    private static final String MISSING_FIELD = "MISSING_FIELD";
    private static final String EXISTING_FIELD = "EXISTING_FIELD";
    private static final String NEW_FIELD = "NEW_FIELD";

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

    // tests for supportedCustomFields()

    @Test
    public void supportedCustomFieldsDelegatesToCustomFieldManager() {
        List<CustomField> expectedFields = new ArrayList<>();
        expectedFields.add(customField);
        when(customFieldManager.getCustomFieldObjects()).thenReturn(expectedFields);

        List<CustomField> fields = service.supportedCustomFields();

        assertThat(fields.size(), is(1));
        assertThat(fields, contains(customField));
    }

    // tests for configuredCustomFields()

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

    // tests for addConfiguredField())

    @Test
    public void addConfiguredFieldIgnoresEmptyString() {
        service.addConfiguredField("");
        verify(globalSettingsService, never()).persist(any());
    }

    @Test
    public void addConfiguredFieldIgnoresNullValue() {
        service.addConfiguredField(null);
        verify(globalSettingsService, never()).persist(any());
    }

    @Test
    public void addConfiguredFieldSupportsInitialEmptyList() {
        when(globalSettingsService.load()).thenReturn(globalSettings);
        when(globalSettings.getAdditionalFields()).thenReturn(null);

        service.addConfiguredField(NEW_FIELD);

        verify(globalSettings).setAdditionalFields(NEW_FIELD);
        verify(globalSettingsService).persist(globalSettings);
    }

    @Test
    public void addConfiguredFieldIgnoresRedundantValues() {
        when(globalSettingsService.load()).thenReturn(globalSettings);
        when(globalSettings.getAdditionalFields()).thenReturn(EXISTING_FIELD);

        service.addConfiguredField(EXISTING_FIELD);

        verify(globalSettings).setAdditionalFields(EXISTING_FIELD);
        verify(globalSettingsService).persist(globalSettings);
    }

    @Test
    public void addConfiguredFieldAppendsNewValues() {
        when(globalSettingsService.load()).thenReturn(globalSettings);
        when(globalSettings.getAdditionalFields()).thenReturn(EXISTING_FIELD);
        when(customFieldManager.getCustomFieldObject(EXISTING_FIELD)).thenReturn(customField);

        service.addConfiguredField(NEW_FIELD);

        verify(globalSettings).setAdditionalFields(EXISTING_FIELD + "," + NEW_FIELD);
        verify(globalSettingsService).persist(globalSettings);
    }

    // tests for removeConfiguredField()

    @Test
    public void removeConfiguredFieldRemovesExistingValue() {
        when(globalSettingsService.load()).thenReturn(globalSettings);
        when(globalSettings.getAdditionalFields()).thenReturn(EXISTING_FIELD);
        when(customFieldManager.getCustomFieldObject(EXISTING_FIELD)).thenReturn(customField);

        service.removeConfiguredField(EXISTING_FIELD);

        verify(globalSettings).setAdditionalFields("");
        verify(globalSettingsService).persist(globalSettings);
    }

    @Test
    public void removeConfiguredFieldAlsoRemovesInvalidValue() {
        when(globalSettingsService.load()).thenReturn(globalSettings);
        when(globalSettings.getAdditionalFields()).thenReturn(EXISTING_FIELD + "," + MISSING_FIELD);
        when(customFieldManager.getCustomFieldObject(EXISTING_FIELD)).thenReturn(customField);

        service.removeConfiguredField(EXISTING_FIELD);

        verify(globalSettings).setAdditionalFields("");
        verify(globalSettingsService).persist(globalSettings);
    }

    // tests for renderFieldValue()

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

}
