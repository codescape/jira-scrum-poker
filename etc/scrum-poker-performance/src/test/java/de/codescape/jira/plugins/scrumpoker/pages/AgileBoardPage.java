package de.codescape.jira.plugins.scrumpoker.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class AgileBoardPage extends Page {

    private final String project;
    private final String boardId;

    @FindBy(id = "ghx-board-name")
    private WebElement boardName;

    public AgileBoardPage(WebDriver webDriver, String project, String boardId) {
        super(webDriver);
        this.project = project;
        this.boardId = boardId;
    }

    @Override
    public String getPageUrl() {
        return "secure/RapidBoard.jspa?projectKey=" + project + "&rapidView=" + boardId + "&view=planning";
    }

    @Override
    public boolean verifyPage() {
        return boardName.isDisplayed();
    }

}
