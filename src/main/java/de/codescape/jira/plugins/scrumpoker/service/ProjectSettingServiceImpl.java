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
 * Implementation of {@link ProjectSettingService} using Active Objects as persistence model.
 */
@Component
public class ProjectSettingServiceImpl implements ProjectSettingService {

    private final ActiveObjects activeObjects;

    @Autowired
    public ProjectSettingServiceImpl(@ComponentImport ActiveObjects activeObjects) {
        this.activeObjects = activeObjects;
    }

    @Override
    public boolean loadScrumPokerEnabled(Long projectId) {
        ScrumPokerProject scrumPokerProject = findById(projectId);
        return scrumPokerProject != null && scrumPokerProject.isScrumPokerEnabled();
    }

    @Override
    public void persistScrumPokerEnabled(Long projectId, boolean scrumPokerEnabled) {
        ScrumPokerProject scrumPokerProject = findOrCreateByProjectId(projectId);
        scrumPokerProject.setScrumPokerEnabled(scrumPokerEnabled);
        scrumPokerProject.save();
    }

    @Override
    public List<ScrumPokerProject> loadAll() {
        return Arrays.stream(activeObjects.find(ScrumPokerProject.class)).collect(Collectors.toList());
    }

    private ScrumPokerProject findById(Long projectId) {
        ScrumPokerProject[] scrumPokerProjects = activeObjects.find(ScrumPokerProject.class,
            Query.select().where("PROJECT_ID = ?", projectId).limit(1));
        return (scrumPokerProjects.length == 1) ? scrumPokerProjects[0] : null;
    }

    private ScrumPokerProject findOrCreateByProjectId(Long projectId) {
        ScrumPokerProject scrumPokerProject = findById(projectId);
        if (scrumPokerProject == null) {
            scrumPokerProject = activeObjects.create(ScrumPokerProject.class,
                new DBParam("PROJECT_ID", projectId));
        }
        return scrumPokerProject;
    }

}
