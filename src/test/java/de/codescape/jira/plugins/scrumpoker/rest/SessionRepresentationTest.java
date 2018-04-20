package de.codescape.jira.plugins.scrumpoker.rest;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class SessionRepresentationTest {

    @Test
    public void shouldSignalExistenceOfConfirmedVoteIfConfirmedVoteIsSet() {
        assertThat(new SessionRepresentation().withConfirmedVote(1).isConfirmedVoteExists(), is(true));
    }

    @Test
    public void shouldSignalNoExistenceOfConfirmedVoteIfNoConfirmedVoteIsSet() {
        assertThat(new SessionRepresentation().withConfirmedVote(null).isConfirmedVoteExists(), is(false));
    }

}
