package de.codescape.jira.plugins.scrumpoker.upgrade;

import com.atlassian.activeobjects.test.TestActiveObjects;
import de.codescape.jira.plugins.scrumpoker.ScrumPokerTestDatabaseUpdater;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerCards;
import de.codescape.jira.plugins.scrumpoker.model.SpecialCards;
import net.java.ao.EntityManager;
import net.java.ao.test.converters.NameConverters;
import net.java.ao.test.jdbc.Data;
import net.java.ao.test.jdbc.Hsql;
import net.java.ao.test.jdbc.Jdbc;
import net.java.ao.test.junit.ActiveObjectsJUnitRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(ActiveObjectsJUnitRunner.class)
@Data(ScrumPokerTestDatabaseUpdater.class)
@Jdbc(Hsql.class)
@NameConverters
public class Upgrade09CreateDefaultCardSetTest {

    @SuppressWarnings("unused")
    private EntityManager entityManager;
    private TestActiveObjects activeObjects;

    private Upgrade09CreateDefaultCardSet upgrade;

    @Before
    public void before() {
        activeObjects = new TestActiveObjects(entityManager);
        upgrade = new Upgrade09CreateDefaultCardSet(activeObjects);
    }

    @Test
    public void expectTheCardSetToBeSetAfterTheUpdate() {
        upgrade.performUpgrade();
        ScrumPokerCards[] scrumPokerCards = activeObjects.find(ScrumPokerCards.class);
        assertThat(scrumPokerCards.length, is(1));
        assertThat(scrumPokerCards[0].getCardSet(), is(equalTo(
            SpecialCards.QUESTION_MARK + "," + SpecialCards.COFFEE_CARD + ",0,1,2,3,5,8,13,20,40,100")));
    }

}
