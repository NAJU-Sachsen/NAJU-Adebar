package de.naju.adebar.web.controller.persons;

import com.querydsl.core.BooleanBuilder;
import de.naju.adebar.model.chapter.LocalGroupRepository;
import de.naju.adebar.model.events.Event;
import de.naju.adebar.model.events.EventRepository;
import de.naju.adebar.model.events.ParticipationManager;
import de.naju.adebar.model.events.ParticipationManager.Result;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.model.persons.PersonRepository;
import de.naju.adebar.model.persons.QPerson;
import de.naju.adebar.model.persons.qualifications.QualificationRepository;
import de.naju.adebar.util.Assert2;
import de.naju.adebar.web.model.persons.participants.EventCollection;
import de.naju.adebar.web.model.persons.participants.EventCollection.EventCollectionBuilder;
import de.naju.adebar.web.validation.persons.AddPersonForm;
import de.naju.adebar.web.validation.persons.AddPersonFormConverter;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Handles the creation of new person instances from the 'add person' template.
 *
 * @author Rico Bergmann
 */
@Controller
public class AddPersonController {

  private static final QPerson person = QPerson.person;
  private final PersonRepository personRepo;
  private final EventRepository eventRepo;
  private final LocalGroupRepository localGroupRepo;
  private final QualificationRepository qualificationRepo;
  private final AddPersonFormConverter personFormConverter;
  private final ParticipationManager participationManager;

  /**
   * Full constructor
   *
   * @param personRepo repository receiving the fresh person instances
   * @param eventRepo repository containing all future events
   * @param localGroupRepo repository containing all available local groups for activists
   * @param qualificationRepo repository containing all available referent qualifications
   * @param personFormConverter service to convert an {@link AddPersonForm} to new persons
   */
  public AddPersonController(PersonRepository personRepo, EventRepository eventRepo,
      LocalGroupRepository localGroupRepo, QualificationRepository qualificationRepo,
      AddPersonFormConverter personFormConverter, ParticipationManager participationManager) {

    Assert2.noNullArguments("No argument may be null", //
        personRepo, eventRepo, localGroupRepo, qualificationRepo, personFormConverter,
        participationManager);

    this.personRepo = personRepo;
    this.eventRepo = eventRepo;
    this.localGroupRepo = localGroupRepo;
    this.qualificationRepo = qualificationRepo;
    this.personFormConverter = personFormConverter;
    this.participationManager = participationManager;
  }

  /**
   * Renders the template to add new persons to the database
   *
   * @param model model to put the data to render into
   * @return the add person template
   */
  @GetMapping("/persons/add")
  public String showAddPersonView(Model model) {

    // add all "backing" attributes first so that form and errors may operate on them
    prepareModel(model);

    if (!model.containsAttribute("form")) {
      model.addAttribute("form", new AddPersonForm());
    }

    return "persons/addPerson";
  }

  /**
   * Saves a new person
   *
   * @param form form containing the info about the new person
   * @param errors validation errors for the form
   * @return a redirection to the new person's profile page
   */
  @PostMapping("/persons/add")
  public String addPerson(@ModelAttribute("form") @Valid AddPersonForm form, Errors errors, //
      @RequestParam(value = "return-action", defaultValue = "") String returnAction, //
      @RequestParam(value = "return-to", defaultValue = "") String returnPath, //
      Model model, RedirectAttributes redirAttr) {

    Person newPerson = null;
    boolean success = true;

    // check n° 1: form has no errors
    if (errors.hasErrors()) {
      success = false;
    } else {
      // only generate the person if no errors were found otherwise the converter may throw
      // arbitrary exceptions due to a illegal data in the form
      newPerson = personFormConverter.toEntity(form);
    }

    // check n° 2: there are no similar persons yet
    List<Person> similarPersons = checkForPersonsSimilarTo(newPerson);
    if (!similarPersons.isEmpty()) {
      model.addAttribute("similarPersons", similarPersons);
      success = false;
    }

    // If something did not go as expected, we will cancel adding the person and instead redirect
    // to the add person form. The model has already been filled according to the kind of error in
    // this case.
    if (!success) {

      /*// retain the attributes for post-processing a successful run
      if (!returnAction.isEmpty()) {
        redirAttr.addAttribute("return-action", returnAction);
      }
      if (!returnPath.isEmpty()) {
        redirAttr.addAttribute("return-to", returnPath);
      }*/

      prepareModel(model);

      return "persons/addPerson";
    }

    // everything seems fine, finish saving the new person
    newPerson = personRepo.save(newPerson);

    addToEventsIfNecessary(newPerson, form, redirAttr);
    addToLocalGroupsIfNecessary(newPerson, form);

    if (!returnAction.isEmpty()) {
      redirAttr.addAttribute("do", returnAction);
    }

    if (!returnPath.isEmpty()) {
      redirAttr.addFlashAttribute("newPerson", newPerson);
      redirAttr.addAttribute("from", "add-person");
      return "redirect:" + returnPath;
    }

    return "redirect:/persons/" + newPerson.getId();
  }

