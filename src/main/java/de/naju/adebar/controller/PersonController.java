package de.naju.adebar.controller;

import java.util.Arrays;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Iterables;

import de.naju.adebar.app.chapter.LocalGroupManager;
import de.naju.adebar.app.human.DataProcessor;
import de.naju.adebar.app.human.PersonManager;
import de.naju.adebar.app.human.filter.PersonFilterBuilder;
import de.naju.adebar.controller.forms.human.AddQualificationForm;
import de.naju.adebar.controller.forms.human.CreateParentForm;
import de.naju.adebar.controller.forms.human.CreatePersonForm;
import de.naju.adebar.controller.forms.human.EditActivistForm;
import de.naju.adebar.controller.forms.human.EditPersonForm;
import de.naju.adebar.controller.forms.human.FilterPersonForm;
import de.naju.adebar.model.chapter.LocalGroup;
import de.naju.adebar.model.human.ImpossibleKinshipRelationException;
import de.naju.adebar.model.human.Person;
import de.naju.adebar.model.human.Qualification;
import de.naju.adebar.model.human.QualificationManager;
import de.naju.adebar.services.conversion.human.ActivistToEditActivistFormConverter;
import de.naju.adebar.services.conversion.human.AddQualificationFormDataExtractor;
import de.naju.adebar.services.conversion.human.CreatePersonFormDataExtractor;
import de.naju.adebar.services.conversion.human.EditActivistFormDataExtractor;
import de.naju.adebar.services.conversion.human.EditPersonFormDataExtractor;
import de.naju.adebar.services.conversion.human.FilterPersonFormFilterExtractor;
import de.naju.adebar.services.conversion.human.PersonToEditPersonFormConverter;

/**
 * Person related controller mappings
 *
 * @author Rico Bergmann
 * @see Person
 */
@Controller
public class PersonController {
    private final static String EMAIL_DELIMITER = ";";

    private PersonManager personManager;
    private QualificationManager qualificationManager;
    private LocalGroupManager localGroupManager;
    private CreatePersonFormDataExtractor createPersonFormDataExtractor;
    private EditPersonFormDataExtractor editPersonFormDataExtractor;
    private EditActivistFormDataExtractor editActivistFormDataExtractor;
    private FilterPersonFormFilterExtractor filterPersonFormFilterExtractor;
    private AddQualificationFormDataExtractor addQualificationFormDataExtractor;
    private DataProcessor dataProcessor;

    @Autowired
    public PersonController(PersonManager personManager, QualificationManager qualificationManager, LocalGroupManager localGroupManager, CreatePersonFormDataExtractor createPersonFormDataExtractor, EditPersonFormDataExtractor editPersonFormDataExtractor, EditActivistFormDataExtractor editActivistFormDataExtractor, FilterPersonFormFilterExtractor filterPersonFormFilterExtractor, AddQualificationFormDataExtractor addQualificationFormDataExtractor, DataProcessor dataProcessor) {
        Object[] params = {personManager, qualificationManager, localGroupManager, localGroupManager, createPersonFormDataExtractor, editPersonFormDataExtractor, editActivistFormDataExtractor, filterPersonFormFilterExtractor, addQualificationFormDataExtractor, dataProcessor};
        Assert.noNullElements(params, "At least one parameter was null: " + Arrays.toString(params));
        this.personManager = personManager;
        this.localGroupManager = localGroupManager;
        this.createPersonFormDataExtractor = createPersonFormDataExtractor;
        this.editPersonFormDataExtractor = editPersonFormDataExtractor;
        this.editActivistFormDataExtractor = editActivistFormDataExtractor;
        this.filterPersonFormFilterExtractor = filterPersonFormFilterExtractor;
        this.qualificationManager = qualificationManager;
        this.addQualificationFormDataExtractor = addQualificationFormDataExtractor;
        this.dataProcessor = dataProcessor;
    }

    /**
     * Displays the person overview
     * @param model model to display the data in
     * @return the persons' overview view
     */
    @RequestMapping("/persons")
    public String showPersonOverview(Model model) {
        model.addAttribute("addPersonForm", new CreatePersonForm());
        model.addAttribute("filterPersonsForm", new FilterPersonForm());
        model.addAttribute("persons", personManager.repository().findFirst25());
        model.addAttribute("qualifications", qualificationManager.repository().findAll());
        return "persons";
    }

    /**
     * Displays the person overview with all persons
     * @param model model to display the data in
     * @return the persons' overview view
     */
    @RequestMapping("/persons/all")
    public String showAllPersons(Model model) {
        model.addAttribute("addPersonForm", new CreatePersonForm());
        model.addAttribute("filterPersonsForm", new FilterPersonForm());
        model.addAttribute("persons", personManager.repository().findAllOrderByLastName());
        model.addAttribute("qualifications", qualificationManager.repository().findAll());
        return "persons";
    }

