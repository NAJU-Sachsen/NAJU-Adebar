package de.naju.adebar.services.conversion.chapter;

import de.naju.adebar.model.chapter.Project;
import de.naju.adebar.web.validation.chapters.ProjectForm;
import org.springframework.stereotype.Service;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Service to convert a {@link Project} to corresponding {@link ProjectForm} objects
 * 
 * @author Rico Bergmann
 */
@Service
public class ProjectToProjectFormConverter {
  private DateTimeFormatter dateTimeFormatter;

  public ProjectToProjectFormConverter() {
    dateTimeFormatter = DateTimeFormatter.ofPattern(ProjectForm.DATE_FORMAT, Locale.GERMAN);
  }

  /**
   * Performs the conversion
   * 
   * @param project the project to convert
   * @return the created form
   */
  public ProjectForm convertToProjectForm(Project project) {
    String start = project.hasStartTime() ? project.getStartTime().format(dateTimeFormatter) : null;
    String end = project.hasEndTime() ? project.getEndTime().format(dateTimeFormatter) : null;
    String personInCharge =
        project.hasPersonInCharge() ? project.getPersonInCharge().getId().toString() : null;

    return new ProjectForm(project.getName(), start, end, personInCharge, project.getId());
  }

}
