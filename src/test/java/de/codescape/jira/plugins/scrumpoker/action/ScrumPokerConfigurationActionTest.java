package de.codescape.jira.plugins.scrumpoker.action;

import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.junit.rules.AvailableInContainer;
import com.atlassian.jira.junit.rules.MockitoContainer;
import com.atlassian.jira.junit.rules.MockitoMocksInContainer;
import com.atlassian.jira.web.HttpServletVariables;
import de.codescape.jira.plugins.scrumpoker.ScrumPokerConstants;
import de.codescape.jira.plugins.scrumpoker.model.AdditionalField;
import de.codescape.jira.plugins.scrumpoker.model.AllowRevealDeck;
import de.codescape.jira.plugins.scrumpoker.model.DisplayCommentsForIssue;
import de.codescape.jira.plugins.scrumpoker.model.GlobalSettings;
import de.codescape.jira.plugins.scrumpoker.service.AdditionalFieldService;
import de.codescape.jira.plugins.scrumpoker.service.EstimateFieldService;
import de.codescape.jira.plugins.scrumpoker.service.GlobalSettingsService;
import de.codescape.jira.plugins.scrumpoker.service.ScrumPokerLicenseService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;

import static de.codescape.jira.plugins.scrumpoker.action.ScrumPokerConfigurationAction.Actions.DEFAULTS;
import static de.codescape.jira.plugins.scrumpoker.action.ScrumPokerConfigurationAction.Actions.SAVE;
import static de.codescape.jira.plugins.scrumpoker.action.ScrumPokerConfigurationAction.Parameters.*;
import static de.codescape.jira.plugins.scrumpoker.action.ScrumPokerProjectConfigurationAction.Parameters.ACTION;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static webwork.action.Action.SUCCESS;

public class ScrumPokerConfigurationActionTest {

    private static final String LICENSE_ERROR = "license.error";

    @Rule
    public MockitoContainer mockitoContainer = MockitoMocksInContainer.rule(this);

    @Mock
    @AvailableInContainer
    private HttpServletVariables httpServletVariables;

    @Mock
    private EstimateFieldService estimateFieldService;

    @Mock
    private GlobalSettingsService globalSettingsService;

    @Mock
    private AdditionalFieldService additionalFieldService;

    @Mock
    private ScrumPokerLicenseService scrumPokerLicenseService;

    @InjectMocks
    private ScrumPokerConfigurationAction action;

    @Mock
    private HttpServletRequest httpServletRequest;

    /* tests for doDefault() */

    @Test
    public void doDefaultAlwaysReturnsSuccess() {
        assertThat(action.doDefault(), is(equalTo(SUCCESS)));
    }

    /* tests for doExecute() */

    @Test
    public void doExecuteWithoutAnyActionReturnsSuccess() {
        when(httpServletVariables.getHttpRequest()).thenReturn(httpServletRequest);
        when(httpServletRequest.getParameterValues(ACTION)).thenReturn(null);

        assertThat(action.doExecute(), is(equalTo(SUCCESS)));
    }

    @Test
    public void doExecuteWithActionDefaultsResetsScrumPokerConfiguration() {
        when(httpServletVariables.getHttpRequest()).thenReturn(httpServletRequest);
        when(httpServletRequest.getParameterValues(ACTION)).thenReturn(new String[]{DEFAULTS});

        assertThat(action.doExecute(), is(equalTo(SUCCESS)));

        ArgumentCaptor<GlobalSettings> globalSettings = ArgumentCaptor.forClass(GlobalSettings.class);
        verify(globalSettingsService).persist(globalSettings.capture());
        assertThat(globalSettings.getValue().getSessionTimeout(), is(equalTo(GlobalSettings.SESSION_TIMEOUT_DEFAULT)));
    }

