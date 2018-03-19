package de.naju.adebar.services.conversion.chapter;

import de.naju.adebar.model.chapter.LocalGroup;
import de.naju.adebar.model.chapter.Project;
import de.naju.adebar.model.chapter.ReadOnlyLocalGroupRepository;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.model.persons.PersonManager;
import de.naju.adebar.web.validation.chapters.ProjectForm;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Service to extract the necessary data from an 'add project group' form
 *
 * @author Rico Bergmann
 */
@Service
public class ProjectFormDataExtractor {

  private DateTimeFormatter dateTimeFormatter;
  private PersonManager personManager;
  private ReadOnlyLocalGroupRepository localGroupRepo;

  @Autowired
  public ProjectFormDataExtractor(PersonManager personManager,
      ReadOnlyLocalGroupRepository localGroupRepo) {
    Object[] params = {personManager, localGroupRepo};
    Assert.noNullElements(params, "At least one parameter was null: " + Arrays.toString(params));

    this.dateTimeFormatter = DateTimeFormatter.ofPattern(ProjectForm.DATE_FORMAT, Locale.GERMAN);
    this.personManager = personManager;
    this.localGroupRepo = localGroupRepo;
  }

  /**
   * @param projectForm form containing the data to extract
   * @return the {@link Project} object described by the form
   */
  public Project extractProject(ProjectForm projectForm) {
    LocalGroup localGroup = localGroupRepo.findById(projectForm.getLocalGroupId()).orElseThrow(
        () -> new IllegalArgumentException(
            "No local group with ID " + projectForm.getLocalGroupId()));

    Project project = new Project(projectForm.getName(), localGroup);

    if (projectForm.hasPersonInCharge()) {
      Person personInCharge = personManager.findPerson(projectForm.getPersonInCharge())
          .orElseThrow(() -> new IllegalStateException(
              "Project form does not specify a valid ID for the person in charge"));
      project.addContributor(personInCharge);
      project.setPersonInCharge(personInCharge);
    }
    if (projectForm.hasStart()) {
      project.setStartTime(LocalDate.parse(projectForm.getStart(), dateTimeFormatter));
    }
    if (projectForm.hasEnd()) {
      project.setEndTime(LocalDate.parse(projectForm.getEnd(), dateTimeFormatter));
    }

    return project;
  }

}
