package net.congstar.jira.plugins.planningpoker.data;

public interface StoryPointFieldSupport {

    void save(String issueKey, Double newValue);

    Double getValue(String issueKey);

}
