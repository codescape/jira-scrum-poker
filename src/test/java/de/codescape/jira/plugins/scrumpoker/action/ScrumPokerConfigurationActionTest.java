package de.codescape.jira.plugins.scrumpoker.action;

import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.junit.rules.AvailableInContainer;
import com.atlassian.jira.junit.rules.MockitoContainer;
import com.atlassian.jira.junit.rules.MockitoMocksInContainer;
import com.atlassian.jira.web.HttpServletVariables;
import de.codescape.jira.plugins.scrumpoker.model.GlobalSettings;
import de.codescape.jira.plugins.scrumpoker.service.EstimateFieldService;
import de.codescape.jira.plugins.scrumpoker.service.GlobalSettingsService;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import javax.servlet.http.HttpServletRequest;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ScrumPokerConfigurationActionTest {

    @Rule
    public MockitoContainer mockitoContainer = MockitoMocksInContainer.rule(this);

    @Mock
    @AvailableInContainer
    private HttpServletVariables httpServletVariables;

    @Mock
    private EstimateFieldService estimateFieldService;

    @Mock
    private GlobalSettingsService scrumPokerSettingsService;

    @InjectMocks
    private ScrumPokerConfigurationAction scrumPokerConfigurationAction;

    @Mock
    private HttpServletRequest httpServletRequest;

    /* tests for doExecute() */

    @Test
    public void doExecuteWithoutAnyActionReturnsSuccess() {
        when(httpServletVariables.getHttpRequest()).thenReturn(httpServletRequest);
        when(httpServletRequest.getParameterValues("action")).thenReturn(null);

        assertThat(scrumPokerConfigurationAction.doExecute(), is(equalTo("success")));
    }

    /* tests for getEstimateFields() */

    @Test
    public void returnListOfSupportedFieldsProvidedByEstimateFieldService() {
        CustomField customField1 = mock(CustomField.class);
        CustomField customField2 = mock(CustomField.class);
        when(estimateFieldService.supportedCustomFields()).thenReturn(asList(customField1, customField2));

        assertThat(scrumPokerConfigurationAction.getEstimateFields(), hasItems(customField1, customField2));
    }

    /* tests for getGlobalSettings() */

    @Test
    public void getGlobalSettingsShouldExposeTheGlobalSettings() {
        GlobalSettings globalSettings = mock(GlobalSettings.class);
        when(scrumPokerSettingsService.load()).thenReturn(globalSettings);

        assertThat(scrumPokerConfigurationAction.getGlobalSettings(), is(equalTo(globalSettings)));
    }

}
