package de.codescape.jira.plugins.scrumpoker.pages;

import org.openqa.selenium.WebDriver;

public class BrowseProjectPage extends Page {

    private final String project;

    public BrowseProjectPage(WebDriver webDriver, String project) {
        super(webDriver);
        this.project = project;
    }

    @Override
    public String getPageUrl() {
        return "browse/" + project + "/issues";
    }

    @Override
    public boolean verifyPage() {
        return true; // TODO: How do we identify that we are on the right page here?
    }

}
