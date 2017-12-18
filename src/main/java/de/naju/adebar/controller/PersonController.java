package de.naju.adebar.controller;

import java.util.HashSet;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.google.common.collect.Iterables;
import com.querydsl.core.BooleanBuilder;
import de.naju.adebar.app.human.filter.predicate.PersonFilterBuilder;
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
import de.naju.adebar.services.conversion.human.ActivistToEditActivistFormConverter;
import de.naju.adebar.services.conversion.human.PersonToEditPersonFormConverter;

/**
 * Person related controller mappings
 *
 * @author Rico Bergmann
 * @see Person
 */
@Controller
@PreAuthorize("hasRole('ROLE_USER')")
public class PersonController {

  private static final String EMAIL_DELIMITER = ";";
  private static final String ADD_PERSON_FORM = "addPersonForm";
  private static final String FILTER_PERSONS_FORM = "filterPersonsForm";
  private static final String PERSONS = "persons";
  private static final String QUALIFICATIONS = "qualifications";
  private static final String PERSONS_VIEW = "persons";
  private static final String REDIRECT_PERSONS = "redirect:/persons/";

  private final PersonControllerManagers managers;
  private final PersonControllerDataProcessors dataProcessors;

  @Autowired
  public PersonController(PersonControllerManagers managers,
      PersonControllerDataProcessors dataProcessors) {
    Assert.notNull(managers, "Managers may not be null");
    Assert.notNull(dataProcessors, "Data processors may not be null");
    this.managers = managers;
    this.dataProcessors = dataProcessors;
  }

  /**
   * Displays the person overview
   *
   * @param model model to display the data in
   * @return the persons' overview view
   */
  @RequestMapping("/persons")
  public String showPersonOverview(Model model) {
    model.addAttribute(ADD_PERSON_FORM, new CreatePersonForm());
    model.addAttribute(FILTER_PERSONS_FORM, new FilterPersonForm());
    model.addAttribute(PERSONS, managers.persons.repository().findFirst25());
    model.addAttribute("chapters", managers.localGroups.repository().findAll());
    model.addAttribute(QUALIFICATIONS, managers.qualifications.repository().findAll());
    return PERSONS_VIEW;
  }

  /**
   * Displays the person overview with all persons
   *
   * @param model model to display the data in
   * @return the persons' overview view
   */
  @RequestMapping("/persons/all")
  public String showAllPersons(Model model) {
    model.addAttribute(ADD_PERSON_FORM, new CreatePersonForm());
    model.addAttribute(FILTER_PERSONS_FORM, new FilterPersonForm());
    model.addAttribute(PERSONS, managers.persons.repository().findAllOrderByLastName());
    model.addAttribute(QUALIFICATIONS, managers.qualifications.repository().findAll());
    return PERSONS_VIEW;
  }

  /**
   * Adds a new person to the database.
   * <p>
   * If persons with the same name are already saved, an overview featuring a confirmation dialog
   * will be displayed instead.
   * </p>
   *
   * @param createPersonForm person object created by the model
   * @return the new person's detail view
   */
  @RequestMapping("/persons/add")
  public String addPerson(@ModelAttribute("addPersonFrom") CreatePersonForm createPersonForm,
      Model model) {
    Iterable<Person> similarPersons = managers.persons.repository().findByFirstNameAndLastName(
        createPersonForm.getFirstName(), createPersonForm.getLastName());

    if (!Iterables.isEmpty(similarPersons)) {
      model.addAttribute("existingPersons", similarPersons);
      model.addAttribute("newPerson", createPersonForm);
      return showPersonOverview(model);
    }

    Person person = dataProcessors.createPersonExtractor.extractPerson(createPersonForm);
    managers.persons.savePerson(person);

    if (person.isActivist()) {
      dataProcessors.createPersonExtractor
          .extractActivistLocalGroups(createPersonForm, managers.localGroups.repository())
          .ifPresent(groups -> groups.forEach(
              chapter -> managers.localGroups.addActivistToLocalGroupIfNecessary(chapter, person)));
    }

    return REDIRECT_PERSONS + person.getId();
  }

  /**
   * Adds a new person to the database.
   * <p>
   * If persons with the same name are already saved, the new person will be put into the database
   * anyways
   * </p>
   *
   * @param createPersonForm person object created by the model
   * @return the new person's detail view
   */
  @RequestMapping("/persons/addIgnoreSimilar")
  public String addPersonIgnoreSimilar(CreatePersonForm createPersonForm) {
    Person person = dataProcessors.createPersonExtractor.extractPerson(createPersonForm);
    managers.persons.savePerson(person);

    return REDIRECT_PERSONS + person.getId();
  }

