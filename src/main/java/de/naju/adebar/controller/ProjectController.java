package de.naju.adebar.controller;

import de.naju.adebar.app.human.DataProcessor;
import de.naju.adebar.controller.forms.chapter.ProjectForm;
import de.naju.adebar.controller.forms.events.EventForm;
import de.naju.adebar.model.chapter.*;
import de.naju.adebar.model.human.Activist;
import de.naju.adebar.model.human.HumanManager;
import de.naju.adebar.model.human.Person;
import de.naju.adebar.util.conversion.PersonConverter;
import de.naju.adebar.util.conversion.chapter.ProjectFormDataExtractor;
import de.naju.adebar.util.conversion.chapter.ProjectToProjectFormConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;

/**
 * Project related controller mappings
 * @author Rico Bergmann
 * @see Project
 */
@Controller
public class ProjectController {
    private final static String EMAIL_DELIMITER = ";";

    private ProjectManager projectManager;
    private HumanManager humanManager;
    private LocalGroupManager localGroupManager;
    private PersonConverter personConverter;
    private DataProcessor humanDataProcessor;
    private ProjectFormDataExtractor dataExtractor;
    private ProjectToProjectFormConverter formConverter;

    @Autowired
    public ProjectController(ProjectManager projectManager, HumanManager humanManager, LocalGroupManager localGroupManager, PersonConverter personConverter, DataProcessor humanDataProcessor, ProjectFormDataExtractor dataExtractor, ProjectToProjectFormConverter formConverter) {
        Object[] params = {projectManager, humanManager, localGroupManager, personConverter, dataExtractor, formConverter};
        Assert.noNullElements(params, "At least one parameter was null: " + Arrays.toString(params));
        this.projectManager = projectManager;
        this.humanManager = humanManager;
        this.localGroupManager = localGroupManager;
        this.personConverter = personConverter;
        this.humanDataProcessor = humanDataProcessor;
        this.dataExtractor = dataExtractor;
        this.formConverter = formConverter;
    }

    /**
     * Detail view for a project
     * @param projectId the id of the project to display
     * @param model model from which the displayed data should be taken
     * @return the project's detail view
     */
    @RequestMapping("/projects/{pid}")
    public String showProjectDetails(@PathVariable("pid") long projectId, Model model) {
        Project project = projectManager.findProject(projectId).orElseThrow(IllegalArgumentException::new);
        Person personInCharge =  project.getPersonInCharge() != null ? personConverter.convertActivist(project.getPersonInCharge()) : null;
        Iterable<Person> contributors = personConverter.convertActivists(project.getContributors());

        model.addAttribute("project", project);
        model.addAttribute("personInCharge", personInCharge);
        model.addAttribute("contributors", contributors);
        model.addAttribute("contributorEmails", humanDataProcessor.extractEmailAddressesAsString(contributors, EMAIL_DELIMITER));

        model.addAttribute("addEventForm", new EventForm());
        model.addAttribute("projectForm", formConverter.convertToProjectForm(project));

        return "projectDetails";
    }

    /**
     * Adds a new project to the database
     * @param projectForm the submitted data about the project to add
     * @param redirAttr attributes for the view that should be used after redirection
     * @return the project's detail view
     */
    @RequestMapping("/projects/add")
    public String addProject(@ModelAttribute("projectForm") ProjectForm projectForm, RedirectAttributes redirAttr) {
        LocalGroup localGroup = localGroupManager.findLocalGroup(projectForm.getLocalGroupId()).orElseThrow(IllegalArgumentException::new);

        Project project = localGroupManager.addProjectToLocalGroup(localGroup, dataExtractor.extractProject(projectForm));

        redirAttr.addFlashAttribute("projectAdded", true);
        return "redirect:/projects/" + project.getId();
    }

    /**
     * Updates the information about a project
     * @param projectId the id of the project to update
     * @param projectForm the submitted new project data
     * @param redirAttr attributes for the view that should be used after redirection
     * @return the project's detail view
     */
    @RequestMapping("/projects/{pid}/edit")
    public String editProject(@PathVariable("pid") long projectId, @ModelAttribute("projectForm") ProjectForm projectForm, RedirectAttributes redirAttr) {
        Project project = dataExtractor.extractProject(projectForm);

        projectManager.updateProject(projectId, project);

        redirAttr.addFlashAttribute("projectUpdated", true);
        return "redirect:/projects/" + projectId;
    }

    /**
     * Adds a contributor to a local group
     * @param projectId the id of the project to update
     * @param personId the id of the activist to add
     * @param redirAttr attributes for the view that should be used after redirection
     * @return the project's detail view
     */
    @RequestMapping("/projects/{pid}/contributors/add")
    public String addContributor(@PathVariable("pid") long projectId, @RequestParam("person-id") String personId, RedirectAttributes redirAttr) {
        Project project = projectManager.findProject(projectId).orElseThrow(IllegalArgumentException::new);
        Person person = humanManager.findPerson(personId).orElseThrow(IllegalArgumentException::new);
        Activist activist = humanManager.findActivist(person);

        try {
            project.addContributor(activist);
            projectManager.updateProject(projectId, project);
            redirAttr.addFlashAttribute("contributorAdded", true);
        } catch (ExistingContributorException e) {
            redirAttr.addFlashAttribute("existingContributor", true);
        }

        return "redirect:/projects/" + projectId;
    }

    /**
     * Removes a contributor from a project
     * @param projectId the id of the project to update
     * @param personId the id of the activist to remove
     * @param redirAttr attributes for the view that should be used after redirection
     * @return the project's detail view
     */
    @RequestMapping("/projects/{pid}/contributors/remove")
    public String removeContributor(@PathVariable("pid") long projectId, @RequestParam("person-id") String personId, RedirectAttributes redirAttr) {
        Project project = projectManager.findProject(projectId).orElseThrow(IllegalArgumentException::new);
        Person person = humanManager.findPerson(personId).orElseThrow(IllegalArgumentException::new);
        Activist activist = humanManager.findActivist(person);

        project.removeContributor(activist);
        projectManager.updateProject(projectId, project);

        redirAttr.addFlashAttribute("contributorRemoved", true);
        return "redirect:/projects/" + projectId;
    }

}
