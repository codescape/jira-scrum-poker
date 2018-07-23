package de.codescape.jira.plugins.scrumpoker.service;

import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.issuetype.IssueType;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerSession;
import de.codescape.jira.plugins.scrumpoker.rest.entities.ReferenceListEntity;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SessionReferenceTransformerTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private IssueManager issueManager;

    @InjectMocks
    private SessionReferenceTransformer sessionReferenceTransformer;

    @Before
    public void before() {
        MutableIssue issue = mock(MutableIssue.class);
        IssueType issueType = mock(IssueType.class);
        when(issue.getIssueType()).thenReturn(issueType);
        when(issueManager.getIssueObject(Matchers.startsWith("ISSUE-"))).thenReturn(issue);
    }

    @Test
    public void shouldMapTheEstimation() {
        ReferenceListEntity referenceListEntity = sessionReferenceTransformer.build(new ArrayList<>(), 5);
        assertThat(referenceListEntity.getEstimation(), is(5));
    }

    @Test
    public void shouldReturnEmptyListForNoReferences() {
        ReferenceListEntity referenceListEntity = sessionReferenceTransformer.build(new ArrayList<>(), 5);
        assertThat(referenceListEntity.isResults(), is(false));
        assertThat(referenceListEntity.getReferences().size(), is(0));
    }

    @Test
    public void shouldExcludeIssuesWhereIssueCannotBeFound() {
        ArrayList<ScrumPokerSession> scrumPokerSessions = new ArrayList<>();
        scrumPokerSessions.add(sessionForIssueKey("ISSUE-5"));
        scrumPokerSessions.add(sessionForIssueKey("NOT-FOUND-1"));
        ReferenceListEntity referenceListEntity = sessionReferenceTransformer.build(scrumPokerSessions, 5);
        assertThat(referenceListEntity.isResults(), is(true));
        assertThat(referenceListEntity.getReferences().size(), is(1));
        assertThat(referenceListEntity.getReferences().get(0).getIssueKey(), equalTo("ISSUE-5"));
    }

    private ScrumPokerSession sessionForIssueKey(String issueKey) {
        ScrumPokerSession scrumPokerSession = mock(ScrumPokerSession.class);
        when(scrumPokerSession.getIssueKey()).thenReturn(issueKey);
        return scrumPokerSession;
    }

}
