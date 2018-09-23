package de.codescape.jira.plugins.scrumpoker.upgrade;

import com.atlassian.sal.api.message.Message;
import com.atlassian.sal.api.upgrade.PluginUpgradeTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * Base class to be used when implementing {@link PluginUpgradeTask}.
 */
abstract class AbstractUpgradeTask implements PluginUpgradeTask {

    private static final Logger log = LoggerFactory.getLogger(AbstractUpgradeTask.class);

    static final String SCRUM_POKER_PLUGIN_KEY = "de.codescape.jira.plugins.scrum-poker";

    @Override
    public final Collection<Message> doUpgrade() {
        log.info("Upgrade task {} to build #{} starting", getClass().getSimpleName(), getBuildNumber());
        performUpgrade();
        log.info("Upgrade task {} to build #{} finished", getClass().getSimpleName(), getBuildNumber());
        return null;
    }

    /**
     * Implementation of the upgrade task.
     */
    protected abstract void performUpgrade();

    @Override
    public final String getPluginKey() {
        return SCRUM_POKER_PLUGIN_KEY;
    }

}
