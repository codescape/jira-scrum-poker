package de.codescape.jira.plugins.scrumpoker.service;

import com.atlassian.upm.api.license.PluginLicenseManager;
import com.atlassian.upm.api.license.entity.LicenseError;
import com.atlassian.upm.api.license.entity.PluginLicense;
import com.atlassian.upm.api.util.Option;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static de.codescape.jira.plugins.scrumpoker.service.ScrumPokerLicenseServiceImpl.ERROR_MESSAGE_PREFIX;
import static de.codescape.jira.plugins.scrumpoker.service.ScrumPokerLicenseServiceImpl.MISSING_LICENSE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ScrumPokerLicenseServiceImplTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private PluginLicenseManager pluginLicenseManager;

    @InjectMocks
    private ScrumPokerLicenseServiceImpl scrumPokerLicenseService;

    /* tests for hasValidLicense() */

    @Test
    public void hasValidLicenseReturnsFalseForMissingLicense() {
        when(pluginLicenseManager.getLicense()).thenReturn(Option.none());
        assertThat(scrumPokerLicenseService.hasValidLicense(), is(false));
    }

    @Test
    public void hasValidLicenseReturnsFalseForLicenseWithError() {
        PluginLicense pluginLicense = mock(PluginLicense.class);
        when(pluginLicense.getError()).thenReturn(Option.option(LicenseError.EXPIRED));
        when(pluginLicenseManager.getLicense()).thenReturn(Option.option(pluginLicense));
        assertThat(scrumPokerLicenseService.hasValidLicense(), is(false));
    }

    @Test
    public void hasValidLicenseReturnsTrueForLicenseWithoutErrors() {
        PluginLicense pluginLicense = mock(PluginLicense.class);
        when(pluginLicense.getError()).thenReturn(Option.none());
        when(pluginLicenseManager.getLicense()).thenReturn(Option.option(pluginLicense));
        assertThat(scrumPokerLicenseService.hasValidLicense(), is(true));
    }

    /* tests for getLicenseError() */

    @Test
    public void getLicenseErrorReturnsMissingForMissingLicense() {
        when(pluginLicenseManager.getLicense()).thenReturn(Option.none());
        assertThat(scrumPokerLicenseService.getLicenseError(), is(equalTo(MISSING_LICENSE)));
    }

    @Test
    public void getLicenseErrorReturnsErrorForLicenseWithError() {
        LicenseError expectedLicenseError = LicenseError.EXPIRED;
        PluginLicense pluginLicense = mock(PluginLicense.class);
        when(pluginLicense.getError()).thenReturn(Option.option(expectedLicenseError));
        when(pluginLicenseManager.getLicense()).thenReturn(Option.option(pluginLicense));
        assertThat(scrumPokerLicenseService.getLicenseError(),
            is(equalTo(ERROR_MESSAGE_PREFIX + expectedLicenseError.name().toLowerCase())));
    }

    @Test
    public void getLicenseErrorReturnsNullForLicenseWithoutErrors() {
        PluginLicense pluginLicense = mock(PluginLicense.class);
        when(pluginLicense.getError()).thenReturn(Option.none());
        when(pluginLicenseManager.getLicense()).thenReturn(Option.option(pluginLicense));
        assertThat(scrumPokerLicenseService.getLicenseError(), is(nullValue()));
    }

}
