package de.codescape.jira.plugins.scrumpoker;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MavenProperties {

    private static Properties properties;

    /**
     * Resolves the properties from the Maven project configuration file pom.xml.
     */
    public static Properties getProperties() {
        if (properties == null)
            initializeProperties();
        return properties;
    }

    private static void initializeProperties() {
        properties = new Properties();
        final ClassLoader classLoader = BasePerformanceTest.class.getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream("maven.properties")) {
            if (inputStream == null)
                throw new RuntimeException("selenium.properties not found");
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
