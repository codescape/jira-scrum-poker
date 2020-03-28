package de.codescape.jira.plugins.scrumpoker.service;

import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of {@link CardSetService} using Active Objects as persistence model.
 */
@Component
public class CardSetServiceImpl implements CardSetService {

    private final GlobalSettingsService globalSettingsService;

    @Autowired
    public CardSetServiceImpl(GlobalSettingsService globalSettingsService) {
        this.globalSettingsService = globalSettingsService;
    }

    @Override
    public List<String> getCardSet() {
        return splitToCards(globalSettingsService.load().getCardSet());
    }

    @Override
    public List<String> getCardSet(ScrumPokerSession scrumPokerSession) {
        return scrumPokerSession.getCardSet() != null ? splitToCards(scrumPokerSession.getCardSet()) : getCardSet();
    }

    private List<String> splitToCards(String cardSet) {
        return Arrays.stream(cardSet.split(","))
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .collect(Collectors.toList());
    }

}