    /**
     * Adds a new person to the database
     * @param createPersonForm person object created by the model
     * @return the new person's detail view
     */
    @RequestMapping("/persons/add")
    public String addPerson(@ModelAttribute("addPersonFrom") CreatePersonForm createPersonForm) {
        Person person = createPersonFormDataExtractor.extractPerson(createPersonForm);
        personManager.savePerson(person);

        return "redirect:/persons/" + person.getId();
    }

    /**
     * Filters the saved persons
     * @param filterPersonForm filter object created by the model
     * @param model model to display the data in
     * @return the persons' overview consisting of the persons that matched the filter
     */
    @RequestMapping("/persons/filter")
    @Transactional
    public String filterPersons(@ModelAttribute("filterPersonsForm") FilterPersonForm filterPersonForm, Model model) {
        Stream<Person> persons = personManager.repository().streamAll();
        PersonFilterBuilder filterBuilder = new PersonFilterBuilder(persons);
        filterPersonFormFilterExtractor.extractAllFilters(filterPersonForm).forEach(filterBuilder::applyFilter);

        Iterable<Person> matchingPersons = filterBuilder.filter();
        String matchingPersonsEmail = dataProcessor.extractEmailAddressesAsString(matchingPersons, EMAIL_DELIMITER);

        model.addAttribute("filtered", true);
        model.addAttribute("persons", matchingPersons);
        model.addAttribute("emails", matchingPersonsEmail);
        model.addAttribute("qualifications", qualificationManager.repository().findAll());
        model.addAttribute("addPersonForm", new CreatePersonForm());
        model.addAttribute("filterPersonsForm", new FilterPersonForm());
        return "persons";
    }

    /**
     * Detail view for persons
     * @param personId the id of the person to display
     * @param model model to display the data in
     * @return the person's detail view
     */
    @RequestMapping("/persons/{pid}")
    public String showPersonDetails(@PathVariable("pid") String personId, Model model) {

        Person person = personManager.findPerson(personId).orElseThrow(IllegalArgumentException::new);

        model.addAttribute("editPersonForm", new PersonToEditPersonFormConverter().convertToEditPersonForm(person));
        model.addAttribute("addQualificationForm", new AddQualificationForm());
        model.addAttribute("addParentForm", new CreateParentForm());
        model.addAttribute("allChapters", localGroupManager.repository().findAll());

        model.addAttribute("person", person);

        if (person.isActivist()) {
            Iterable<LocalGroup> localGroups = localGroupManager.repository().findByMembersContains(person);
            Iterable<LocalGroup> boards = localGroupManager.findAllLocalGroupsForBoardMember(person);

            model.addAttribute("isActivist", !person.isArchived());
            model.addAttribute("editActivistForm", new ActivistToEditActivistFormConverter(localGroupManager.repository()).convertToEditActivistForm(person));
            model.addAttribute("localGroups", Iterables.isEmpty(localGroups) ? null : localGroups);
            model.addAttribute("boards", Iterables.isEmpty(boards) ? null : boards);
        } else {
            model.addAttribute("isActivist", false);
            model.addAttribute("editActivistForm", new EditActivistForm());
        }

        model.addAttribute("isReferent", person.isReferent());

        model.addAttribute("allQualifications", qualificationManager.repository().findAll());

        return "personDetails";
    }

    /**
     * Updates a person
     * @param personId the id of the person to edit
     * @param personForm person object created by the model
     * @param redirAttr attributes for the view to display some result information
     * @return the person's detail view
     */
    @RequestMapping("/persons/{pid}/edit")
    public String editPerson(@PathVariable("pid") String personId, @ModelAttribute("editPersonForm") EditPersonForm personForm, RedirectAttributes redirAttr) {

        Person person = personManager.findPerson(personId).orElseThrow(IllegalArgumentException::new);

        personManager.updatePerson(person.getId(), editPersonFormDataExtractor.extractPerson(person.getId(), personForm));

        redirAttr.addFlashAttribute("personUpdated", true);
        return "redirect:/persons/" + personId;
    }

    /**
     * Deactivates a person. It will not be listed in selection dialogs any more
     * @param personId the id of the person to remove
     * @param redirAttr attributes for the view to display some result information
     * @return the persons' overview view
     */
    @RequestMapping("/persons/{pid}/delete")
    public String deletePerson(@PathVariable("pid") String personId, RedirectAttributes redirAttr) {
        Person person = personManager.findPerson(personId).orElseThrow(IllegalArgumentException::new);

        try {
            personManager.deactivatePerson(person);
        } catch (IllegalStateException e) {
            redirAttr.addFlashAttribute("deletionError", true);
            return "redirect:/persons/" + personId;
        }


        redirAttr.addFlashAttribute("personDeleted", true);
        return "redirect:/persons/";
    }

