package de.codescape.jira.plugins.scrumpoker.action;

import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.junit.rules.AvailableInContainer;
import com.atlassian.jira.junit.rules.MockitoContainer;
import com.atlassian.jira.junit.rules.MockitoMocksInContainer;
import com.atlassian.jira.web.HttpServletVariables;
import com.atlassian.upm.api.license.PluginLicenseManager;
import com.atlassian.upm.api.license.entity.PluginLicense;
import com.atlassian.upm.api.util.Option;
import de.codescape.jira.plugins.scrumpoker.model.GlobalSettings;
import de.codescape.jira.plugins.scrumpoker.service.EstimationFieldService;
import de.codescape.jira.plugins.scrumpoker.service.ScrumPokerSettingService;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import javax.servlet.http.HttpServletRequest;

import static de.codescape.jira.plugins.scrumpoker.action.HealthCheckAction.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;

public class HealthCheckActionTest {

    @Rule
    public MockitoContainer mockitoContainer = MockitoMocksInContainer.rule(this);

    @Mock
    @AvailableInContainer
    private HttpServletVariables httpServletVariables;

    @Mock
    private PluginLicenseManager pluginLicenseManager;

    @Mock
    private ScrumPokerSettingService scrumPokerSettingService;

    @Mock
    private EstimationFieldService estimationFieldService;

    @InjectMocks
    private HealthCheckAction healthCheckAction;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private PluginLicense pluginLicense;

    @Mock
    private GlobalSettings globalSettings;

    @Mock
    private CustomField estimationField;

    @Test
    public void shouldAlwaysDisplayThePage() {
        assertThat(healthCheckAction.doExecute(), is(equalTo("success")));
    }

    @Test
    public void shouldShowResultsIfButtonForResultsIsClicked() {
        expectParameterToReturnValue(Parameters.ACTION, "start");
        assertThat(healthCheckAction.showResults(), is(equalTo(true)));
    }

    @Test
    public void shouldNotShowResultsIfButtonForResultsIsNotClicked() {
        expectParameterToReturnValue(Parameters.ACTION, null);
        assertThat(healthCheckAction.showResults(), is(equalTo(false)));
    }

    @Test
    public void shouldShowLicenseChecksWhenSelected() {
        expectParameterToReturnValue(Parameters.SCAN_LICENSE, "true");
        assertThat(healthCheckAction.showLicense(), is(equalTo(true)));
    }

    @Test
    public void shouldShowNoLicenseChecksWhenNotSelected() {
        expectParameterToReturnValue(Parameters.SCAN_LICENSE, null);
        assertThat(healthCheckAction.showLicense(), is(equalTo(false)));
    }

    @Test
    public void shouldShowConfigurationChecksWhenSelected() {
        expectParameterToReturnValue(Parameters.SCAN_CONFIGURATION, "true");
        assertThat(healthCheckAction.showConfiguration(), is(equalTo(true)));
    }

    @Test
    public void shouldShowNoConfigurationChecksWhenNotSelected() {
        expectParameterToReturnValue(Parameters.SCAN_CONFIGURATION, null);
        assertThat(healthCheckAction.showConfiguration(), is(equalTo(false)));
    }

    @Test
    public void shouldShowErrorLogChecksWhenSelected() {
        expectParameterToReturnValue(Parameters.SCAN_ERRORS, "true");
        assertThat(healthCheckAction.showErrors(), is(equalTo(true)));
    }

    @Test
    public void shouldShowNoErrorLogChecksWhenNotSelected() {
        expectParameterToReturnValue(Parameters.SCAN_ERRORS, null);
        assertThat(healthCheckAction.showErrors(), is(equalTo(false)));
    }

    private void expectParameterToReturnValue(String parameterName, String parameterValue) {
        when(httpServletVariables.getHttpRequest()).thenReturn(httpServletRequest);
        when(httpServletRequest.getParameter(parameterName)).thenReturn(parameterValue);
    }

    // license

    @Test
    public void shouldSignalMissingLicenseIfNoLicenseIsFound() {
        when(pluginLicenseManager.getLicense()).thenReturn(Option.none());
        assertThat(healthCheckAction.getLicenseResults(), hasItem(License.NO_LICENSE_FOUND));
    }

    @Test
    public void shouldSignalInvalidLicenseIfLicenseIsFoundButHasErrors() {
        when(pluginLicenseManager.getLicense()).thenReturn(Option.option(pluginLicense));
        when(pluginLicense.isValid()).thenReturn(false);
        assertThat(healthCheckAction.getLicenseResults(), hasItem(License.LICENSE_INVALID));
    }

    @Test
    public void shouldSignalNoLicenseErrorsForExistingAndValidLicense() {
        when(pluginLicenseManager.getLicense()).thenReturn(Option.option(pluginLicense));
        when(pluginLicense.isValid()).thenReturn(true);
        assertThat(healthCheckAction.getLicenseResults(), is(empty()));
    }

    // configuration

    @Test
    public void shouldSignalUnsetEstimationFieldIfEstimationFieldIsNotSet() {
        when(scrumPokerSettingService.load()).thenReturn(globalSettings);
        when(globalSettings.getStoryPointField()).thenReturn(null);
        assertThat(healthCheckAction.getConfigurationResults(), hasItem(Configuration.ESTIMATION_FIELD_NOT_SET));
    }

    @Test
    public void shouldSignalMissingEstimationFieldIfEstimationFieldIsSetButDoesNotExist() {
        when(scrumPokerSettingService.load()).thenReturn(globalSettings);
        when(globalSettings.getStoryPointField()).thenReturn("something");
        when(estimationFieldService.findStoryPointField()).thenReturn(null);
        assertThat(healthCheckAction.getConfigurationResults(), hasItem(Configuration.ESTIMATION_FIELD_NOT_FOUND));
    }

    @Test
    public void shouldSignalNoConfigurationErrorsIfNoneAreFound() {
        when(scrumPokerSettingService.load()).thenReturn(globalSettings);
        when(globalSettings.getStoryPointField()).thenReturn("something");
        when(estimationFieldService.findStoryPointField()).thenReturn(estimationField);
        assertThat(healthCheckAction.getConfigurationResults(), is(empty()));
    }

    // error log

    @Test
    public void shouldSignalNoErrorsIfErrorLogIsEmpty() {
        assertThat(healthCheckAction.getErrorsResults(), is(empty()));
    }

}
