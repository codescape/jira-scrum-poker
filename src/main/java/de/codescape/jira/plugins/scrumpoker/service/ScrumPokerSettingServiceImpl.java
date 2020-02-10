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

import static de.codescape.jira.plugins.scrumpoker.model.GlobalSettings.*;

/**
 * Implementation of {@link ScrumPokerSettingService} using Active Objects as persistence model.
 */
@Component
public class ScrumPokerSettingServiceImpl implements ScrumPokerSettingService {

    private static final String STORY_POINT_FIELD = "storyPointField";
    private static final String SESSION_TIMEOUT = "sessionTimeout";
    private static final String DEFAULT_PROJECT_ACTIVATION = "defaultProjectActivation";
    private static final String ALLOW_REVEAL_DECK = "allowRevealDeck";
    private static final String DISPLAY_DROPDOWN_ON_BOARDS = "displayDropdownOnBoards";
    private static final String CHECK_PERMISSION_TO_SAVE_ESTIMATE = "checkPermissionToSaveEstimate";
    private static final String DISPLAY_COMMENTS_FOR_ISSUE = "displayCommentsForIssue";

    private final ActiveObjects activeObjects;

    @Autowired
    public ScrumPokerSettingServiceImpl(@ComponentImport ActiveObjects activeObjects) {
        this.activeObjects = activeObjects;
    }

    @Override
    public GlobalSettings load() {
        GlobalSettings globalSettings = new GlobalSettings();
        globalSettings.setStoryPointField(
            loadString(STORY_POINT_FIELD, null));
        globalSettings.setSessionTimeout(
            loadInteger(SESSION_TIMEOUT, SESSION_TIMEOUT_DEFAULT));
        globalSettings.setAllowRevealDeck(
            AllowRevealDeck.valueOf(loadString(ALLOW_REVEAL_DECK, ALLOW_REVEAL_DECK_DEFAULT.name())));
        globalSettings.setDefaultProjectActivation(
            loadBoolean(DEFAULT_PROJECT_ACTIVATION, DEFAULT_PROJECT_ACTIVATION_DEFAULT));
        globalSettings.setDisplayDropdownOnBoards(
            loadBoolean(DISPLAY_DROPDOWN_ON_BOARDS, DISPLAY_DROPDOWN_ON_BOARDS_DEFAULT));
        globalSettings.setCheckPermissionToSaveEstimate(
            loadBoolean(CHECK_PERMISSION_TO_SAVE_ESTIMATE, CHECK_PERMISSION_TO_SAVE_ESTIMATE_DEFAULT));
        globalSettings.setDisplayCommentsForIssue(DisplayCommentsForIssue.valueOf(
            loadString(DISPLAY_COMMENTS_FOR_ISSUE, DISPLAY_COMMENTS_FOR_ISSUE_DEFAULT.name())));
        return globalSettings;
    }

    @Override
    public void persist(GlobalSettings globalSettings) {
        persist(SESSION_TIMEOUT, String.valueOf(globalSettings.getSessionTimeout()));
        persist(STORY_POINT_FIELD, globalSettings.getStoryPointField());
        persist(DEFAULT_PROJECT_ACTIVATION, String.valueOf(globalSettings.isDefaultProjectActivation()));
        persist(ALLOW_REVEAL_DECK, globalSettings.getAllowRevealDeck().name());
        persist(DISPLAY_DROPDOWN_ON_BOARDS, String.valueOf(globalSettings.isDisplayDropdownOnBoards()));
        persist(CHECK_PERMISSION_TO_SAVE_ESTIMATE, String.valueOf(globalSettings.isCheckPermissionToSaveEstimate()));
        persist(DISPLAY_COMMENTS_FOR_ISSUE, globalSettings.getDisplayCommentsForIssue().name());
    }

    private String loadString(String key, String defaultValue) {
        ScrumPokerSetting scrumPokerSetting = findByKey(key);
        return (scrumPokerSetting != null) ? scrumPokerSetting.getValue() : defaultValue;
    }

    @SuppressWarnings("SameParameterValue")
    private Integer loadInteger(String key, Integer defaultValue) {
        ScrumPokerSetting scrumPokerSetting = findByKey(key);
        return (scrumPokerSetting != null) ? Integer.valueOf(scrumPokerSetting.getValue()) : defaultValue;
    }

    private boolean loadBoolean(String key, boolean defaultValue) {
        ScrumPokerSetting scrumPokerSetting = findByKey(key);
        return (scrumPokerSetting != null) ? Boolean.parseBoolean(scrumPokerSetting.getValue()) : defaultValue;
    }

    private ScrumPokerSetting findByKey(String key) {
        ScrumPokerSetting[] scrumPokerSettings = activeObjects.find(ScrumPokerSetting.class,
            Query.select().where("KEY = ?", key).limit(1));
        return (scrumPokerSettings.length == 1) ? scrumPokerSettings[0] : null;
    }

    private void persist(String key, String value) {
        ScrumPokerSetting scrumPokerSetting = findByKey(key);
        if (scrumPokerSetting == null) {
            scrumPokerSetting = activeObjects.create(ScrumPokerSetting.class, new DBParam("KEY", key));
        }
        scrumPokerSetting.setValue(value);
        scrumPokerSetting.save();
    }

}
