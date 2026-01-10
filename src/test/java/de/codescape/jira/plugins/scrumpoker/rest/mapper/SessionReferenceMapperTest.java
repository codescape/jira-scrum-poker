package de.codescape.jira.plugins.scrumpoker.rest.mapper;

import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.issuetype.IssueType;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerSession;
import de.codescape.jira.plugins.scrumpoker.rest.entities.ReferenceListEntity;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SessionReferenceMapperTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private IssueManager issueManager;

    @InjectMocks
    private SessionReferenceMapper sessionReferenceMapper;

    @Test
    public void shouldMapTheEstimation() {
        ReferenceListEntity referenceListEntity = sessionReferenceMapper.build(new ArrayList<>(), "5");
        assertThat(referenceListEntity.getEstimate(), is("5"));
    }

    @Test
    public void shouldReturnEmptyListForNoReferences() {
        ReferenceListEntity referenceListEntity = sessionReferenceMapper.build(new ArrayList<>(), "5");
        assertThat(referenceListEntity.isResults(), is(false));
        assertThat(referenceListEntity.getReferences().size(), is(0));
    }

    @Test
    public void shouldExcludeIssuesWhereIssueCannotBeFound() {
        issueManagerKnowsIssueWithKey("ISSUE-5");
        List<ScrumPokerSession> scrumPokerSessions = scrumPokerSessionsWithKeys("ISSUE-5", "UNKNOWN-ISSUE-1");
        ReferenceListEntity referenceListEntity = sessionReferenceMapper.build(scrumPokerSessions, "5");
        assertThat(referenceListEntity.isResults(), is(true));
        assertThat(referenceListEntity.getReferences().size(), is(1));
        assertThat(referenceListEntity.getReferences().getFirst().getIssueKey(), equalTo("ISSUE-5"));
    }

    private List<ScrumPokerSession> scrumPokerSessionsWithKeys(String... keys) {
        return Arrays.stream(keys).map(this::sessionForIssueKey).collect(Collectors.toList());
    }

    private void issueManagerKnowsIssueWithKey(String issueKey) {
        MutableIssue issue = mock(MutableIssue.class);
        IssueType issueType = mock(IssueType.class);
        when(issue.getIssueType()).thenReturn(issueType);
        when(issue.getKey()).thenReturn(issueKey);
        when(issueManager.getIssueObject(issueKey)).thenReturn(issue);
    }

    private ScrumPokerSession sessionForIssueKey(String issueKey) {
        ScrumPokerSession scrumPokerSession = mock(ScrumPokerSession.class);
        when(scrumPokerSession.getIssueKey()).thenReturn(issueKey);
        return scrumPokerSession;
    }

}
