package de.codescape.jira.plugins.scrumpoker.service;

import de.codescape.jira.plugins.scrumpoker.model.GlobalSettings;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

public class ScrumPokerCardServiceImplTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private ScrumPokerSettingService scrumPokerSettingService;

    @InjectMocks
    private ScrumPokerCardServiceImpl service;

    @Mock
    private GlobalSettings globalSettings;

    @Test
    public void shouldIgnoreWhitespaceOnElementsInCardSet() {
        expectResultFromActiveObjects(" 1, 2 , 3, coffee   , ,");
        List<String> cardSet = service.getCardSet();
        assertThat(cardSet.size(), is(4));
        assertThat(cardSet, Matchers.contains("1", "2", "3", "coffee"));
    }

    @Test
    public void shouldReturnDefaultSimplifiedFibonacciCardSet() {
        expectResultFromActiveObjects("question,coffee,0,1,2,3,5,8,13,20,40,100");
        List<String> cardSet = service.getCardSet();
        assertThat(cardSet.size(), is(12));
        assertThat(cardSet, Matchers.contains("question", "coffee", "0", "1", "2", "3", "5", "8", "13", "20", "40", "100"));
    }

    private void expectResultFromActiveObjects(String expectedString) {
        when(scrumPokerSettingService.load()).thenReturn(globalSettings);
        when(globalSettings.getCardSet()).thenReturn(expectedString);
    }

}
