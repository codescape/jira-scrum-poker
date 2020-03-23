package de.codescape.jira.plugins.scrumpoker.service;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerCards;
import net.java.ao.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of {@link ScrumPokerCardService} using Active Objects as persistence model.
 */
@Component
public class ScrumPokerCardServiceImpl implements ScrumPokerCardService {

    private final ActiveObjects activeObjects;

    @Autowired
    public ScrumPokerCardServiceImpl(@ComponentImport ActiveObjects activeObjects) {
        this.activeObjects = activeObjects;
    }

    @Override
    public List<String> getCardSet() {
        // at the moment we only expect one entry but to make sure we only query for one
        ScrumPokerCards[] cardSets = activeObjects.find(ScrumPokerCards.class, Query.select()
            .order("ID DESC")
            .limit(1));
        return splitToCards(cardSets[0].getCardSet());
    }

    private List<String> splitToCards(String cardSet) {
        return Arrays.stream(cardSet.split(","))
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .collect(Collectors.toList());
    }

}
