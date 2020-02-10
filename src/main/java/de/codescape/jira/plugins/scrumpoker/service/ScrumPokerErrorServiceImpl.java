package de.codescape.jira.plugins.scrumpoker.service;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.jira.util.BuildUtilsInfo;
import com.atlassian.plugin.Plugin;
import com.atlassian.plugin.PluginAccessor;
import com.atlassian.plugin.PluginInformation;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerError;
import net.java.ao.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static de.codescape.jira.plugins.scrumpoker.ScrumPokerConstants.SCRUM_POKER_PLUGIN_KEY;

/**
 * Implementation of {@link ScrumPokerErrorService} using Active Objects as persistence model.
 */
@Component
public class ScrumPokerErrorServiceImpl implements ScrumPokerErrorService {

    private final ActiveObjects activeObjects;
    private final BuildUtilsInfo buildUtilsInfo;
    private final PluginAccessor pluginAccessor;

    @Autowired
    public ScrumPokerErrorServiceImpl(@ComponentImport ActiveObjects activeObjects,
                                      @ComponentImport BuildUtilsInfo buildUtilsInfo,
                                      @ComponentImport PluginAccessor pluginAccessor) {
        this.activeObjects = activeObjects;
        this.buildUtilsInfo = buildUtilsInfo;
        this.pluginAccessor = pluginAccessor;
    }

    @Override
    public void logError(String errorMessage, Throwable throwable) {
        ScrumPokerError scrumPokerError = activeObjects.create(ScrumPokerError.class);
        scrumPokerError.setErrorTimestamp(new Date());
        scrumPokerError.setStacktrace(stacktraceAsString(throwable));
        scrumPokerError.setErrorMessage(errorMessage);
        scrumPokerError.setJiraVersion(buildUtilsInfo.getVersion());
        scrumPokerError.setScrumPokerVersion(getScrumPokerVersion());
        scrumPokerError.save();
    }

    @Override
    public List<ScrumPokerError> listAll() {
        return Arrays.asList(activeObjects.find(ScrumPokerError.class, Query.select()
            .order("ERROR_TIMESTAMP DESC, ID DESC")));
    }

    @Override
    public void emptyErrorLog() {
        ScrumPokerError[] scrumPokerErrors = activeObjects.find(ScrumPokerError.class);
        Arrays.stream(scrumPokerErrors).forEach(activeObjects::delete);
    }

    private String stacktraceAsString(Throwable throwable) {
        if (throwable == null) {
            return null;
        }
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        throwable.printStackTrace(printWriter);
        return stringWriter.toString().trim();
    }

    private String getScrumPokerVersion() {
        Plugin plugin = pluginAccessor.getPlugin(SCRUM_POKER_PLUGIN_KEY);
        if (plugin == null) {
            return null;
        }
        PluginInformation pluginInformation = plugin.getPluginInformation();
        if (pluginInformation == null) {
            return null;
        }
        return pluginInformation.getVersion();
    }

}
