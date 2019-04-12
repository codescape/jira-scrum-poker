package de.codescape.jira.plugins.scrumpoker.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends Page {

    @FindBy(id = "login-form-username")
    private WebElement usernameInput;

    @FindBy(id = "login-form-password")
    private WebElement passwordInput;

    @FindBy(id = "login-form-submit")
    private WebElement loginButton;

    public LoginPage(WebDriver webDriver) {
        super(webDriver);
        openPage();
    }

    @Override
    public String getPageUrl() {
        return "login.jsp";
    }

    @Override
    public boolean verifyPage() {
        return usernameInput.isDisplayed() && passwordInput.isDisplayed() && loginButton.isDisplayed();
    }

    public DashboardPage loginAs(String username, String password) {
        usernameInput.sendKeys(username);
        passwordInput.sendKeys(password);
        loginButton.click();
        return new DashboardPage(webDriver);
    }

}