  /**
   * Registers the validator for the {@link AddPersonForm}
   *
   * @param binder the binder
   */
  @InitBinder("form")
  protected void initBinders(WebDataBinder binder) {
    binder.addValidators(personFormConverter);
  }

  /**
   * If the new person is a participant and it should attend an event right away, this method will
   * take care of exactly that.
   * <p>
   * This method must run in a transactional context in order to persist its changes
   *
   * @param person the new person
   * @param form form possibly containing the events to attend
   */
  protected boolean addToEventsIfNecessary(Person person, AddPersonForm form,
      RedirectAttributes redirAttr) {
    if (!form.isParticipant() || !form.getParticipantForm().hasEvents()) {
      return true;
    }

    boolean success = true;
    Map<Event, Result> failedParticipations = new HashMap<>();
    for (Event event : form.getParticipantForm().getEvents()) {
      Result result = participationManager.addParticipant(event, person);

      if (!result.isOk()) {
        failedParticipations.put(event, result);
        success = false;
      }
    }

    redirAttr.addFlashAttribute("failedParticipations", failedParticipations);
    redirAttr.addFlashAttribute("participationFailed", !success);
    return success;
  }

  /**
   * If the new person is an activist and should be part of local groups, this method will take care
   * of creating the necessary associations.
   * <p>
   * This method must run in a transactional context in order to persist its changes
   *
   * @param person the new person
   * @param form form possibly containing the local groups the person should be part of
   */
  protected void addToLocalGroupsIfNecessary(Person person, AddPersonForm form) {
    if (!form.isActivist() || !form.getActivistForm().hasLocalGroups()) {
      return;
    }
    form.getActivistForm().getLocalGroups().forEach(localGroup -> {
      localGroup.addMember(person);
      localGroupRepo.save(localGroup);
    });
  }

  /**
   * Collects all future events which are not yet booked out and wraps them into a model object to
   * better support their display in a select box
   *
   * @return the event wrapper
   */
  private EventCollection createEventCollection() {
    EventCollectionBuilder builder = EventCollection.newCollection();
    eventRepo.findByStartTimeIsAfterAndParticipantsListBookedOutIsFalse(LocalDateTime.now())
        .forEach(event -> {
          if (event.isForLocalGroup()) {
            builder.appendFor(event.getLocalGroup(), event);
          } else if (event.isForProject()) {
            builder.appendFor(event.getProject(), event);
          } else {
            builder.appendRaw(event);
          }
        });
    return builder.done();
  }

  /**
   * Searches for persons with a similar name like the given one
   *
   * @param newPerson the person to compare
   * @return all persons with a similar name
   */
  private List<Person> checkForPersonsSimilarTo(Person newPerson) {
    if (newPerson == null) {
      return Collections.emptyList();
    }

    BooleanBuilder predicate = new BooleanBuilder();
    predicate //
        .and(person.firstName.containsIgnoreCase(newPerson.getFirstName())) //
        .and(person.lastName.containsIgnoreCase(newPerson.getLastName()));
    return personRepo.findAll(predicate);
  }

  private void prepareModel(Model model) {
    model.addAttribute("localGroups", localGroupRepo.findAll());
    model.addAttribute("qualifications", qualificationRepo.findAll());
    model.addAttribute("eventCollection", createEventCollection());
  }

}