  /**
   * Filters the saved persons
   *
   * @param filterPersonForm filter object created by the model
   * @param model model to display the data in
   * @return the persons' overview consisting of the persons that matched the filter
   */
  @RequestMapping("/persons/filter")
  @Transactional
  public String filterPersons(
      @ModelAttribute(FILTER_PERSONS_FORM) FilterPersonForm filterPersonForm, Model model) {
    PersonFilterBuilder filterBuilder = new PersonFilterBuilder();
    dataProcessors.filterPersonExtractor.extractAllFilters(filterPersonForm)
        .forEach(filterBuilder::applyFilter);

    BooleanBuilder predicate = filterBuilder.filter();

    List<Person> matchingPersons = managers.persons.repository().findAll(predicate);
    HashSet<Person> persons;
    if (filterPersonForm.isReturnParents()) {
      persons = new HashSet<>(matchingPersons.size());
      matchingPersons.stream().map(Person::getParentProfiles)
          .forEach(parents -> parents.forEach(persons::add));
    } else {
      persons = new HashSet<>(matchingPersons);
    }

    String matchingPersonsEmail =
        dataProcessors.persons.extractEmailAddressesAsString(persons, EMAIL_DELIMITER);

    model.addAttribute("filtered", true);
    model.addAttribute(PERSONS, persons);
    model.addAttribute("emails", matchingPersonsEmail);
    model.addAttribute(QUALIFICATIONS, managers.qualifications.repository().findAll());
    model.addAttribute(ADD_PERSON_FORM, new CreatePersonForm());
    model.addAttribute(FILTER_PERSONS_FORM, new FilterPersonForm());
    return PERSONS_VIEW;
  }

  /**
   * Detail view for persons
   *
   * @param personId the id of the person to display
   * @param model model to display the data in
   * @return the person's detail view
   */
  @RequestMapping("/persons/{pid}")
  public String showPersonDetails(@PathVariable("pid") String personId, Model model) {

    Person person =
        managers.persons.findPerson(personId).orElseThrow(IllegalArgumentException::new);

    model.addAttribute("editPersonForm",
        new PersonToEditPersonFormConverter().convertToEditPersonForm(person));
    model.addAttribute("addQualificationForm", new AddQualificationForm());
    model.addAttribute("addParentForm", new CreateParentForm());
    model.addAttribute("allChapters", managers.localGroups.repository().findAll());

    model.addAttribute("person", person);

    if (person.isActivist()) {
      Iterable<LocalGroup> localGroups =
          managers.localGroups.repository().findByMembersContains(person);
      Iterable<LocalGroup> boards = managers.localGroups.findAllLocalGroupsForBoardMember(person);

      model.addAttribute("isActivist", !person.isArchived());
      model.addAttribute("editActivistForm",
          new ActivistToEditActivistFormConverter(managers.localGroups.repository())
              .convertToEditActivistForm(person));
      model.addAttribute("localGroups", Iterables.isEmpty(localGroups) ? null : localGroups);
      model.addAttribute("boards", Iterables.isEmpty(boards) ? null : boards);
    } else {
      model.addAttribute("isActivist", false);
      model.addAttribute("editActivistForm", new EditActivistForm());
    }

    model.addAttribute("isReferent", person.isReferent());

    model.addAttribute("allQualifications", managers.qualifications.repository().findAll());

    return "personDetails";
  }

  /**
   * Updates a person
   *
   * @param personId the id of the person to edit
   * @param personForm person object created by the model
   * @param redirAttr attributes for the view to display some result information
   * @return the person's detail view
   */
  @RequestMapping("/persons/{pid}/edit")
  public String editPerson(@PathVariable("pid") String personId,
      @ModelAttribute("editPersonForm") EditPersonForm personForm, RedirectAttributes redirAttr) {

    Person person =
        managers.persons.findPerson(personId).orElseThrow(IllegalArgumentException::new);

    managers.persons.updatePerson(person.getId(),
        dataProcessors.editPersonExtractor.extractPerson(person.getId(), personForm));

    redirAttr.addFlashAttribute("personUpdated", true);
    return REDIRECT_PERSONS + personId;
  }

  /**
   * Deactivates a person. It will not be listed in selection dialogs any more
   *
   * @param personId the id of the person to remove
   * @param redirAttr attributes for the view to display some result information
   * @return the persons' overview view
   */
  @RequestMapping("/persons/{pid}/delete")
  public String deletePerson(@PathVariable("pid") String personId, RedirectAttributes redirAttr) {
    Person person =
        managers.persons.findPerson(personId).orElseThrow(IllegalArgumentException::new);

    try {
      managers.persons.deactivatePerson(person);
    } catch (IllegalStateException e) {
      redirAttr.addFlashAttribute("deletionError", true);
      return REDIRECT_PERSONS + personId;
    }

    redirAttr.addFlashAttribute("personDeleted", true);
    return REDIRECT_PERSONS;
  }

