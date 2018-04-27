package de.codescape.jira.plugins.scrumpoker.rest;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class VoteRepresentationTest {

    private static final String USER = "Some User";
    private static final String VOTE = "?";

    @Test
    public void voteReturnsValuesItIsInitializedWith() {
        VoteRepresentation voteRepresentation = new VoteRepresentation(USER, VOTE, true);
        assertThat(voteRepresentation.getUser(), is(equalTo(USER)));
        assertThat(voteRepresentation.getVote(), is(equalTo(VOTE)));
        assertThat(voteRepresentation.isNeedToTalk(), is(equalTo(true)));
    }

}
