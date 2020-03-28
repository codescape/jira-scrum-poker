package de.codescape.jira.plugins.scrumpoker.action;

import com.atlassian.jira.web.action.JiraWebActionSupport;

/**
 * Base class to be used when normally extending {@link JiraWebActionSupport}.
 */
abstract class AbstractScrumPokerAction extends JiraWebActionSupport {

    /**
     * Short form for resolving a parameter from the HTTP request. If the parameter is empty return null instead.
     *
     * @param parameterName name of the parameter
     * @return value of the parameter
     */
    String getParameter(String parameterName) {
        String value = getHttpRequest().getParameter(parameterName);
        return value != null && !value.isEmpty() ? value : null;
    }

}