  /**
   * Edits the activist data of a person
   *
   * @param personId the id of the person/activist to edit
   * @param activistForm the activists new data
   * @param redirAttr attributes for the view to display some result information
   * @return the person's detail view
   */
  @RequestMapping("/persons/{pid}/edit-activist")
  public String editActivist(@PathVariable("pid") String personId,
      @ModelAttribute("editActivistForm") EditActivistForm activistForm,
      RedirectAttributes redirAttr) {

    Person person =
        managers.persons.findPerson(personId).orElseThrow(IllegalArgumentException::new);

    if (!person.isActivist()) {
      person.makeActivist();
    }

    person.setActivistProfile(
        dataProcessors.editActivistExtractor.extractActivistForm(person, activistForm));
    managers.persons.updatePerson(person.getId(), person);

    managers.localGroups.updateLocalGroupMembership(person,
        managers.localGroups.repository().findAll(activistForm.getLocalGroups()));

    return REDIRECT_PERSONS + personId;
  }

  /**
   * Adds a new qualification to a referent. If the person is not a referent already, it will be
   * turned into one.
   *
   * @param personId the id of the person/referent
   * @param qualificationForm qualification object created by the model
   * @param redirAttr attributes for the view to display some result information
   * @return the person's detail view
   */
  @RequestMapping("/persons/{pid}/add-qualification")
  public String addQualification(@PathVariable("pid") String personId,
      @ModelAttribute("addQualificationForm") AddQualificationForm qualificationForm,
      RedirectAttributes redirAttr) {

    Person person =
        managers.persons.findPerson(personId).orElseThrow(IllegalArgumentException::new);

    if (!person.isReferent()) {
      person.makeReferent();
    }

    managers.persons.addQualificationToPerson(person,
        dataProcessors.addQualificationExtractor.extractQualification(qualificationForm));

    redirAttr.addFlashAttribute("qualificationAdded", true);
    return REDIRECT_PERSONS + personId;
  }

  /**
   * Removes a qualification from a referent
   *
   * @param personId the id of the person/referent
   * @param qualificationName qualification object created by the model
   * @param redirAttr attributes for the view to display some result information
   * @return the person's detail view
   */
  @RequestMapping("/persons/{pid}/qualifications/remove")
  public String removeQualification(@PathVariable("pid") String personId,
      @RequestParam("name") String qualificationName, RedirectAttributes redirAttr) {

    Person person =
        managers.persons.findPerson(personId).orElseThrow(IllegalArgumentException::new);

    Qualification qualification = managers.qualifications.findQualification(qualificationName)
        .orElseThrow(IllegalArgumentException::new);

    person.getReferentProfile().removeQualification(qualification);
    managers.persons.updatePerson(person.getId(), person);

    return REDIRECT_PERSONS + personId;
  }

  /**
   * Registers a person as parent
   *
   * @param personId the child
   * @param parentId the parent
   * @return the child's detail view
   */
  @RequestMapping("/persons/{cid}/parents/add/existing")
  public String addParent(@PathVariable("cid") String personId,
      @RequestParam("person-id") String parentId, RedirectAttributes redirAttr) {
    Person person =
        managers.persons.findPerson(personId).orElseThrow(IllegalArgumentException::new);
    Person parent =
        managers.persons.findPerson(parentId).orElseThrow(IllegalArgumentException::new);

    try {
      person.connectParentProfile(parent);
      managers.persons.updatePerson(person.getId(), person);
    } catch (ImpossibleKinshipRelationException e) {
      redirAttr.addFlashAttribute("impossibleKinship", true);
    }

    return REDIRECT_PERSONS + personId;
  }

  /**
   * Creates a new parent for a person
   *
   * @param personId the child
   * @param createParentForm the form containing the parent's data
   * @return the child's detail view
   */
  @RequestMapping("/persons/{cid}/parents/add/new")
  public String createParent(@PathVariable("cid") String personId,
      @ModelAttribute("addPersonFrom") CreateParentForm createParentForm,
      RedirectAttributes redirAttr) {
    Person person =
        managers.persons.findPerson(personId).orElseThrow(IllegalArgumentException::new);
    Person parent = dataProcessors.createPersonExtractor.extractParent(person, createParentForm);

    managers.persons.savePerson(parent);
    person.connectParentProfile(parent);
    managers.persons.updatePerson(person.getId(), person);

    return REDIRECT_PERSONS + personId;
  }

  /**
   * Removes a parent
   *
   * @param personId the former child
   * @param parentId the former parent
   * @return the child's detail view
   */
  @RequestMapping("/persons/{cid}/parents/remove")
  public String removeParent(@PathVariable("cid") String personId,
      @RequestParam("person-id") String parentId) {
    Person person =
        managers.persons.findPerson(personId).orElseThrow(IllegalArgumentException::new);
    Person parent =
        managers.persons.findPerson(parentId).orElseThrow(IllegalArgumentException::new);

    person.disconnectParentProfile(parent);
    managers.persons.updatePerson(person.getId(), person);

    return REDIRECT_PERSONS + personId;
  }

}
