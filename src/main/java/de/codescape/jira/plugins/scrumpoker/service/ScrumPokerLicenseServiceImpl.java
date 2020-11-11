package de.codescape.jira.plugins.scrumpoker.service;

import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.upm.api.license.PluginLicenseManager;
import com.atlassian.upm.api.license.entity.PluginLicense;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Implementation of {@link ScrumPokerLicenseService}.
 */
@Component
public class ScrumPokerLicenseServiceImpl implements ScrumPokerLicenseService {

    static final String ERROR_MESSAGE_PREFIX = "scrumpoker.error.license.reason.";
    static final String MISSING_LICENSE = ERROR_MESSAGE_PREFIX + "missing";

    private final PluginLicenseManager pluginLicenseManager;

    @Autowired
    public ScrumPokerLicenseServiceImpl(@ComponentImport PluginLicenseManager pluginLicenseManager) {
        this.pluginLicenseManager = pluginLicenseManager;
    }

    @Override
    public boolean hasValidLicense() {
        if (pluginLicenseManager.getLicense().isDefined()) {
            PluginLicense license = pluginLicenseManager.getLicense().get();
            return !license.getError().isDefined();
        } else {
            return false;
        }
    }

    @Override
    public String getLicenseError() {
        if (pluginLicenseManager.getLicense().isDefined()) {
            PluginLicense license = pluginLicenseManager.getLicense().get();
            return license.getError().isDefined() ? errorMessageCodeFor(license.getError().get().name()) : null;
        }
        return MISSING_LICENSE;
    }

    private String errorMessageCodeFor(String errorCode) {
        return ERROR_MESSAGE_PREFIX + errorCode.toLowerCase();
    }

}
