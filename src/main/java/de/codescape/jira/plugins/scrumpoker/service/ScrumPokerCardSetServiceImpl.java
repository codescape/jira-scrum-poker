package de.codescape.jira.plugins.scrumpoker.service;

import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of {@link ScrumPokerCardSetService} using Active Objects as persistence model.
 */
@Component
public class ScrumPokerCardSetServiceImpl implements ScrumPokerCardSetService {

    private final ScrumPokerSettingService scrumPokerSettingService;

    @Autowired
    public ScrumPokerCardSetServiceImpl(ScrumPokerSettingService scrumPokerSettingService) {
        this.scrumPokerSettingService = scrumPokerSettingService;
    }

    @Override
    public List<String> getCardSet() {
        return splitToCards(scrumPokerSettingService.load().getCardSet());
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
