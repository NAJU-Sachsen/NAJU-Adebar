package de.naju.adebar.web.controller.persons;

import de.naju.adebar.model.chapter.LocalGroupRepository;
import de.naju.adebar.model.events.EventRepository;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.model.persons.PersonRepository;
import de.naju.adebar.model.persons.qualifications.QualificationRepository;
import de.naju.adebar.util.Assert2;
import de.naju.adebar.web.model.persons.participants.EventCollection;
import de.naju.adebar.web.model.persons.participants.EventCollection.EventCollectionBuilder;
import de.naju.adebar.web.validation.persons.AddPersonForm;
import de.naju.adebar.web.validation.persons.AddPersonFormConverter;
import java.time.LocalDateTime;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Handles the creation of new person instances from the 'add person' template
 *
 * @author Rico Bergmann
 */
@Controller
public class AddPersonController {

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
   * @param model model to put the form into if validation failed or other errors occurred
   * @return a redirection to the new person's profile page
   */
  @PostMapping("/persons/add")
  @Transactional
  public String addPerson(@ModelAttribute("form") @Valid AddPersonForm form, Errors errors,
      Model model, RedirectAttributes redirAttr) {

    if (errors.hasErrors()) {
      model.addAttribute("form", form);
      return showAddPersonView(model);
    }

    Person person = personFormConverter.toEntity(form);
    addToEventsIfNecessary(person, form);
    addToLocalGroupsIfNecessary(person, form);
    personRepo.save(person);

    return "redirect:/persons/" + person.getId();
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
   *
   * @param person the new person
   * @param form form possibly containing the events to attend
   */
  @Transactional
  protected void addToEventsIfNecessary(Person person, AddPersonForm form) {
    if (!form.isParticipant() || !form.getParticipantForm().hasEvents()) {
      return;
    }
    form.getParticipantForm().getEvents().forEach(event -> event.addParticipant(person));
  }

  /**
   * If the new person is an activist and it is part of local groups, this method will take care of
   * creating this connection.
   *
   * @param person the new person
   * @param form form possibly containing the local groups the person should be part of
   */
  @Transactional
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
        .filter(event -> !event.isBookedOut())
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

}
