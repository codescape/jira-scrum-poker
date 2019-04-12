package de.codescape.jira.plugins.scrumpoker.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class IssueDetailPage extends Page {

    private final String issueKey;

    public IssueDetailPage(WebDriver webDriver, String issueKey) {
        super(webDriver);
        this.issueKey = issueKey;
    }

    @Override
    public String getPageUrl() {
        return "browse/" + issueKey;
    }

    @Override
    public boolean verifyPage() {
        return webDriver.findElement(By.className("issue-view")).isDisplayed();
    }

}
