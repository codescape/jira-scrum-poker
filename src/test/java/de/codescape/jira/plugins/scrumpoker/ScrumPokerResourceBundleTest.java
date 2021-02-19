package de.codescape.jira.plugins.scrumpoker;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ScrumPokerResourceBundleTest {

    private static final String[] SUPPORTED_BUNDLES = {
        "i18n/scrum-poker_de.properties",
        "i18n/scrum-poker_fr.properties",
        "i18n/scrum-poker.properties"
    };
    private static final String LOWER_CASE_LETTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPER_CASE_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String NUMBERS = "0123456789";
    private static final String SPECIAL_CHARACTERS = ".,!?= \\'{}:-+\"()_";
    private static final String ALLOWED_CHARACTERS =
        LOWER_CASE_LETTERS + UPPER_CASE_LETTERS + NUMBERS + SPECIAL_CHARACTERS;

    /* tests for files */

    @Test
    public void resourceBundleContainsExpectedTranslations() throws Exception {
        List<String> resourceBundles = findAllResourceBundles();
        assertThat(resourceBundles.size(), is(equalTo(SUPPORTED_BUNDLES.length)));

        Arrays.stream(SUPPORTED_BUNDLES)
            .map(bundle -> bundle.substring(bundle.indexOf("/") + 1))
            .forEach(bundle -> assertThat(resourceBundles, hasItem(bundle)));
    }

    /* tests for file content */

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

    /* supporting methods */

    private List<String> findAllResourceBundles() throws URISyntaxException {
        URL directory = getClass().getClassLoader().getResource("i18n");
        assertThat("Directory must be found", directory, is(notNullValue()));

        String[] bundles = new File(directory.toURI()).list();
        assertThat("Directory must contain files", bundles, is(notNullValue()));

        return Arrays.asList(bundles);
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
        URL resource = getClass().getClassLoader().getResource(fileName);
        assertThat("File must be found", resource, is(notNullValue()));
        return new File(resource.getFile());
    }

}
