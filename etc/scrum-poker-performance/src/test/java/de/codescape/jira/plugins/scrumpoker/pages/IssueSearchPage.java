package de.codescape.jira.plugins.scrumpoker.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class IssueSearchPage extends Page {

    public IssueSearchPage(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    public String getPageUrl() {
        return "issues/?jql=";
    }

    @Override
    public boolean verifyPage() {
        return webDriver.findElement(By.className("navigator-container")).isDisplayed();
    }

}
