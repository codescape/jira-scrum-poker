package de.codescape.jira.plugins.scrumpoker.ao;

import com.atlassian.activeobjects.test.TestActiveObjects;
import de.codescape.jira.plugins.scrumpoker.ScrumPokerTestDatabaseUpdater;
import net.java.ao.EntityManager;
import net.java.ao.test.converters.NameConverters;
import net.java.ao.test.jdbc.Data;
import net.java.ao.test.jdbc.Hsql;
import net.java.ao.test.jdbc.Jdbc;
import net.java.ao.test.junit.ActiveObjectsJUnitRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(ActiveObjectsJUnitRunner.class)
@Data(ScrumPokerTestDatabaseUpdater.class)
@Jdbc(Hsql.class)
@NameConverters
public class ScrumPokerErrorTest {

    @SuppressWarnings("unused")
    private EntityManager entityManager;
    private TestActiveObjects activeObjects;

    @Before
    public void before() {
        activeObjects = new TestActiveObjects(entityManager);
    }

    @Test
    public void shouldPersistScrumPokerError() {
        ScrumPokerError scrumPokerError = activeObjects.create(ScrumPokerError.class);
        scrumPokerError.save();
        assertThat(scrumPokerError.getID(), is(notNullValue()));
    }

    @Test
    public void shouldPersistScrumPokerErrorWithAllFields() {
        ScrumPokerError scrumPokerError = activeObjects.create(ScrumPokerError.class);
        scrumPokerError.setErrorMessage("The error message.");
        scrumPokerError.setErrorTimestamp(new Date());
        scrumPokerError.setJiraVersion("8.8.0");
        scrumPokerError.setScrumPokerVersion("4.10");
        scrumPokerError.setStacktrace(stacktraceFrom(new RuntimeException("We need a stacktrace!")));

        scrumPokerError.save();

        assertThat(scrumPokerError.getID(), is(notNullValue()));
        assertThat(scrumPokerError.getErrorMessage(), containsString("error message"));
        assertThat(scrumPokerError.getErrorTimestamp(), is(notNullValue()));
        assertThat(scrumPokerError.getJiraVersion(), is(equalTo("8.8.0")));
        assertThat(scrumPokerError.getScrumPokerVersion(), is(equalTo("4.10")));
        assertThat(scrumPokerError.getStacktrace(), containsString("RuntimeException"));
    }

    private String stacktraceFrom(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        throwable.printStackTrace(printWriter);
        return stringWriter.toString();
    }

}
