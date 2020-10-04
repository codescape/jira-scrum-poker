package de.codescape.jira.plugins.scrumpoker.service;

import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.fields.layout.field.FieldLayout;
import com.atlassian.jira.issue.fields.layout.field.FieldLayoutItem;
import com.atlassian.jira.issue.fields.layout.field.FieldLayoutManager;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import de.codescape.jira.plugins.scrumpoker.model.GlobalSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import webwork.action.Action;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of {@link AdditionalFieldService}.
 */
// TODO implement unit tests
@Component
public class AdditionalFieldServiceImpl implements AdditionalFieldService {

    private final FieldLayoutManager fieldLayoutManager;
    private final CustomFieldManager customFieldManager;
    private final GlobalSettingsService globalSettingsService;

    @Autowired
    public AdditionalFieldServiceImpl(@ComponentImport FieldLayoutManager fieldLayoutManager,
                                      @ComponentImport CustomFieldManager customFieldManager,
                                      GlobalSettingsService globalSettingsService) {
        this.fieldLayoutManager = fieldLayoutManager;
        this.customFieldManager = customFieldManager;
        this.globalSettingsService = globalSettingsService;
    }

    @Override
    public String renderFieldValue(CustomField customField, Action action, MutableIssue issue) {
        FieldLayout fieldLayout = fieldLayoutManager.getFieldLayout(issue);
        FieldLayoutItem fieldLayoutItem = fieldLayout.getFieldLayoutItem(customField);
        return customField.getViewHtml(fieldLayoutItem, action, issue);
    }

    @Override
    public List<CustomField> supportedCustomFields() {
        return customFieldManager.getCustomFieldObjects();
    }

    @Override
    public List<CustomField> configuredCustomFields() {
        List<String> additionalFields = additionalFieldsAsList(globalSettingsService.load().getAdditionalFields());
        return additionalFields.stream()
            .map(customFieldManager::getCustomFieldObject)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    @Override
    public void addConfiguredField(String newAdditionalField) {
        // prevent empty values being added
        if (newAdditionalField == null || newAdditionalField.isEmpty()) {
            return;
        }

        GlobalSettings globalSettings = globalSettingsService.load();

        // prevent duplicates and only add fields that are not configured yet
        List<String> additionalFields = additionalFieldsAsList(globalSettings.getAdditionalFields());
        additionalFields.removeAll(invalidCustomFieldIds(additionalFields));
        if (!additionalFields.contains(newAdditionalField)) {
            additionalFields.add(newAdditionalField);
        }

        globalSettings.setAdditionalFields(additionalFieldsAsString(additionalFields));
        globalSettingsService.persist(globalSettings);
    }

    @Override
    public void removeConfiguredField(String customFieldId) {
        GlobalSettings globalSettings = globalSettingsService.load();

        List<String> additionalFields = additionalFieldsAsList(globalSettings.getAdditionalFields());
        additionalFields.removeAll(invalidCustomFieldIds(additionalFields));
        additionalFields.remove(customFieldId);

        globalSettings.setAdditionalFields(additionalFieldsAsString(additionalFields));
        globalSettingsService.persist(globalSettings);
    }

    // identifies all invalid custom field ids (e.g. pointing to a non existent field)
    private List<String> invalidCustomFieldIds(List<String> additionalFields) {
        return additionalFields.stream()
            .filter(customField -> customFieldManager.getCustomFieldObject(customField) == null)
            .collect(Collectors.toList());
    }

    private List<String> additionalFieldsAsList(String additionalFields) {
        if (additionalFields != null) {
            return new ArrayList<>(Arrays.asList(additionalFields.split(",")));
        } else {
            return new ArrayList<>();
        }
    }

    private String additionalFieldsAsString(List<String> additionalFields) {
        return String.join(",", additionalFields);
    }

}
