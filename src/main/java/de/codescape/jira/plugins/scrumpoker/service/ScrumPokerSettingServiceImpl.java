package de.codescape.jira.plugins.scrumpoker.service;

import com.atlassian.activeobjects.external.ActiveObjects;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerSetting;
import net.java.ao.DBParam;
import net.java.ao.Query;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Implementation of {@link ScrumPokerSettingService} using Active Objects as persistence model.
 */
@Component
public class ScrumPokerSettingServiceImpl implements ScrumPokerSettingService {

    static final String STORY_POINT_FIELD = "storyPointField";
    static final String SESSION_TIMEOUT = "sessionTimeout";
    static final Integer SESSION_TIMEOUT_DEFAULT = 12;

    private final ActiveObjects activeObjects;

    @Autowired
    public ScrumPokerSettingServiceImpl(ActiveObjects activeObjects) {
        this.activeObjects = checkNotNull(activeObjects);
    }

    @Override
    public String loadStoryPointField() {
        ScrumPokerSetting scrumPokerSetting = findByKey(STORY_POINT_FIELD);
        return (scrumPokerSetting != null) ? scrumPokerSetting.getValue() : null;
    }

    @Override
    public Integer loadSessionTimeout() {
        ScrumPokerSetting scrumPokerSetting = findByKey(SESSION_TIMEOUT);
        return (scrumPokerSetting != null && NumberUtils.isNumber(scrumPokerSetting.getValue()))
            ? Integer.valueOf(scrumPokerSetting.getValue()) : SESSION_TIMEOUT_DEFAULT;
    }

    @Override
    public void persistSettings(String storyPointField, String sessionTimeout) {
        persist(STORY_POINT_FIELD, storyPointField);
        persist(SESSION_TIMEOUT, sessionTimeout);
    }

    private ScrumPokerSetting findByKey(String key) {
        ScrumPokerSetting[] scrumPokerSettings = activeObjects.find(ScrumPokerSetting.class,
            Query.select().where("KEY = ?", key).limit(1));
        return (scrumPokerSettings.length == 1) ? scrumPokerSettings[0] : null;
    }

    private void persist(String key, String value) {
        ScrumPokerSetting scrumPokerSetting = findByKey(key);
        if (scrumPokerSetting == null) {
            scrumPokerSetting = activeObjects.create(ScrumPokerSetting.class,
                new DBParam("KEY", key));
        }
        scrumPokerSetting.setValue(value);
        scrumPokerSetting.save();
    }

}
