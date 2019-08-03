package de.codescape.jira.plugins.scrumpoker.model;

import org.junit.Test;

import static de.codescape.jira.plugins.scrumpoker.model.GlobalSettings.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class GlobalSettingsTest {

    @Test
    public void newGlobalSettingsInstanceHasCorrectDefaultValues() {
        GlobalSettings globalSettings = new GlobalSettings();
        assertThat(globalSettings.getSessionTimeout(), is(equalTo(SESSION_TIMEOUT_DEFAULT)));
        assertThat(globalSettings.getAllowRevealDeck(), is(equalTo(ALLOW_REVEAL_DECK_DEFAULT)));
        assertThat(globalSettings.isDefaultProjectActivation(), is(equalTo(DEFAULT_PROJECT_ACTIVATION_DEFAULT)));
        assertThat(globalSettings.isDisplayDropdownOnBoards(), is(equalTo(DISPLAY_DROPDOWN_ON_BOARDS_DEFAULT)));
        assertThat(globalSettings.isCheckPermissionToSaveEstimate(), is(equalTo(CHECK_PERMISSION_TO_SAVE_ESTIMATE_DEFAULT)));
    }

}