    /**
     * Edits the activist data of a person
     * @param personId the id of the person/activist to edit
     * @param activistForm the activists new data
     * @param redirAttr attributes for the view to display some result information
     * @return the person's detail view
     */
    @RequestMapping("/persons/{pid}/edit-activist")
    public String editActivist(@PathVariable("pid") String personId, @ModelAttribute("editActivistForm") EditActivistForm activistForm, RedirectAttributes redirAttr) {

        Person person = personManager.findPerson(personId).orElseThrow(IllegalArgumentException::new);

        if (!person.isActivist()) {
            person.makeActivist();
        }

        person.setActivistProfile(editActivistFormDataExtractor.extractActivistForm(person, activistForm));
        personManager.updatePerson(person.getId(), person);

        localGroupManager.updateLocalGroupMembership(person, localGroupManager.repository().findAll(activistForm.getLocalGroups()));

        return "redirect:/persons/" + personId;
    }

    /**
     * Adds a new qualification to a referent. If the person is not a referent already, it will be turned into one.
     * @param personId the id of the person/referent
     * @param qualificationForm qualification object created by the model
     * @param redirAttr attributes for the view to display some result information
     * @return the person's detail view
     */
    @RequestMapping("/persons/{pid}/add-qualification")
    public String addQualification(@PathVariable("pid") String personId, @ModelAttribute("addQualificationForm") AddQualificationForm qualificationForm, RedirectAttributes redirAttr) {

        Person person = personManager.findPerson(personId).orElseThrow(IllegalArgumentException::new);

        if (!person.isReferent()) {
            person.makeReferent();
        }

        personManager.addQualificationToPerson(person,addQualificationFormDataExtractor.extractQualification(qualificationForm));

        redirAttr.addFlashAttribute("qualificationAdded", true);
        return "redirect:/persons/" + personId;
    }

    /**
     * Removes a qualification from a referent
     * @param personId the id of the person/referent
     * @param qualificationName qualification object created by the model
     * @param redirAttr attributes for the view to display some result information
     * @return the person's detail view
     */
    @RequestMapping("/persons/{pid}/qualifications/remove")
    public String removeQualification(@PathVariable("pid") String personId, @RequestParam("name") String qualificationName, RedirectAttributes redirAttr) {

        Person person = personManager.findPerson(personId).orElseThrow(IllegalArgumentException::new);

        Qualification qualification = qualificationManager.findQualification(qualificationName).orElseThrow(IllegalArgumentException::new);

        person.getReferentProfile().removeQualification(qualification);
        personManager.updatePerson(person.getId(), person);

        return "redirect:/persons/" + personId;
    }

    /**
     * Registers a person as parent
     * @param personId the child
     * @param parentId the parent
     * @return the child's detail view
     */
    @RequestMapping("/persons/{cid}/parents/add/existing")
    public String addParent(@PathVariable("cid") String personId, @RequestParam("person-id") String parentId, RedirectAttributes redirAttr) {
    	Person person = personManager.findPerson(personId).orElseThrow(IllegalArgumentException::new);
    	Person parent = personManager.findPerson(parentId).orElseThrow(IllegalArgumentException::new);

    	try {
    		person.connectParentProfile(parent);
    		personManager.updatePerson(person.getId(), person);
    	} catch (ImpossibleKinshipRelationException e) {
    		redirAttr.addFlashAttribute("impossibleKinship", true);
    	}

    	return "redirect:/persons/" + personId;
    }

    /**
     * Creates a new parent for a person
     * @param personId the child
     * @param createParentForm the form containing the parent's data
     * @return the child's detail view
     */
    @RequestMapping("/persons/{cid}/parents/add/new")
    public String createParent(@PathVariable("cid") String personId, @ModelAttribute("addPersonFrom") CreateParentForm createParentForm, RedirectAttributes redirAttr) {
    	Person person = personManager.findPerson(personId).orElseThrow(IllegalArgumentException::new);
    	Person parent = createPersonFormDataExtractor.extractParent(person, createParentForm);

    	personManager.savePerson(parent);
    	person.connectParentProfile(parent);
		personManager.updatePerson(person.getId(), person);

    	return "redirect:/persons/" + personId;
    }

    /**
     * Removes a parent
     * @param personId the former child
     * @param parentId the former parent
     * @return the child's detail view
     */
    @RequestMapping("/persons/{cid}/parents/remove")
    public String removeParent(@PathVariable("cid") String personId, @RequestParam("person-id") String parentId) {
    	Person person = personManager.findPerson(personId).orElseThrow(IllegalArgumentException::new);
    	Person parent = personManager.findPerson(parentId).orElseThrow(IllegalArgumentException::new);

    	person.disconnectParentProfile(parent);
    	personManager.updatePerson(person.getId(), person);

    	return "redirect:/persons/" + personId;
    }

}
