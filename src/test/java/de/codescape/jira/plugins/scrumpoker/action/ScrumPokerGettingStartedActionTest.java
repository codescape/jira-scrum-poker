package de.codescape.jira.plugins.scrumpoker.action;

import com.atlassian.jira.junit.rules.MockitoContainer;
import com.atlassian.jira.junit.rules.MockitoMocksInContainer;
import de.codescape.jira.plugins.scrumpoker.ScrumPokerConstants;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static webwork.action.Action.SUCCESS;

public class ScrumPokerGettingStartedActionTest {

    @Rule
    public MockitoContainer mockitoContainer = MockitoMocksInContainer.rule(this);

    @InjectMocks
    private ScrumPokerGettingStartedAction action;

    /* tests for doExecute() */

    @Test
    public void doExecuteShouldReturnSuccess() {
        assertThat(action.doDefault(), is(equalTo(SUCCESS)));
    }

    /* tests for getDocumentationUrl() */

    @Test
    public void getDocumentationUrlShouldExposeLink() {
        assertThat(action.getDocumentationUrl(), is(equalTo(ScrumPokerConstants.GETTING_STARTED_DOCUMENTATION)));
    }

    /* tests for getServiceDeskUrl() */

    @Test
    public void getServiceDeskUrlShouldExposeLink() {
        assertThat(action.getServiceDeskUrl(), is(equalTo(ScrumPokerConstants.SERVICE_DESK_URL)));
    }

    /* tests for getMarketplaceUrl() */

    @Test
    public void getMarketplaceUrlShouldExposeLink() {
        assertThat(action.getMarketplaceUrl(), is(equalTo(ScrumPokerConstants.MARKETPLACE_URL)));
    }

}
