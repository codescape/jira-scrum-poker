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

    public static final String MISSING_LICENSE = "MISSING";

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
            return license.getError().isDefined() ? license.getError().get().name() : null;
        }
        return MISSING_LICENSE;
    }

}
