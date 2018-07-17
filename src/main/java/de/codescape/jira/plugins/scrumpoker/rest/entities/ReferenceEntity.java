package de.codescape.jira.plugins.scrumpoker.rest.entities;

import org.codehaus.jackson.annotate.JsonAutoDetect;

/**
 * REST representation of a reference issue to be displayed during a Scrum poker session.
 */
@JsonAutoDetect
public class ReferenceEntity {

    private final String issueIcon;
    private final String issueKey;
    private final String issueSummary;

    public ReferenceEntity(String issueKey, String issueIcon, String issueSummary) {
        this.issueKey = issueKey;
        this.issueIcon = issueIcon;
        this.issueSummary = issueSummary;
    }

    public String getIssueKey() {
        return issueKey;
    }

    public String getIssueIcon() {
        return issueIcon;
    }

    public String getIssueSummary() {
        return issueSummary;
    }

}
