package de.naju.adebar.web.controller;

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import de.naju.adebar.app.chapter.LocalGroupManager;
import de.naju.adebar.app.chapter.ProjectManager;
import de.naju.adebar.app.persons.HumanDataProcessor;
import de.naju.adebar.model.chapter.ExistingContributorException;
import de.naju.adebar.model.chapter.LocalGroup;
import de.naju.adebar.model.chapter.Project;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.model.persons.PersonManager;
import de.naju.adebar.services.conversion.chapter.ProjectFormDataExtractor;
import de.naju.adebar.services.conversion.chapter.ProjectToProjectFormConverter;
import de.naju.adebar.web.validation.chapters.ProjectForm;
import de.naju.adebar.web.validation.events.EventForm;

/**
 * Project related controller mappings
 *
 * @author Rico Bergmann
 * @see Project
 */
@Controller
public class ProjectController {

  private static final String EMAIL_DELIMITER = ";";
  private static final String REDIRECT_PROJECTS = "redirect:/projects/";

  private final ProjectManager projectManager;
  private final PersonManager personManager;
  private final LocalGroupManager localGroupManager;
  private final HumanDataProcessor humanDataProcessor;
  private final ProjectFormDataExtractor dataExtractor;
  private final ProjectToProjectFormConverter formConverter;

  @Autowired
  public ProjectController(ProjectManager projectManager, PersonManager personManager,
      LocalGroupManager localGroupManager, HumanDataProcessor humanDataProcessor,
      ProjectFormDataExtractor dataExtractor, ProjectToProjectFormConverter formConverter) {
    Object[] params =
        {projectManager, personManager, localGroupManager, dataExtractor, formConverter};
    Assert.noNullElements(params, "At least one parameter was null: " + Arrays.toString(params));
    this.projectManager = projectManager;
    this.personManager = personManager;
    this.localGroupManager = localGroupManager;
    this.humanDataProcessor = humanDataProcessor;
    this.dataExtractor = dataExtractor;
    this.formConverter = formConverter;
  }

  /**
   * Detail view for a project
   *
   * @param projectId the id of the project to display
   * @param model model from which the displayed data should be taken
   * @return the project's detail view
   */
  @RequestMapping("/projects/{pid}")
  public String showProjectDetails(@PathVariable("pid") long projectId, Model model) {
    Project project =
        projectManager.findProject(projectId).orElseThrow(IllegalArgumentException::new);
    Iterable<Person> contributors = project.getContributors();

    model.addAttribute("project", project);
    model.addAttribute("contributorEmails",
        humanDataProcessor.extractEmailAddressesAsString(contributors, EMAIL_DELIMITER));

    model.addAttribute("addEventForm", new EventForm());
    model.addAttribute("projectForm", formConverter.convertToProjectForm(project));

    return "projectDetails";
  }

  /**
   * Adds a new project to the database
   *
   * @param projectForm the submitted data about the project to add
   * @param redirAttr attributes for the view that should be used after redirection
   * @return the project's detail view
   */
  @RequestMapping("/projects/add")
  public String addProject(@ModelAttribute("projectForm") ProjectForm projectForm,
      RedirectAttributes redirAttr) {
    LocalGroup localGroup = localGroupManager.findLocalGroup(projectForm.getLocalGroupId())
        .orElseThrow(IllegalArgumentException::new);

    Project project = localGroupManager.addProjectToLocalGroup(localGroup,
        dataExtractor.extractProject(projectForm));

    redirAttr.addFlashAttribute("projectAdded", true);
    return REDIRECT_PROJECTS + project.getId();
  }

  /**
   * Updates the information about a project
   *
   * @param projectId the id of the project to update
   * @param projectForm the submitted new project data
   * @param redirAttr attributes for the view that should be used after redirection
   * @return the project's detail view
   */
  @RequestMapping("/projects/{pid}/edit")
  public String editProject(@PathVariable("pid") long projectId,
      @ModelAttribute("projectForm") ProjectForm projectForm, RedirectAttributes redirAttr) {
    Project project = dataExtractor.extractProject(projectForm);

    projectManager.updateProject(projectId, project);

    redirAttr.addFlashAttribute("projectUpdated", true);
    return REDIRECT_PROJECTS + projectId;
  }

  /**
   * Adds a contributor to a local group
   *
   * @param projectId the id of the project to update
   * @param personId the id of the activist to add
   * @param redirAttr attributes for the view that should be used after redirection
   * @return the project's detail view
   */
  @RequestMapping("/projects/{pid}/contributors/add")
  public String addContributor(@PathVariable("pid") long projectId,
      @RequestParam("person-id") String personId, RedirectAttributes redirAttr) {
    Project project =
        projectManager.findProject(projectId).orElseThrow(IllegalArgumentException::new);
    Person activist = personManager.findPerson(personId).orElseThrow(IllegalArgumentException::new);

    try {
      project.addContributor(activist);
      projectManager.updateProject(projectId, project);
      redirAttr.addFlashAttribute("contributorAdded", true);
    } catch (ExistingContributorException e) {
      redirAttr.addFlashAttribute("existingContributor", true);
    }

    return REDIRECT_PROJECTS + projectId;
  }

  /**
   * Removes a contributor from a project
   *
   * @param projectId the id of the project to update
   * @param personId the id of the activist to remove
   * @param redirAttr attributes for the view that should be used after redirection
   * @return the project's detail view
   */
  @RequestMapping("/projects/{pid}/contributors/remove")
  public String removeContributor(@PathVariable("pid") long projectId,
      @RequestParam("person-id") String personId, RedirectAttributes redirAttr) {
    Project project =
        projectManager.findProject(projectId).orElseThrow(IllegalArgumentException::new);
    Person activist = personManager.findPerson(personId).orElseThrow(IllegalArgumentException::new);

    project.removeContributor(activist);
    projectManager.updateProject(projectId, project);

    redirAttr.addFlashAttribute("contributorRemoved", true);
    return REDIRECT_PROJECTS + projectId;
  }

}
