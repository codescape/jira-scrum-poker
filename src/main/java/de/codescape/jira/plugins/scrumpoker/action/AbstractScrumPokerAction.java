package de.codescape.jira.plugins.scrumpoker.action;

import com.atlassian.jira.web.action.JiraWebActionSupport;

/**
 * Base class to be used when normally extending {@link JiraWebActionSupport}.
 * <p>
 * The simple name of the concrete class must be unique across all JIRA add-ons. That is, if one add-on has an action
 * class MyEditAction then no other add-on should have an action class MyEditAction. It is recommended that each add-on
 * use a prefix to make their action classes unique.
 * <p>
 * Source: https://developer.atlassian.com/server/jira/platform/webwork/
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
