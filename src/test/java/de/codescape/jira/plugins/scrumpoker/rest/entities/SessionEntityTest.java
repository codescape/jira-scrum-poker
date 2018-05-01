package de.codescape.jira.plugins.scrumpoker.rest.entities;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class SessionEntityTest {

    @Test
    public void shouldSignalExistenceOfConfirmedVoteIfConfirmedVoteIsSet() {
        assertThat(new SessionEntity().withConfirmedVote(1).isConfirmedVoteExists(), is(true));
    }

    @Test
    public void shouldSignalNoExistenceOfConfirmedVoteIfNoConfirmedVoteIsSet() {
        assertThat(new SessionEntity().withConfirmedVote(null).isConfirmedVoteExists(), is(false));
    }

}
