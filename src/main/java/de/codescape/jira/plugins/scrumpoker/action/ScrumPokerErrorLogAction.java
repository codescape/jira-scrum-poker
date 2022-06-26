package de.codescape.jira.plugins.scrumpoker.action;

import com.atlassian.jira.security.request.RequestMethod;
import com.atlassian.jira.security.request.SupportedMethods;
import com.atlassian.jira.security.xsrf.RequiresXsrfCheck;
import de.codescape.jira.plugins.scrumpoker.ScrumPokerConstants;
import de.codescape.jira.plugins.scrumpoker.model.Error;
import de.codescape.jira.plugins.scrumpoker.service.ErrorLogService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

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
    @SupportedMethods({RequestMethod.GET})
    public String doDefault() {
        return SUCCESS;
    }

    @Override
    @RequiresXsrfCheck
    @SupportedMethods({RequestMethod.POST})
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
    public List<Error> getErrorList() {
        return errorLogService.listAll().stream()
            .map(Error::new)
            .collect(Collectors.toList());
    }

}
