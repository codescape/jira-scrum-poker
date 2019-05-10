package de.codescape.jira.plugins.scrumpoker.service;

import com.atlassian.activeobjects.external.ActiveObjects;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerSetting;
import de.codescape.jira.plugins.scrumpoker.model.AllowRevealDeck;
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

    private final ActiveObjects activeObjects;

    @Autowired
    public ScrumPokerSettingServiceImpl(ActiveObjects activeObjects) {
        this.activeObjects = activeObjects;
    }

    @Override
    public GlobalSettings load() {
        GlobalSettings globalSettings = new GlobalSettings();
        globalSettings.setStoryPointField(loadString(STORY_POINT_FIELD, null));
        globalSettings.setSessionTimeout(loadInteger(SESSION_TIMEOUT, SESSION_TIMEOUT_DEFAULT));
        globalSettings.setAllowRevealDeck(AllowRevealDeck.valueOf(loadString(ALLOW_REVEAL_DECK, ALLOW_REVEAL_DECK_DEFAULT.name())));
        globalSettings.setDefaultProjectActivation(loadBoolean(DEFAULT_PROJECT_ACTIVATION, DEFAULT_PROJECT_ACTIVATION_DEFAULT));
        return globalSettings;
    }

    @Override
    public void persist(GlobalSettings globalSettings) {
        persist(SESSION_TIMEOUT, String.valueOf(globalSettings.getSessionTimeout()));
        persist(STORY_POINT_FIELD, globalSettings.getStoryPointField());
        persist(DEFAULT_PROJECT_ACTIVATION, String.valueOf(globalSettings.isDefaultProjectActivation()));
        persist(ALLOW_REVEAL_DECK, globalSettings.getAllowRevealDeck().name());
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

    @SuppressWarnings("SameParameterValue")
    private boolean loadBoolean(String key, boolean defaultValue) {
        ScrumPokerSetting scrumPokerSetting = findByKey(key);
        return (scrumPokerSetting != null) ? Boolean.valueOf(scrumPokerSetting.getValue()) : defaultValue;
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
