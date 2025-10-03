package de.codescape.jira.plugins.scrumpoker.service;

import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.fields.layout.field.FieldLayout;
import com.atlassian.jira.issue.fields.layout.field.FieldLayoutItem;
import com.atlassian.jira.issue.fields.layout.field.FieldLayoutManager;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import de.codescape.jira.plugins.scrumpoker.model.AdditionalField;
import jakarta.inject.Inject;
import org.springframework.stereotype.Component;
import webwork.action.Action;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of {@link AdditionalFieldService}.
 */
@Component
public class AdditionalFieldServiceImpl implements AdditionalFieldService {

    private final FieldLayoutManager fieldLayoutManager;
    private final CustomFieldManager customFieldManager;
    private final GlobalSettingsService globalSettingsService;

    @Inject
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
    public List<AdditionalField> supportedCustomFields() {
        List<String> configuredFields = additionalFieldsAsList(globalSettingsService.load().getAdditionalFields());
        return customFieldManager.getCustomFieldObjects().stream()
            // add boolean flag whether this field is configured or not
            .map(customField -> new AdditionalField(customField, configuredFields.contains(customField.getId())))
            // sorting must preserve sorting of configured fields and thus they are moved in front of all other fields
            .sorted((Comparator.comparingInt(additionalField ->
                configuredFields.contains(additionalField.getCustomField().getId()) ?
                    configuredFields.indexOf(additionalField.getCustomField().getId()) : Integer.MAX_VALUE)))
            .collect(Collectors.toList());
    }

    @Override
    public List<CustomField> configuredCustomFields() {
        List<String> additionalFields = additionalFieldsAsList(globalSettingsService.load().getAdditionalFields());
        return additionalFields.stream()
            .map(customFieldManager::getCustomFieldObject)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    // split the comma separated string into a list of strings
    private List<String> additionalFieldsAsList(String additionalFields) {
        if (additionalFields != null) {
            return new ArrayList<>(Arrays.asList(additionalFields.split(",")));
        } else {
            return new ArrayList<>();
        }
    }

}
