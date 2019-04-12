package de.codescape.jira.plugins.scrumpoker;

import de.codescape.jira.plugins.scrumpoker.pages.LoginPage;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static de.codescape.jira.plugins.scrumpoker.MavenProperties.getProperties;

public class BasePerformanceTest {

    protected static WebDriver webDriver;

    @BeforeClass
    public static void launchApplication() {
        initializeWebDriver();
    }

    @AfterClass
    public static void closeBrowser() {
        webDriver.quit();
    }

    /**
     * Creates the Chrome driver to run the performance tests with.
     */
    private static void initializeWebDriver() {
        System.setProperty("webdriver.chrome.driver", getProperties().getProperty("chromeDriver"));
        webDriver = new ChromeDriver();
        webDriver.manage().window().maximize();
    }

    /**
     * Performs a login with the default admin credentials.
     */
    protected void withUserLoggedIn() {
        LoginPage loginPage = new LoginPage(webDriver);
        loginPage.loginAs("admin", "admin");
    }

    /**
     * Returns the configured number of requests to be send. Can be configures in pom.xml.
     */
    protected static int pageRequests() {
        return Integer.valueOf(getProperties().getProperty("pageRequests"));
    }

}
