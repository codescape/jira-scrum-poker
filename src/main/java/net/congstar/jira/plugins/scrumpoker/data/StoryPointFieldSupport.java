package net.congstar.jira.plugins.scrumpoker.data;

public interface StoryPointFieldSupport {

    void save(String issueKey, Double newValue);

    Double getValue(String issueKey);

}
