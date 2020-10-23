package de.codescape.jira.plugins.scrumpoker.service;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import de.codescape.jira.plugins.scrumpoker.ao.ScrumPokerProject;
import net.java.ao.DBParam;
import net.java.ao.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of {@link ProjectSettingsService} using Active Objects as persistence model.
 */
@Component
public class ProjectSettingsServiceImpl implements ProjectSettingsService {

    private final ActiveObjects activeObjects;

    @Autowired
    public ProjectSettingsServiceImpl(@ComponentImport ActiveObjects activeObjects) {
        this.activeObjects = activeObjects;
    }

    @Override
    public List<ScrumPokerProject> loadAll() {
        return Arrays.stream(activeObjects.find(ScrumPokerProject.class)).collect(Collectors.toList());
    }

    @Override
    public void persistSettings(Long projectId, boolean activateScrumPoker, String estimateField) {
        ScrumPokerProject scrumPokerProject = findOrCreateByProjectId(projectId);
        scrumPokerProject.setScrumPokerEnabled(activateScrumPoker);
        scrumPokerProject.setEstimateField(estimateField);
        scrumPokerProject.save();
    }

    @Override
    public ScrumPokerProject loadSettings(Long projectId) {
        return findOrCreateByProjectId(projectId);
    }

    private ScrumPokerProject findOrCreateByProjectId(Long projectId) {
        ScrumPokerProject[] scrumPokerProjects = activeObjects.find(ScrumPokerProject.class,
            Query.select().where("PROJECT_ID = ?", projectId).limit(1));
        ScrumPokerProject scrumPokerProject = (scrumPokerProjects.length == 1) ? scrumPokerProjects[0] : null;
        if (scrumPokerProject == null) {
            scrumPokerProject = activeObjects.create(ScrumPokerProject.class,
                new DBParam("PROJECT_ID", projectId));
        }
        return scrumPokerProject;
    }

}
