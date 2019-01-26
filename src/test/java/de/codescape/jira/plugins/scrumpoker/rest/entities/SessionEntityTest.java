package de.codescape.jira.plugins.scrumpoker.rest.entities;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.containsString;
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

    @Test
    public void shouldReturnNumberOfVotesWhenVotesAreGiven() {
        assertThat(new SessionEntity().withVotes(listOfVotes(3)).getVoteCount(), is(3));
    }

    @Test
    public void shouldReturnZeroVotesWhenNoVotesAreGiven() {
        assertThat(new SessionEntity().withVotes(listOfVotes(0)).getVoteCount(), is(0));
    }

    @Test
    public void jsonRepresentationsContainsAllFields() throws Exception {
        String json = new ObjectMapper().writeValueAsString(new SessionEntity());
        assertThat(json, containsString("issueKey"));
        assertThat(json, containsString("cards"));
        assertThat(json, containsString("confirmedVote"));
        assertThat(json, containsString("visible"));
        assertThat(json, containsString("boundedVotes"));
        assertThat(json, containsString("agreementReached"));
        assertThat(json, containsString("votes"));
        assertThat(json, containsString("voteCount"));
        assertThat(json, containsString("allowReset"));
        assertThat(json, containsString("allowReveal"));
    }

    private List<VoteEntity> listOfVotes(Integer voteCount) {
        return IntStream.range(0, voteCount)
            .mapToObj(i -> new VoteEntity("SomeUser", "SomeVote", false))
            .collect(Collectors.toList());
    }

}
