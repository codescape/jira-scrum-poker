package de.codescape.jira.plugins.scrumpoker.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ScrumPokerSessionsPage extends Page {

    public ScrumPokerSessionsPage(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    public String getPageUrl() {
        return "secure/ScrumPokerSessions.jspa";
    }

    @Override
    public boolean verifyPage() {
        return webDriver.findElement(By.className("scrum-poker-sessions")).isDisplayed();
    }

}
