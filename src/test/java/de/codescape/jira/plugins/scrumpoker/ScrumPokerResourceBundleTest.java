package de.codescape.jira.plugins.scrumpoker;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;
import java.util.Scanner;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class ScrumPokerResourceBundleTest {

    private static final String[] SUPPORTED_BUNDLES = {
        "scrum-poker_de_DE.properties",
        "scrum-poker.properties"
    };

    @Test
    public void resourceBundlesMustNotContainGermanUmlauts() {
        Arrays.stream(SUPPORTED_BUNDLES).forEach(bundleName -> {
                try (Scanner scanner = new Scanner(getFile(bundleName))) {
                    while (scanner.hasNextLine()) {
                        assertThat(scanner.nextLine(), not(anyOf(
                            containsString("ä"),
                            containsString("ö"),
                            containsString("ü"),
                            containsString("Ä"),
                            containsString("Ö"),
                            containsString("Ü"),
                            containsString("ß"))));
                    }
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        );
    }

    @Test
    public void resourceBundlesMustNotIncludeMalformedUnicodeEscapes() {
        Arrays.stream(SUPPORTED_BUNDLES).forEach(bundleName -> {
                try {
                    new Properties().load(new FileReader(getFile(bundleName)));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        );
    }

    private File getFile(String fileName) {
        return new File(getClass().getClassLoader().getResource(fileName).getFile());
    }

}
