package de.codescape.jira.plugins.scrumpoker.action;

import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerError;
import de.codescape.jira.plugins.scrumpoker.service.ScrumPokerErrorService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Error Log page to display and empty the error log.
 */
public class ShowErrorLogAction extends AbstractScrumPokerAction {

    private static final long serialVersionUID = 1L;

    /**
     * Names of all parameters used on the global configuration page.
     */
    static final class Parameters {

        static final String ACTION = "action";

    }

    private final ScrumPokerErrorService scrumPokerErrorService;

    @Autowired
    public ShowErrorLogAction(ScrumPokerErrorService scrumPokerErrorService) {
        this.scrumPokerErrorService = scrumPokerErrorService;
    }

    @Override
    protected String doExecute() {
        String action = getParameter(Parameters.ACTION);
        if (action != null && action.equals("empty")) {
            scrumPokerErrorService.emptyErrorLog();
        }

        return SUCCESS;
    }

    /**
     * Returns the list of all logged errors.
     */
    public List<ScrumPokerError> getErrorList() {
        return scrumPokerErrorService.listAll();
    }

}
