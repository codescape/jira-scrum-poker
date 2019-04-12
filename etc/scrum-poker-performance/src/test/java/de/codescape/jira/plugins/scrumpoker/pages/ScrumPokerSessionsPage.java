package de.codescape.jira.plugins.scrumpoker.pages;

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
        return true; // TODO: How do we identify that we are on the right page?
    }

}
