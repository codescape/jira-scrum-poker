package de.codescape.jira.plugins.scrumpoker.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ScrumPokerPage extends Page {

    private final String issueKey;

    @FindBy(id = "key-val")
    private WebElement issueLink;

    public ScrumPokerPage(WebDriver webDriver, String issueKey) {
        super(webDriver);
        this.issueKey = issueKey;
    }

    @Override
    public String getPageUrl() {
        return "secure/ScrumPoker.jspa?issueKey=" + issueKey;
    }

    @Override
    public boolean verifyPage() {
        return issueLink.isDisplayed() && issueLink.getText().equals(issueKey);
    }

}
