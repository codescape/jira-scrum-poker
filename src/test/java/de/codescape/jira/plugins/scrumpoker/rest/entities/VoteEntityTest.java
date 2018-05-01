package de.codescape.jira.plugins.scrumpoker.rest.entities;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
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

}
