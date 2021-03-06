package de.codescape.jira.plugins.scrumpoker.action;

import com.atlassian.jira.security.xsrf.RequiresXsrfCheck;
import de.codescape.jira.plugins.scrumpoker.ScrumPokerConstants;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerError;
import de.codescape.jira.plugins.scrumpoker.service.ErrorLogService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Error Log page to display and empty the error log.
 */
public class ScrumPokerErrorLogAction extends AbstractScrumPokerAction implements ProvidesDocumentationLink {

    private static final long serialVersionUID = 1L;

    /**
     * Names of all parameters used on the global configuration page.
     */
    static final class Parameters {

        static final String ACTION = "action";

    }

    @Autowired
    public ScrumPokerErrorLogAction(ErrorLogService errorLogService) {
        super(errorLogService);
    }

    @Override
    public String doDefault() {
        return SUCCESS;
    }

    @Override
    @RequiresXsrfCheck
    protected String doExecute() {
        String action = getParameter(Parameters.ACTION);
        if (action != null && action.equals("empty")) {
            errorLogService.emptyErrorLog();
        }
        return SUCCESS;
    }

    @Override
    public String getDocumentationUrl() {
        return ScrumPokerConstants.ERROR_LOG_DOCUMENTATION;
    }

    /**
     * Returns the list of all logged errors.
     */
    public List<ScrumPokerError> getErrorList() {
        return errorLogService.listAll();
    }

}
