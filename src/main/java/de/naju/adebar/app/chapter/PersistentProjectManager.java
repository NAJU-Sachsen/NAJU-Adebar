package de.naju.adebar.app.chapter;

import de.naju.adebar.app.IdUpdateFailedException;
import de.naju.adebar.model.chapter.LocalGroup;
import de.naju.adebar.model.chapter.Project;
import de.naju.adebar.model.chapter.ProjectRepository;
import de.naju.adebar.model.chapter.ReadOnlyProjectRepository;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * A {@link ProjectManager} that persists its data in a database
 *
 * @author Rico Bergmann
 */
@Service
public class PersistentProjectManager implements ProjectManager {

  private ProjectRepository projectRepo;
  private ReadOnlyProjectRepository roRepo;
  private LocalGroupManager localGroupManager;

  @Autowired
  public PersistentProjectManager(ProjectRepository projectRepo, ReadOnlyProjectRepository roRepo,
      LocalGroupManager localGroupManager) {
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
    setProjectId(projectData, projectId);
    return projectRepo.save(projectData);
  }

  @Override
  public Project adoptProjectData(long projectId, Project projectData) {
    Project project = findProject(projectId)
        .orElseThrow(() -> new IllegalArgumentException("No project found for ID " + projectId));
    project.setName(projectData.getName());
    project.setStartTime(projectData.getStartTime());
    project.setEndTime(projectData.getEndTime());
    project.setPersonInCharge(projectData.getPersonInCharge());
    return updateProject(projectId, project);
  }

  @Override
  public Optional<Project> findProject(long id) {
    return projectRepo.findById(id);
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

  /**
   * Updates a project's ID. To be used extremely cautiously.
   *
   * @param project the project to update
   * @param id the new ID
   */
  protected void setProjectId(Project project, Long id) {
    try {
      Method changeId = Project.class.getDeclaredMethod("setId", long.class);
      changeId.setAccessible(true);
      changeId.invoke(project, id);
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      throw new IdUpdateFailedException("Error during invocation of reflection", e);
    }
  }
}
