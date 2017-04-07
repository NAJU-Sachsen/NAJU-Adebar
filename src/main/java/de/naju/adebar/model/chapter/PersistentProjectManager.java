package de.naju.adebar.model.chapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Optional;

/**
 * A {@link ProjectManager} that persists its data in a database
 * @author Rico Bergmann
 */
@Service
public class PersistentProjectManager  implements ProjectManager {
    private ProjectRepository projectRepo;
    private ReadOnlyProjectRepository roRepo;
    private LocalGroupManager localGroupManager;

    @Autowired
    public PersistentProjectManager(ProjectRepository projectRepo, ReadOnlyProjectRepository roRepo, LocalGroupManager localGroupManager) {
        Object[] params = {projectRepo, roRepo, localGroupManager};
        Assert.notNull(params, "At least one parameter was null: " + Arrays.toString(params));
        this.projectRepo = projectRepo;
        this.roRepo = roRepo;
        this.localGroupManager = localGroupManager;
    }

    @Override
    public Project saveProject(Project project) {
        return projectRepo.save(project);
    }

    @Override
    public Project createProject(String name, LocalGroup localGroup) {
        Project project = new Project(name, localGroup);
        localGroup.addProject(project);
        localGroupManager.updateLocalGroup(localGroup.getId(), localGroup);
        return projectRepo.save(project);
    }

    @Override
    public Project updateProject(long projectId, Project projectData) {
        projectData.setId(projectId);
        return projectRepo.save(projectData);
    }

    @Override
    public Project adoptProjectData(long projectId, Project projectData) {
        Project project = findProject(projectId).orElseThrow(() -> new IllegalArgumentException("No project found for ID " + projectId));
        project.setName(projectData.getName());
        project.setStartTime(projectData.getStartTime());
        project.setEndTime(projectData.getEndTime());
        project.setPersonInCharge(projectData.getPersonInCharge());
        return updateProject(projectId, project);
    }

    @Override
    public Optional<Project> findProject(long id) {
        Project project = projectRepo.findOne(id);
        return project != null ? Optional.of(project) : Optional.empty();
    }

    @Override
    public Optional<Project> findProject(String name, LocalGroup localGroup) {
        Project project = projectRepo.findByNameAndLocalGroup(name, localGroup);
        return project != null ? Optional.of(project) : Optional.empty();
    }

    @Override
    public ReadOnlyProjectRepository repository() {
        return roRepo;
    }
}
