package de.codescape.jira.plugins.scrumpoker.rest.entities;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class VoteEntityTest {

    private static final String USER = "Some User";
    private static final String VOTE = "?";

    @Test
    public void voteReturnsValuesItIsInitializedWith() {
        VoteEntity voteEntity = new VoteEntity(USER, VOTE, true, false);
        assertThat(voteEntity.getUser(), is(equalTo(USER)));
        assertThat(voteEntity.getVote(), is(equalTo(VOTE)));
        assertThat(voteEntity.isNeedToTalk(), is(equalTo(true)));
        assertThat(voteEntity.isNeedABreak(), is(equalTo(false)));
    }

    @Test
    public void jsonRepresentationContainsAllFields() throws Exception {
        String json = new ObjectMapper().writeValueAsString(new VoteEntity(USER, VOTE, false, true));
        assertThat(json, containsString("user"));
        assertThat(json, containsString("vote"));
        assertThat(json, containsString("needToTalk"));
        assertThat(json, containsString("needABreak"));
    }

}
