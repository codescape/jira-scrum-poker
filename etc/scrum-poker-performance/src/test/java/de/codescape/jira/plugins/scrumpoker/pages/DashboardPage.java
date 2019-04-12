package de.codescape.jira.plugins.scrumpoker.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class DashboardPage extends Page {

    public DashboardPage(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    public String getPageUrl() {
        return "secure/Dashboard.jspa";
    }

    @Override
    public boolean verifyPage() {
        return webDriver.findElement(By.id("dashboard")).isDisplayed();
    }

}
