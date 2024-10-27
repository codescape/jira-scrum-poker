package de.codescape.jira.plugins.scrumpoker.action;

import com.atlassian.jira.security.request.RequestMethod;
import com.atlassian.jira.security.request.SupportedMethods;
import de.codescape.jira.plugins.scrumpoker.ScrumPokerConstants;
import de.codescape.jira.plugins.scrumpoker.service.ErrorLogService;

import javax.inject.Inject;

/**
 * Getting Started page for the Scrum Poker app.
 */
public class ScrumPokerGettingStartedAction extends AbstractScrumPokerAction implements ProvidesDocumentationLink {

    @Inject
    public ScrumPokerGettingStartedAction(ErrorLogService errorLogService) {
        super(errorLogService);
    }

    @Override
    @SupportedMethods({RequestMethod.GET})
    public String doDefault() {
        return SUCCESS;
    }

    @Override
    public String getDocumentationUrl() {
        return ScrumPokerConstants.GETTING_STARTED_DOCUMENTATION;
    }

    /**
     * Returns the URL to the Service Desk for Scrum Poker for Jira.
     */
    public String getServiceDeskUrl() {
        return ScrumPokerConstants.SERVICE_DESK_URL;
    }

    /**
     * Returns the URL to the Marketplace entry for Scrum Poker for Jira.
     */
    public String getMarketplaceUrl() {
        return ScrumPokerConstants.MARKETPLACE_URL;
    }

}
