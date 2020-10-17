package de.codescape.jira.plugins.scrumpoker.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import static de.codescape.jira.plugins.scrumpoker.MavenProperties.getProperties;
import static org.hamcrest.CoreMatchers.is;

public abstract class Page {

    final WebDriver webDriver;

    Page(WebDriver webDriver) {
        this.webDriver = webDriver;
        PageFactory.initElements(webDriver, this);
    }

    protected abstract String getPageUrl();

    protected abstract boolean verifyPage();

    public final void openPage() {
        String target = getProperties().getProperty("baseUrl") + getPageUrl();
        long before = System.currentTimeMillis();
        webDriver.get(target);
        long duration = System.currentTimeMillis() - before;
        printlnCsvEntry(target, duration);
        assertThat(verifyPage(), is(true));
    }

    private void printlnCsvEntry(String endpoint, long responseTime) {
        String className = this.getClass().getSimpleName();
        System.out.println(className + "\t" + endpoint + "\t" + responseTime);
    }

}
