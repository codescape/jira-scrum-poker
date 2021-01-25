package de.codescape.jira.plugins.scrumpoker.action;

import de.codescape.jira.plugins.scrumpoker.ScrumPokerConstants;
import de.codescape.jira.plugins.scrumpoker.service.ErrorLogService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Getting Started page for the Scrum Poker app.
 */
public class ScrumPokerGettingStartedAction extends AbstractScrumPokerAction implements ProvidesDocumentationLink {

    @Autowired
    public ScrumPokerGettingStartedAction(ErrorLogService errorLogService) {
        super(errorLogService);
    }

    @Override
    protected String doExecuteInternal() {
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

}
