package de.naju.adebar.app.chapter;

import java.util.Optional;
import org.springframework.stereotype.Service;
import de.naju.adebar.model.chapter.LocalGroup;
import de.naju.adebar.model.chapter.Project;
import de.naju.adebar.model.chapter.ReadOnlyProjectRepository;

/**
 * Service to take care of {@link Project Projects}
 * 
 * @author Rico Bergmann
 * @see Project
 */
@Service
public interface ProjectManager {

  /**
   * Saves the given project. It may or may not be saved already
   * 
   * @param project the project to save
   * @return the saved project. As its internal state may differ after the save, this instance
   *         should be used for future operations
   */
  Project saveProject(Project project);

  /**
   * Creates a new project
   * 
   * @param name the project's name
   * @param localGroup the local group the project belongs to
   * @return the saved project. As its internal state may differ after the save, this instance
   *         should be used for future operations
   */
  Project createProject(String name, LocalGroup localGroup);

  /**
   * Changes the state of a saved project. This will overwrite the complete state of the project to
   * update
   * 
   * @param projectId the identifier of the project to update
   * @param projectData the new data of the project
   * @return the updated project
   */
  Project updateProject(long projectId, Project projectData);

  /**
   * Changes the state of a saved project. In difference to {@link #updateProject(long, Project)}
   * this does only modify "static" information, i.e. name, start/end date and person in charge.
   * However this does <strong>not</strong> change the local group the project is associated to, as
   * this method is primarily designed with the use case of simply changing a project's name or
   * period in mind.
   * 
   * @param projectId the identifier of the project to update
   * @param projectData the project data containing the "static" data to adopt
   * @return the updated project
   */
  Project adoptProjectData(long projectId, Project projectData);

  /**
   * Queries for a specific project
   * 
   * @param id the project's id
   * @return an optional containing the project if it exists, otherwise the optional is empty
   */
  Optional<Project> findProject(long id);

  /**
   * Queries for a specific project
   * 
   * @param name the project's name
   * @param localGroup the local group the project is associated to
   * @return an optional containing the project if such a project exists, otherwise the optional is
   *         empty
   */
  Optional<Project> findProject(String name, LocalGroup localGroup);

  /**
   * Provides access to the underlying data
   * 
   * @return a read only instance of the database
   */
  ReadOnlyProjectRepository repository();
}
