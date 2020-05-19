package de.codescape.jira.plugins.scrumpoker.service;

import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerSession;
import de.codescape.jira.plugins.scrumpoker.model.Card;
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
    public List<Card> getCardSet() {
        return splitToCards(globalSettingsService.load().getCardSet());
    }

    @Override
    public List<Card> getCardSet(ScrumPokerSession scrumPokerSession) {
        return scrumPokerSession.getCardSet() != null ? splitToCards(scrumPokerSession.getCardSet()) : getCardSet();
    }

    private List<Card> splitToCards(String cardSet) {
        return Arrays.stream(cardSet.split(","))
            .map(String::trim)
            .filter(cardValue -> !cardValue.isEmpty())
            .map(cardValue -> new Card(cardValue, isAssignable(cardValue)))
            .collect(Collectors.toList());
    }

    private boolean isAssignable(String cardValue) {
        return valueIsNotASpecialCard(cardValue) && valueIsAssignableToEstimateField(cardValue);
    }

    private boolean valueIsAssignableToEstimateField(String cardValue) {
        // TODO can the cardValue be persisted into the configured estimation field?
        return true;
    }

    private boolean valueIsNotASpecialCard(String cardValue) {
        return !(Card.COFFEE_BREAK.getValue().equals(cardValue) || Card.QUESTION_MARK.getValue().equals(cardValue));
    }

}