    @Test
    public void doExecuteWithActionSavePersistsAllParameters() {
        // when the save method is called
        when(httpServletVariables.getHttpRequest()).thenReturn(httpServletRequest);
        when(httpServletRequest.getParameterValues(ACTION)).thenReturn(new String[]{SAVE});

        // with the following parameters
        when(httpServletRequest.getParameterValues(ESTIMATE_FIELD))
            .thenReturn(new String[]{"estimateField"});
        when(httpServletRequest.getParameterValues(SESSION_TIMEOUT))
            .thenReturn(new String[]{"18"});
        when(httpServletRequest.getParameterValues(ACTIVATE_SCRUM_POKER))
            .thenReturn(new String[]{"true"});
        when(httpServletRequest.getParameterValues(ALLOW_REVEAL_DECK))
            .thenReturn(new String[]{"PARTICIPANTS"});
        when(httpServletRequest.getParameterValues(DISPLAY_DROPDOWN_ON_BOARDS))
            .thenReturn(new String[]{"true"});
        when(httpServletRequest.getParameterValues(CHECK_PERMISSION_TO_SAVE_ESTIMATE))
            .thenReturn(new String[]{"true"});
        when(httpServletRequest.getParameterValues(DISPLAY_COMMENTS_FOR_ISSUE))
            .thenReturn(new String[]{"NONE"});
        when(httpServletRequest.getParameterValues(CARD_SET))
            .thenReturn(new String[]{"1,2,3,4,5"});
        when(httpServletRequest.getParameterValues(DISPLAY_ADDITIONAL_FIELDS))
            .thenReturn(new String[]{"field1,field2,field3"});

        assertThat(action.doExecute(), is(equalTo(SUCCESS)));

        GlobalSettings globalSettings = getPersistedGlobalSettings();
        assertThat(globalSettings.getEstimateField(), is(equalTo("estimateField")));
        assertThat(globalSettings.getSessionTimeout(), is(equalTo(18)));
        assertThat(globalSettings.isActivateScrumPoker(), is(equalTo(true)));
        assertThat(globalSettings.getAllowRevealDeck(), is(equalTo(AllowRevealDeck.PARTICIPANTS)));
        assertThat(globalSettings.isDisplayDropdownOnBoards(), is(equalTo(true)));
        assertThat(globalSettings.isCheckPermissionToSaveEstimate(), is(equalTo(true)));
        assertThat(globalSettings.getDisplayCommentsForIssue(), is(equalTo(DisplayCommentsForIssue.NONE)));
        assertThat(globalSettings.getCardSet(), is(equalTo("1,2,3,4,5")));
        assertThat(globalSettings.getAdditionalFields(), is(equalTo("field1,field2,field3")));
    }

    @Test
    public void doExecuteWithActionSavePersistsFalseForActivateScrumPokerIfUnchecked() {
        // when the save method is called
        when(httpServletVariables.getHttpRequest()).thenReturn(httpServletRequest);
        when(httpServletRequest.getParameterValues(ACTION)).thenReturn(new String[]{SAVE});

        // with the following parameters
        when(httpServletRequest.getParameterValues(ACTIVATE_SCRUM_POKER))
            .thenReturn(null);

        assertThat(action.doExecute(), is(equalTo(SUCCESS)));

        GlobalSettings globalSettings = getPersistedGlobalSettings();
        assertThat(globalSettings.isActivateScrumPoker(), is(equalTo(false)));
    }

    /* tests for getDocumentationUrl() */

    @Test
    public void getDocumentationUrlShouldExposeLink() {
        assertThat(action.getDocumentationUrl(), is(equalTo(ScrumPokerConstants.CONFIGURATION_DOCUMENTATION)));
    }

    /* tests for getEstimateFields() */

    @Test
    public void returnListOfSupportedFieldsProvidedByEstimateFieldService() {
        CustomField customField1 = mock(CustomField.class);
        CustomField customField2 = mock(CustomField.class);
        when(estimateFieldService.supportedCustomFields()).thenReturn(asList(customField1, customField2));

        assertThat(action.getEstimateFields(), hasItems(customField1, customField2));
    }

    /* tests for getLicenseError() */

    @Test
    public void getLicenseErrorExposesLicenseErrorIfExists() {
        when(scrumPokerLicenseService.getLicenseError()).thenReturn(LICENSE_ERROR);
        assertThat(action.getLicenseError(), is(equalTo(LICENSE_ERROR)));
        verify(scrumPokerLicenseService, times(1)).getLicenseError();
        verifyNoMoreInteractions(scrumPokerLicenseService);
    }

    @Test
    public void getLicenseErrorReturnsNullIfNoLicenseErrorExists() {
        when(scrumPokerLicenseService.getLicenseError()).thenReturn(null);
        assertThat(action.getLicenseError(), is(nullValue()));
        verify(scrumPokerLicenseService, times(1)).getLicenseError();
        verifyNoMoreInteractions(scrumPokerLicenseService);
    }

    /* tests for getAdditionalFields() */

    @Test
    public void getAdditionalFieldsDelegatesToAdditionalFieldsService() {
        ArrayList<AdditionalField> additionalFields = new ArrayList<>();
        additionalFields.add(new AdditionalField(null, true));
        when(additionalFieldService.supportedCustomFields()).thenReturn(additionalFields);

        assertThat(action.getAdditionalFields(), is(equalTo(additionalFields)));
        verify(additionalFieldService).supportedCustomFields();
        verifyNoMoreInteractions(additionalFieldService);
    }

    /* tests for getGlobalSettings() */

    @Test
    public void getGlobalSettingsShouldExposeTheGlobalSettings() {
        GlobalSettings globalSettings = mock(GlobalSettings.class);
        when(globalSettingsService.load()).thenReturn(globalSettings);

        assertThat(action.getGlobalSettings(), is(equalTo(globalSettings)));
    }

    /* supporting methods */

    private GlobalSettings getPersistedGlobalSettings() {
        ArgumentCaptor<GlobalSettings> globalSettingsCaptor = ArgumentCaptor.forClass(GlobalSettings.class);
        verify(globalSettingsService).persist(globalSettingsCaptor.capture());
        return globalSettingsCaptor.getValue();
    }

}
