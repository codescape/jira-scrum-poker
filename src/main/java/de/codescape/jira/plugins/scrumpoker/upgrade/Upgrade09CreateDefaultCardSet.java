package de.codescape.jira.plugins.scrumpoker.upgrade;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.upgrade.PluginUpgradeTask;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerCards;
import de.codescape.jira.plugins.scrumpoker.model.SpecialCards;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Create the simplified Fibonacci card set as the default card set.
 *
 * @since 4.10
 */
@Component
@ExportAsService(PluginUpgradeTask.class)
public class Upgrade09CreateDefaultCardSet extends AbstractUpgradeTask {

    private final ActiveObjects activeObjects;

    @Autowired
    public Upgrade09CreateDefaultCardSet(@ComponentImport ActiveObjects activeObjects) {
        this.activeObjects = activeObjects;
    }

    @Override
    public int getBuildNumber() {
        return 9;
    }

    @Override
    public String getShortDescription() {
        return "Create the simplified Fibonacci card set.";
    }

    @Override
    protected void performUpgrade() {
        ScrumPokerCards scrumPokerCards = activeObjects.create(ScrumPokerCards.class);
        scrumPokerCards.setCardSet(simplifiedFibonacciCardSet());
        scrumPokerCards.save();
    }

    private String simplifiedFibonacciCardSet() {
        return SpecialCards.QUESTION_MARK + "," + SpecialCards.COFFEE_CARD + ",0,1,2,3,5,8,13,20,40,100";
    }

}
