package de.codescape.jira.plugins.scrumpoker.rest.entities;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class VoteEntityTest {

    private static final String USER = "Some User";
    private static final String VOTE = "?";

    @Test
    public void voteReturnsValuesItIsInitializedWith() {
        VoteEntity voteEntity = new VoteEntity(USER, VOTE, true);
        assertThat(voteEntity.getUser(), is(equalTo(USER)));
        assertThat(voteEntity.getVote(), is(equalTo(VOTE)));
        assertThat(voteEntity.isNeedToTalk(), is(equalTo(true)));
    }

    @Test
    public void jsonRepresentationContainsAllFields() throws Exception {
        String json = new ObjectMapper().writeValueAsString(new VoteEntity(USER, VOTE, false));
        assertThat(json, containsString("user"));
        assertThat(json, containsString("vote"));
        assertThat(json, containsString("needToTalk"));
    }

}
