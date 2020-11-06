package de.codescape.jira.plugins.scrumpoker.service;

/**
 * Service to be used to evaluate the Scrum Poker license and check whether it is valid or not.
 */
public interface ScrumPokerLicenseService {

    /**
     * Returns whether there exists a valid Scrum Poker license.
     *
     * @return <code>true</code> if valid license exists, otherwise <code>false</code>
     */
    boolean hasValidLicense();

    /**
     * Returns the error in case of an invalid license.
     *
     * @return license error or <code>null</code>
     */
    String getLicenseError();

}
