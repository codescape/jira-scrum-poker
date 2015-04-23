package net.congstar.jira.plugins.planningpoker;


import com.atlassian.crowd.embedded.api.User;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.plugin.webfragment.conditions.AbstractJiraCondition;
import com.atlassian.jira.plugin.webfragment.model.JiraHelper;

public class StoryTypeCondition extends AbstractJiraCondition {

    @Override
    public boolean shouldDisplay(User user, JiraHelper jiraHelper) {
        Issue currentIssue = (Issue) jiraHelper.getContextParams().get("issue");
        return currentIssue.getIssueTypeObject().getName().equals("Story");
    }

}
