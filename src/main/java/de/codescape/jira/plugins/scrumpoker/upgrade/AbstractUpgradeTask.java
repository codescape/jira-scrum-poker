package de.codescape.jira.plugins.scrumpoker.upgrade;

import com.atlassian.sal.api.message.Message;
import com.atlassian.sal.api.upgrade.PluginUpgradeTask;

import java.util.Collection;

/**
 * Base class to be used when implementing {@link PluginUpgradeTask}.
 */
public abstract class AbstractUpgradeTask implements PluginUpgradeTask {

    public static final String SCRUM_POKER_PLUGIN_KEY = "de.codescape.jira.plugins.scrum-poker";

    @Override
    public Collection<Message> doUpgrade() {
        performUpgrade();
        return null;
    }

    /**
     * Implementation of the upgrade task.
     */
    protected abstract void performUpgrade();

    @Override
    public String getPluginKey() {
        return SCRUM_POKER_PLUGIN_KEY;
    }

}
