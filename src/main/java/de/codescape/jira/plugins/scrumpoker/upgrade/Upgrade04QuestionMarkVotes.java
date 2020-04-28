package de.codescape.jira.plugins.scrumpoker.upgrade;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.upgrade.PluginUpgradeTask;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerVote;
import net.java.ao.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static de.codescape.jira.plugins.scrumpoker.model.Card.QUESTION_MARK;

/**
 * Migrate persisted votes that contain a question mark and save with the new value 'question'.
 *
 * @since 3.13.0
 */
@Component
@ExportAsService(PluginUpgradeTask.class)
public class Upgrade04QuestionMarkVotes extends AbstractUpgradeTask {

    private static final String LEGACY_QUESTION_MARK = "?";

    private final ActiveObjects activeObjects;

    @Autowired
    public Upgrade04QuestionMarkVotes(@ComponentImport ActiveObjects activeObjects) {
        this.activeObjects = activeObjects;
    }

    @Override
    public int getBuildNumber() {
        return 4;
    }

    @Override
    public String getShortDescription() {
        return "Migrate old votes with question marks.";
    }

    @Override
    protected void performUpgrade() {
        ScrumPokerVote[] scrumPokerVotes = activeObjects.find(ScrumPokerVote.class,
            Query.select().where("VOTE = ?", LEGACY_QUESTION_MARK));
        Arrays.stream(scrumPokerVotes).forEach(vote -> {
            vote.setVote(QUESTION_MARK.getValue());
            vote.save();
        });
    }

}
