package de.naju.adebar.web.controller.persons;

import java.time.LocalDateTime;
import java.util.List;
import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.querydsl.core.BooleanBuilder;
import de.naju.adebar.model.chapter.LocalGroupRepository;
import de.naju.adebar.model.events.EventRepository;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.model.persons.PersonRepository;
import de.naju.adebar.model.persons.QPerson;
import de.naju.adebar.model.persons.qualifications.QualificationRepository;
import de.naju.adebar.util.Assert2;
import de.naju.adebar.web.model.persons.participants.EventCollection;
import de.naju.adebar.web.model.persons.participants.EventCollection.EventCollectionBuilder;
import de.naju.adebar.web.validation.persons.AddPersonForm;
import de.naju.adebar.web.validation.persons.AddPersonFormConverter;

/**
 * Handles the creation of new person instances from the 'add person' template
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
      AddPersonFormConverter personFormConverter) {

    Assert2.noNullArguments("No argument may be null", //
        personRepo, eventRepo, localGroupRepo, qualificationRepo, personFormConverter);

    this.personRepo = personRepo;
    this.eventRepo = eventRepo;
    this.localGroupRepo = localGroupRepo;
    this.qualificationRepo = qualificationRepo;
    this.personFormConverter = personFormConverter;
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
    model.addAttribute("localGroups", localGroupRepo.findAll());
    model.addAttribute("qualifications", qualificationRepo.findAll());
    model.addAttribute("eventCollection", createEventCollection());

    if (!model.containsAttribute("form")) {
      model.addAttribute("form", new AddPersonForm());
    }

    return "persons/addPerson";
  }

  /**
   * Saves a new person
   *
   * @param form the filled add person form
   * @param errors validation errors within the form
   * @return a redirection to the new person's profile page
   */
  @PostMapping("/persons/add")
  @Transactional
  public String addPerson(@ModelAttribute("form") @Valid AddPersonForm form, //
      @RequestParam(value = "return-action", defaultValue = "") String returnAction, //
      @RequestParam(value = "return-to", defaultValue = "") String returnPath, //
      Errors errors, RedirectAttributes redirAttr) {

    boolean success = true;

    // check n° 1: form has no errors
    if (errors.hasErrors()) {
      redirAttr.addFlashAttribute("form", form);
      redirAttr.addFlashAttribute(errors);
      success = false;
    }

    Person newPerson = personFormConverter.toEntity(form);

    // check n° 2: there are no similar persons yet
    List<Person> similarPersons = checkForPersonsSimilarTo(newPerson);
    if (!similarPersons.isEmpty()) {
      redirAttr.addFlashAttribute("similarPersons", similarPersons);
      redirAttr.addFlashAttribute("form", form);
      success = false;
    }

    // if something did not go as expected, we will cancel adding the person and instead redirect
    // to the add person form. Depending on the kind of problem, the model has already been filled
    // for us accordingly
    if (!success) {
      if (!returnAction.isEmpty()) {
        redirAttr.addAttribute("return-action", returnAction);
      }

      if (!returnPath.isEmpty()) {
        redirAttr.addAttribute("return-to", returnPath);
      }

      return "redirect:/persons/add";
    }

    // everything seems fine, finish saving the new person
    addToEventsIfNecessary(newPerson, form);
    addToLocalGroupsIfNecessary(newPerson, form);
    personRepo.save(newPerson);

    if (!returnAction.isEmpty()) {
      redirAttr.addAttribute("do", returnAction);
    }

    if (!returnPath.isEmpty()) {
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
   * This method must run in a transactional context in order to persist its changed
   *
   * @param person the new person
   * @param form form possibly containing the events to attend
   */
  protected void addToEventsIfNecessary(Person person, AddPersonForm form) {
    if (!form.isParticipant() || !form.getParticipantForm().hasEvents()) {
      return;
    }
    form.getParticipantForm().getEvents().forEach(event -> event.addParticipant(person));
  }

  /**
   * If the new person is an activist and it is part of local groups, this method will take care of
   * creating this connection.
   * <p>
   * This method must run in a transactional context in order to persist its changed
   *
   * @param person the new person
   * @param form form possibly containing the local groups the person should be part of
   */
  protected void addToLocalGroupsIfNecessary(Person person, AddPersonForm form) {
    if (!form.isActivist() || !form.getActivistForm().hasLocalGroups()) {
      return;
    }
    form.getActivistForm().getLocalGroups().forEach(localGroup -> localGroup.addMember(person));
  }

  /**
   * Collects all future events which are not yet booked out and wraps them into a model object to
   * better support their display in a select box
   *
   * @return the event wrapper
   */
  private EventCollection createEventCollection() {
    EventCollectionBuilder builder = EventCollection.newCollection();
    eventRepo.findByStartTimeIsAfter(LocalDateTime.now()).stream()
        .filter(event -> !event.isBookedOut()).forEach(event -> {
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
    BooleanBuilder predicate = new BooleanBuilder();
    predicate //
        .and(person.firstName.containsIgnoreCase(newPerson.getFirstName())) //
        .and(person.lastName.containsIgnoreCase(newPerson.getLastName()));
    return personRepo.findAll(predicate);
  }

}
