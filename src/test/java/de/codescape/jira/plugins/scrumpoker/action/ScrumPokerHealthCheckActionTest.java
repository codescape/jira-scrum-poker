package de.codescape.jira.plugins.scrumpoker.action;

import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.junit.rules.AvailableInContainer;
import com.atlassian.jira.junit.rules.MockitoContainer;
import com.atlassian.jira.junit.rules.MockitoMocksInContainer;
import com.atlassian.jira.web.HttpServletVariables;
import de.codescape.jira.plugins.scrumpoker.ScrumPokerConstants;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerError;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerProject;
import de.codescape.jira.plugins.scrumpoker.model.Card;
import de.codescape.jira.plugins.scrumpoker.model.GlobalSettings;
import de.codescape.jira.plugins.scrumpoker.model.ProjectActivation;
import de.codescape.jira.plugins.scrumpoker.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static de.codescape.jira.plugins.scrumpoker.action.ScrumPokerHealthCheckAction.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ScrumPokerHealthCheckActionTest {

    @Rule
    public MockitoContainer mockitoContainer = MockitoMocksInContainer.rule(this);

    @Mock
    @AvailableInContainer
    private HttpServletVariables httpServletVariables;

    @Mock
    private GlobalSettingsService globalSettingsService;

    @Mock
    private CustomFieldManager customFieldManager;

    @Mock
    private ProjectSettingsService projectSettingsService;

    @Mock
    private ErrorLogService errorLogService;

    @Mock
    private CardSetService cardSetService;

    @Mock
    EstimateFieldService estimateFieldService;

    @Mock
    private ScrumPokerLicenseService scrumPokerLicenseService;

    @InjectMocks
    private ScrumPokerHealthCheckAction action;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private GlobalSettings globalSettings;

    @Mock
    private CustomField estimateField;

    /**/

    @Test
    public void shouldAlwaysDisplayThePageOnDoDefault() {
        assertThat(action.doDefault(), is(equalTo(ScrumPokerHealthCheckAction.SUCCESS)));
    }

    /* tests for doExecute() */

    @Test
    public void shouldAlwaysDisplayThePageOnDoExecute() {
        assertThat(action.doExecute(), is(equalTo(ScrumPokerHealthCheckAction.SUCCESS)));
    }

    /* tests for showResults() */

    @Test
    public void shouldShowResultsIfButtonForResultsIsClicked() {
        expectParameterToReturnValue(Parameters.ACTION, "start");
        assertThat(action.showResults(), is(equalTo(true)));
    }

    @Test
    public void shouldNotShowResultsIfButtonForResultsIsNotClicked() {
        expectParameterToReturnValue(Parameters.ACTION, null);
        assertThat(action.showResults(), is(equalTo(false)));
    }

    /* tests for showLicense() */

    @Test
    public void shouldShowLicenseChecksWhenSelected() {
        expectParameterToReturnValue(Parameters.SCAN_LICENSE, "true");
        assertThat(action.showLicense(), is(equalTo(true)));
    }

    @Test
    public void shouldShowNoLicenseChecksWhenNotSelected() {
        expectParameterToReturnValue(Parameters.SCAN_LICENSE, null);
        assertThat(action.showLicense(), is(equalTo(false)));
    }

    /* tests for showConfiguration() */

    @Test
    public void shouldShowConfigurationChecksWhenSelected() {
        expectParameterToReturnValue(Parameters.SCAN_CONFIGURATION, "true");
        assertThat(action.showConfiguration(), is(equalTo(true)));
    }

    @Test
    public void shouldShowNoConfigurationChecksWhenNotSelected() {
        expectParameterToReturnValue(Parameters.SCAN_CONFIGURATION, null);
        assertThat(action.showConfiguration(), is(equalTo(false)));
    }

    /* tests for showErrors() */

    @Test
    public void shouldShowErrorLogChecksWhenSelected() {
        expectParameterToReturnValue(Parameters.SCAN_ERRORS, "true");
        assertThat(action.showErrors(), is(equalTo(true)));
    }

    @Test
    public void shouldShowNoErrorLogChecksWhenNotSelected() {
        expectParameterToReturnValue(Parameters.SCAN_ERRORS, null);
        assertThat(action.showErrors(), is(equalTo(false)));
    }

    /* tests for getLicenseResults() */

    @Test
    public void shouldSignalLicenseErrorsIfLicenseIsNotValid() {
        when(scrumPokerLicenseService.hasValidLicense()).thenReturn(false);
        when(scrumPokerLicenseService.getLicenseError()).thenReturn("some.license.error");
        assertThat(action.getLicenseResults(), hasItem("some.license.error"));
    }

    @Test
    public void shouldSignalNoLicenseErrorsForExistingAndValidLicense() {
        when(scrumPokerLicenseService.hasValidLicense()).thenReturn(true);
        assertThat(action.getLicenseResults(), is(empty()));
    }

    /* tests for getConfigurationResults() */

    @Test
    public void shouldSignalUnsetEstimateFieldIfEstimateFieldIsNotSet() {
        when(globalSettingsService.load()).thenReturn(globalSettings);
        when(globalSettings.getEstimateField()).thenReturn(null);
        when(globalSettings.isActivateScrumPoker()).thenReturn(true);
        assertThat(action.getConfigurationResults(), hasItem(Configuration.ESTIMATE_FIELD_NOT_SET));
    }

    @Test
    public void shouldSignalMissingEstimateFieldIfEstimateFieldIsSetButDoesNotExist() {
        when(globalSettingsService.load()).thenReturn(globalSettings);
        when(globalSettings.getEstimateField()).thenReturn("something");
        when(customFieldManager.getCustomFieldObject("something")).thenReturn(null);
        when(globalSettings.isActivateScrumPoker()).thenReturn(true);
        assertThat(action.getConfigurationResults(), hasItem(Configuration.ESTIMATE_FIELD_NOT_FOUND));
    }

    @Test
    public void shouldSignalEstimateFieldNotSupportedIfEstimateFieldIsSetToNotSupportedField() {
        when(globalSettingsService.load()).thenReturn(globalSettings);
        when(globalSettings.getEstimateField()).thenReturn("unsupported");
        when(customFieldManager.getCustomFieldObject("unsupported")).thenReturn(estimateField);
        CustomField supportedField = mock(CustomField.class);
        when(supportedField.getId()).thenReturn("supported");
        when(estimateFieldService.supportedCustomFields()).thenReturn(Collections.singletonList(supportedField));
        assertThat(action.getConfigurationResults(), hasItem(Configuration.ESTIMATE_FIELD_NOT_SUPPORTED));
    }

    @Test
    public void shouldSignalNoProjectEnabledWhenNeitherGloballyEnabledNorHavingAProjectExplicitlyEnabled() {
        when(globalSettingsService.load()).thenReturn(globalSettings);
        when(globalSettings.getEstimateField()).thenReturn("something");
        when(customFieldManager.getCustomFieldObject("something")).thenReturn(null);
        when(globalSettings.isActivateScrumPoker()).thenReturn(false);
        expectNoProjectExplicitlyEnabled();
        assertThat(action.getConfigurationResults(), hasItem(Configuration.ESTIMATE_FIELD_NOT_FOUND));
    }

    @Test
    public void shouldSignalCardSetWithoutOptionsIfCardSetHasOnlyOneOption() {
        when(globalSettingsService.load()).thenReturn(globalSettings);
        when(globalSettings.getEstimateField()).thenReturn("something");
        when(customFieldManager.getCustomFieldObject("something")).thenReturn(estimateField);
        when(globalSettings.isActivateScrumPoker()).thenReturn(true);
        when(cardSetService.getCardSet()).thenReturn(Collections.singletonList(new Card("1", true)));
        assertThat(action.getConfigurationResults(), hasItem(Configuration.CARD_SET_WITHOUT_OPTIONS));
    }

    @Test
    public void shouldSignalCardSetWithoutOptionsIfCardSetHasNoOptionAtAll() {
        when(globalSettingsService.load()).thenReturn(globalSettings);
        when(globalSettings.getEstimateField()).thenReturn("something");
        when(customFieldManager.getCustomFieldObject("something")).thenReturn(estimateField);
        when(globalSettings.isActivateScrumPoker()).thenReturn(true);
        when(cardSetService.getCardSet()).thenReturn(Collections.emptyList());
        assertThat(action.getConfigurationResults(), hasItem(Configuration.CARD_SET_WITHOUT_OPTIONS));
    }

    @Test
    public void shouldSignalCardSetWithoutOptionsIfCardSetHasOnlyOneOptionApartFromThoseSpecialCards() {
        when(globalSettingsService.load()).thenReturn(globalSettings);
        when(globalSettings.getEstimateField()).thenReturn("something");
        when(customFieldManager.getCustomFieldObject("something")).thenReturn(estimateField);
        when(globalSettings.isActivateScrumPoker()).thenReturn(true);
        when(cardSetService.getCardSet()).thenReturn(Arrays.asList(
            Card.COFFEE_BREAK,
            Card.QUESTION_MARK,
            new Card("1", true)));
        assertThat(action.getConfigurationResults(), hasItem(Configuration.CARD_SET_WITHOUT_OPTIONS));
    }

    @Test
    public void shouldSignalNoConfigurationErrorsIfNoneAreFound() {
        when(globalSettingsService.load()).thenReturn(globalSettings);
        when(globalSettings.getEstimateField()).thenReturn("something");
        when(customFieldManager.getCustomFieldObject("something")).thenReturn(estimateField);
        when(globalSettings.isActivateScrumPoker()).thenReturn(true);
        when(cardSetService.getCardSet()).thenReturn(Arrays.asList(
            new Card("1", true),
            new Card("2", true),
            new Card("3", true),
            new Card("5", true)));
        when(estimateFieldService.supportedCustomFields()).thenReturn(Collections.singletonList(estimateField));
        when(estimateField.getId()).thenReturn("something");
        assertThat(action.getConfigurationResults(), is(empty()));
    }

    /* tests for getErrorsResults() */

    @Test
    public void shouldSignalNoErrorsIfErrorLogIsEmpty() {
        assertThat(action.getErrorsResults(), is(empty()));
    }

    @Test
    public void shouldSignalErrorsIfErrorLogContainsErrors() {
        ScrumPokerError[] errors = {mock(ScrumPokerError.class)};
        when(errorLogService.listAll()).thenReturn(Arrays.asList(errors));
        assertThat(action.getErrorsResults(), hasItem(ErrorLog.ERROR_LOG_NOT_EMPTY));
    }

    /* tests for getDocumentationUrl() */

    @Test
    public void getDocumentationUrlShouldExposeLink() {
        assertThat(action.getDocumentationUrl(), is(equalTo(ScrumPokerConstants.HEALTH_CHECK_DOCUMENTATION)));
    }

    /* supporting methods */

    private void expectParameterToReturnValue(String parameterName, String parameterValue) {
        when(httpServletVariables.getHttpRequest()).thenReturn(httpServletRequest);
        when(httpServletRequest.getParameterValues(parameterName)).thenReturn(new String[]{parameterValue});
    }

    private void expectNoProjectExplicitlyEnabled() {
        List<ScrumPokerProject> scrumPokerProjects = new ArrayList<>();
        ScrumPokerProject scrumPokerProject = mock(ScrumPokerProject.class);
        when(scrumPokerProject.getActivateScrumPoker()).thenReturn(ProjectActivation.INHERIT);
        scrumPokerProjects.add(scrumPokerProject);
        when(projectSettingsService.loadAll()).thenReturn(scrumPokerProjects);
    }

}
