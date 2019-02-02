package de.codescape.jira.plugins.scrumpoker;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.stream.Collectors;

import static org.junit.Assert.assertThat;

public class ScrumPokerResourceBundleTest {

    private static final String[] SUPPORTED_BUNDLES = {
        "scrum-poker_de.properties",
        "scrum-poker_fr.properties",
        "scrum-poker.properties"
    };
    private static final String LOWER_CASE_LETTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPER_CASE_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String NUMBERS = "0123456789";
    private static final String SPECIAL_CHARACTERS = ".,!?= \\'{}:-+\"";
    private static final String ALLOWED_CHARACTERS = LOWER_CASE_LETTERS + UPPER_CASE_LETTERS + NUMBERS + SPECIAL_CHARACTERS;

    @Test
    public void resourceBundlesMustOnlyContainAllowedCharacters() {
        Arrays.stream(SUPPORTED_BUNDLES).forEach(bundleName -> {
                try (Scanner scanner = new Scanner(getFile(bundleName))) {
                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine();
                        if (isNotACommentLine(line)) {
                            assertThat(line, onlyContainsCharacters(ALLOWED_CHARACTERS));
                        }
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

    private boolean isNotACommentLine(String line) {
        return !line.startsWith("#") && !line.startsWith("!");
    }

    private static TypeSafeMatcher<String> onlyContainsCharacters(final String characters) {
        return new TypeSafeMatcher<String>() {

            @Override
            protected boolean matchesSafely(String line) {
                return Arrays.stream(line.split(""))
                    .allMatch(characters::contains);
            }

            @Override
            protected void describeMismatchSafely(String line, Description mismatchDescription) {
                List<String> offendingLetters = Arrays.stream(line.split(""))
                    .filter(element -> !characters.contains(element))
                    .collect(Collectors.toList());
                mismatchDescription
                    .appendValue(line)
                    .appendText(" contained ")
                    .appendValue(offendingLetters);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("must only contain allowed characters " + characters);
            }

        };
    }

    private File getFile(String fileName) {
        return new File(getClass().getClassLoader().getResource(fileName).getFile());
    }

}
