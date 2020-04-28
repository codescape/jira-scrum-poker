package de.codescape.jira.plugins.scrumpoker.service;

import de.codescape.jira.plugins.scrumpoker.model.Card;
import de.codescape.jira.plugins.scrumpoker.model.GlobalSettings;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.List;

import static de.codescape.jira.plugins.scrumpoker.model.Card.COFFEE_BREAK;
import static de.codescape.jira.plugins.scrumpoker.model.Card.QUESTION_MARK;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

public class CardSetServiceImplTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private GlobalSettingsService globalSettingsService;

    @InjectMocks
    private CardSetServiceImpl service;

    @Mock
    private GlobalSettings globalSettings;

    @Test
    public void shouldIgnoreWhitespaceOnElementsInCardSet() {
        expectResultFromActiveObjects(" 1, 2 , 3, coffee   , ,");
        List<Card> cardSet = service.getCardSet();
        assertThat(cardSet.size(), is(4));
        assertThat(cardSet, Matchers.contains(
            new Card("1", true),
            new Card("2", true),
            new Card("3", true),
            COFFEE_BREAK));
    }

    @Test
    public void shouldReturnDefaultSimplifiedFibonacciCardSet() {
        expectResultFromActiveObjects(GlobalSettings.CARD_SET_DEFAULT);
        List<Card> cardSet = service.getCardSet();
        assertThat(cardSet.size(), is(12));
        assertThat(cardSet, Matchers.contains(
            QUESTION_MARK,
            COFFEE_BREAK,
            new Card("0", true),
            new Card("1", true),
            new Card("2", true),
            new Card("3", true),
            new Card("5", true),
            new Card("8", true),
            new Card("13", true),
            new Card("20", true),
            new Card("40", true),
            new Card("100", true)));
    }

    private void expectResultFromActiveObjects(String expectedString) {
        when(globalSettingsService.load()).thenReturn(globalSettings);
        when(globalSettings.getCardSet()).thenReturn(expectedString);
    }

}
