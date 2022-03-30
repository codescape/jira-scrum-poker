package de.codescape.jira.plugins.scrumpoker.service;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerSetting;
import de.codescape.jira.plugins.scrumpoker.model.AllowRevealDeck;
import de.codescape.jira.plugins.scrumpoker.model.DisplayCommentsForIssue;
import de.codescape.jira.plugins.scrumpoker.model.GlobalSettings;
import net.java.ao.DBParam;
import net.java.ao.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static de.codescape.jira.plugins.scrumpoker.model.GlobalSettings.*;

/**
 * Implementation of {@link GlobalSettingsService} using Active Objects as persistence model.
 */
@Component
public class GlobalSettingsServiceImpl implements GlobalSettingsService {

    public static final String ACTIVATE_SCRUM_POKER = "activateScrumPoker";
    private static final String ESTIMATE_FIELD = "storyPointField";
    private static final String SESSION_TIMEOUT = "sessionTimeout";
    private static final String ALLOW_REVEAL_DECK = "allowRevealDeck";
    private static final String DISPLAY_DROPDOWN_ON_BOARDS = "displayDropdownOnBoards";
    private static final String CHECK_PERMISSION_TO_SAVE_ESTIMATE = "checkPermissionToSaveEstimate";
    private static final String DISPLAY_COMMENTS_FOR_ISSUE = "displayCommentsForIssue";
    private static final String CARD_SET = "defaultCardSet";
    private static final String ADDITIONAL_FIELDS = "additionalFields";

    private final ActiveObjects activeObjects;

    @Autowired
    public GlobalSettingsServiceImpl(@ComponentImport ActiveObjects activeObjects) {
        this.activeObjects = activeObjects;
    }

    @Override
    public GlobalSettings load() {
        GlobalSettings globalSettings = new GlobalSettings();
        Arrays.stream(activeObjects.find(ScrumPokerSetting.class)).forEach(scrumPokerSetting -> {
            if (ACTIVATE_SCRUM_POKER.equals(scrumPokerSetting.getKey())) {
                globalSettings.setActivateScrumPoker(Boolean.parseBoolean(scrumPokerSetting.getValue()));
            }
            if (ESTIMATE_FIELD.equals(scrumPokerSetting.getKey())) {
                globalSettings.setEstimateField(scrumPokerSetting.getValue());
            }
            if (SESSION_TIMEOUT.equals(scrumPokerSetting.getKey())) {
                globalSettings.setSessionTimeout(Integer.valueOf(scrumPokerSetting.getValue()));
            }
            if (ALLOW_REVEAL_DECK.equals(scrumPokerSetting.getKey())) {
                globalSettings.setAllowRevealDeck(AllowRevealDeck.valueOf(scrumPokerSetting.getValue()));
            }
            if (DISPLAY_DROPDOWN_ON_BOARDS.equals(scrumPokerSetting.getKey())) {
                globalSettings.setDisplayDropdownOnBoards(Boolean.parseBoolean(scrumPokerSetting.getValue()));
            }
            if (CHECK_PERMISSION_TO_SAVE_ESTIMATE.equals(scrumPokerSetting.getKey())) {
                globalSettings.setCheckPermissionToSaveEstimate(Boolean.parseBoolean(scrumPokerSetting.getValue()));
            }
            if (DISPLAY_COMMENTS_FOR_ISSUE.equals(scrumPokerSetting.getKey())) {
                globalSettings.setDisplayCommentsForIssue(DisplayCommentsForIssue.valueOf(scrumPokerSetting.getValue()));
            }
            if (CARD_SET.equals(scrumPokerSetting.getKey())) {
                globalSettings.setCardSet(scrumPokerSetting.getValue());
            }
            if (ADDITIONAL_FIELDS.equals(scrumPokerSetting.getKey())) {
                globalSettings.setAdditionalFields(scrumPokerSetting.getValue());
            }
        });
        return globalSettings;
    }

    @Override
    public void persist(GlobalSettings globalSettings) {
        persist(SESSION_TIMEOUT, String.valueOf(globalSettings.getSessionTimeout()));
        persist(ESTIMATE_FIELD, globalSettings.getEstimateField());
        persist(ACTIVATE_SCRUM_POKER, String.valueOf(globalSettings.isActivateScrumPoker()));
        persist(ALLOW_REVEAL_DECK, globalSettings.getAllowRevealDeck().name());
        persist(DISPLAY_DROPDOWN_ON_BOARDS, String.valueOf(globalSettings.isDisplayDropdownOnBoards()));
        persist(CHECK_PERMISSION_TO_SAVE_ESTIMATE, String.valueOf(globalSettings.isCheckPermissionToSaveEstimate()));
        persist(DISPLAY_COMMENTS_FOR_ISSUE, globalSettings.getDisplayCommentsForIssue().name());
        persist(CARD_SET, globalSettings.getCardSet());
        persist(ADDITIONAL_FIELDS, globalSettings.getAdditionalFields());
    }

    private void persist(String key, String value) {
        ScrumPokerSetting scrumPokerSetting = findByKey(key);
        if (scrumPokerSetting == null) {
            scrumPokerSetting = activeObjects.create(ScrumPokerSetting.class, new DBParam("KEY", key));
        }
        scrumPokerSetting.setValue(value);
        scrumPokerSetting.save();
    }

    private ScrumPokerSetting findByKey(String key) {
        ScrumPokerSetting[] scrumPokerSettings = activeObjects.find(ScrumPokerSetting.class,
            Query.select().where("KEY = ?", key).limit(1));
        return (scrumPokerSettings.length == 1) ? scrumPokerSettings[0] : null;
    }

}
