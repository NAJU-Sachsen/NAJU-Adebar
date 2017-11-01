package de.naju.adebar.controller;

import java.time.LocalDateTime;
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
import de.naju.adebar.app.chapter.LocalGroupManager;
import de.naju.adebar.app.chapter.ProjectManager;
import de.naju.adebar.app.events.EventDataProcessor;
import de.naju.adebar.app.events.EventDataProcessor.EventType;
import de.naju.adebar.app.events.EventManager;
import de.naju.adebar.app.events.filter.EventFilterBuilder;
import de.naju.adebar.app.human.DataProcessor;
import de.naju.adebar.app.human.PersonManager;
import de.naju.adebar.controller.forms.events.EventForm;
import de.naju.adebar.controller.forms.events.FilterEventsForm;
import de.naju.adebar.model.chapter.LocalGroup;
import de.naju.adebar.model.chapter.Project;
import de.naju.adebar.model.events.BookedOutException;
import de.naju.adebar.model.events.Event;
import de.naju.adebar.model.events.ExistingParticipantException;
import de.naju.adebar.model.events.ParticipationInfo;
import de.naju.adebar.model.events.PersonIsTooYoungException;
import de.naju.adebar.model.human.Person;
import de.naju.adebar.model.human.PersonId;
import de.naju.adebar.services.conversion.events.EventFormDataExtractor;
import de.naju.adebar.services.conversion.events.EventToEventFormConverter;
import de.naju.adebar.services.conversion.events.FilterEventsFormDataExtractor;

/**
 * Event related controller mappings
 * 
 * @author Rico Bergmann
 * @see Event
 *
 */
@Controller
public class EventController {
  private final static String EMAIL_DELIMITER = ";";

  private PersonManager personManager;
  private EventManager eventManager;
  private LocalGroupManager localGroupManager;
  private ProjectManager projectManager;
  private EventFormDataExtractor eventFormDataExtractor;
  private EventToEventFormConverter eventToEventFormConverter;
  private FilterEventsFormDataExtractor filterEventsFormDataExtractor;
  private DataProcessor humanDataProcessor;
  private EventDataProcessor eventDataProcessor;

  @Autowired
  public EventController(PersonManager personManager, EventManager eventManager,
      LocalGroupManager localGroupManager, ProjectManager projectManager,
      EventFormDataExtractor eventFormDataExtractor,
      EventToEventFormConverter eventToEventFormConverter,
      FilterEventsFormDataExtractor filterEventsFormDataExtractor, DataProcessor humanDataProcessor,
      EventDataProcessor eventDataProcessor) {
    Object[] params = {personManager, eventManager, localGroupManager, projectManager,
        eventFormDataExtractor, eventToEventFormConverter, filterEventsFormDataExtractor,
        humanDataProcessor, eventDataProcessor};
    Assert.noNullElements(params, "No parameter may be null: " + Arrays.toString(params));
    this.personManager = personManager;
    this.eventManager = eventManager;
    this.localGroupManager = localGroupManager;
    this.projectManager = projectManager;
    this.eventFormDataExtractor = eventFormDataExtractor;
    this.eventToEventFormConverter = eventToEventFormConverter;
    this.filterEventsFormDataExtractor = filterEventsFormDataExtractor;
    this.humanDataProcessor = humanDataProcessor;
    this.eventDataProcessor = eventDataProcessor;
  }

  /**
   * Displays the event overview
   * 
   * @param model model containing the data to display
   * @return the events' overview view
   */
  @RequestMapping("/events")
  public String showEventOverview(Model model) {

    Iterable<Event> currentEvents = eventManager.findOngoingEvents();
    Iterable<Event> futureEvents =
        eventManager.repository().findByStartTimeIsAfter(LocalDateTime.now());

    model.addAttribute("currentEvents", currentEvents);
    model.addAttribute("currentEventsLocalGroups",
        eventDataProcessor.getLocalGroupBelonging(EventType.RUNNING));
    model.addAttribute("currentEventsProjects",
        eventDataProcessor.getProjectBelonging(EventType.RUNNING));

    model.addAttribute("futureEvents", futureEvents);
    model.addAttribute("futureEventsLocalGroups",
        eventDataProcessor.getLocalGroupBelonging(EventType.FUTURE));
    model.addAttribute("futureEventsProjects",
        eventDataProcessor.getProjectBelonging(EventType.FUTURE));

    model.addAttribute("addEventForm", new EventForm());
    model.addAttribute("filterEventsForm", new FilterEventsForm());
    model.addAttribute("localGroups", localGroupManager.repository().findAll());
    model.addAttribute("projects", projectManager.repository().findAll());
    model.addAttribute("normalDisplay", true);

    return "events";
  }

  /**
   * Displays all past events
   * 
   * @param model model from which the displayed data should be taken
   * @return the overview of all past events
   */
  @RequestMapping("/events/past")
  public String showPastEvents(Model model) {

    Iterable<Event> pastEvents =
        eventManager.repository().findByEndTimeIsBefore(LocalDateTime.now());

    model.addAttribute("pastEvents", pastEvents);
    model.addAttribute("pastEventsLocalGroups",
        eventDataProcessor.getLocalGroupBelonging(EventType.PAST));
    model.addAttribute("pastEventsProjects",
        eventDataProcessor.getProjectBelonging(EventType.PAST));

    model.addAttribute("addEventForm", new EventForm());
    model.addAttribute("filterEventsForm", new FilterEventsForm());
    model.addAttribute("localGroups", localGroupManager.repository().findAll());
    model.addAttribute("projects", projectManager.repository().findAll());

    return "events";
  }

  /**
   * Displays all events which matched certain criteria
   * 
   * @param eventsForm form specifying the criteria to filter for
   * @param model model containing the data to display
   * @return the overview of all matching events
   */
  @RequestMapping("/events/filter")
  @Transactional
  public String filterEvents(@ModelAttribute("filterEventsForm") FilterEventsForm eventsForm,
      Model model) {
    Stream<Event> events = eventManager.repository().streamAll();
    EventFilterBuilder filterBuilder = new EventFilterBuilder(events);
    filterEventsFormDataExtractor.extractAllFilters(eventsForm).forEach(filterBuilder::applyFilter);

    Iterable<Event> matchingEvents = filterBuilder.filter();

    model.addAttribute("filteredEvents", matchingEvents);
    model.addAttribute("filteredEventsLocalGroups",
        eventDataProcessor.getLocalGroupBelonging(matchingEvents));
    model.addAttribute("filteredEventsProjects",
        eventDataProcessor.getProjectBelonging(matchingEvents));

    model.addAttribute("addEventForm", new EventForm());
    model.addAttribute("filterEventsForm", new FilterEventsForm());
    model.addAttribute("localGroups", localGroupManager.repository().findAll());
    model.addAttribute("projects", projectManager.repository().findAll());
    return "events";
  }

  /**
   * Adds a new event to the database
   * 
   * @param eventForm the submitted event data
   * @param redirAttr attributes for the view to display some result information
   * @return the event's detail view
   */
  @RequestMapping("/events/add")
  public String addEvent(@ModelAttribute("addEventForm") EventForm eventForm,
      RedirectAttributes redirAttr) {
    Event event = eventManager.saveEvent(eventFormDataExtractor.extractEvent(eventForm));

    switch (eventFormDataExtractor.extractBelonging(eventForm)) {
      case PROJECT:
        Project project = projectManager.findProject(eventForm.getProjectId())
            .orElseThrow(IllegalArgumentException::new);
        project.addEvent(event);
        projectManager.updateProject(project.getId(), project);
        break;
      case LOCALGROUP:
        LocalGroup localGroup = localGroupManager.findLocalGroup(eventForm.getLocalGroupId())
            .orElseThrow(IllegalArgumentException::new);
        localGroup.addEvent(event);
        localGroupManager.updateLocalGroup(localGroup.getId(), localGroup);
        break;
    }

    return "redirect:/events/" + event.getId();
  }

  /**
   * Detail view for an event
   * 
   * @param eventId the id of the event to display
   * @param model model containing the data to display
   * @return the event's detail view
   */
  @RequestMapping("/events/{eid}")
  public String showEventDetails(@PathVariable("eid") String eventId, Model model) {
    Event event = eventManager.findEvent(eventId).orElseThrow(IllegalArgumentException::new);
    Iterable<Person> organizers = event.getOrganizers();
    Iterable<Person> counselors = event.getCounselors();
    Iterable<Person> participants = event.getParticipants();

    model.addAttribute("event", event);
    model.addAttribute("organizers", organizers);
    model.addAttribute("organizerEmails",
        humanDataProcessor.extractEmailAddressesAsString(organizers, EMAIL_DELIMITER));

    model.addAttribute("counselors", counselors);
    model.addAttribute("counselorEmails",
        humanDataProcessor.extractEmailAddressesAsString(counselors, EMAIL_DELIMITER));

    model.addAttribute("participantEmails",
        humanDataProcessor.extractEmailAddressesAsString(participants, EMAIL_DELIMITER));
    model.addAttribute("participantEmailsNoFee", humanDataProcessor
        .extractEmailAddressesAsString(event.getParticipantsWithFeeNotPayed(), EMAIL_DELIMITER));
    model.addAttribute("participantEmailsNoForm", humanDataProcessor.extractEmailAddressesAsString(
        event.getParticipantsWithFormNotReceived(), EMAIL_DELIMITER));

    model.addAttribute("noOrganizers", !organizers.iterator().hasNext());
    model.addAttribute("noCounselors", !counselors.iterator().hasNext());
    model.addAttribute("noParticipants", !participants.iterator().hasNext());

    model.addAttribute("editEventForm", eventToEventFormConverter.convertToEventForm(event));
    return "eventDetails";
  }

  /**
   * Adapts information about an event
   * 
   * @param eventId the id of the event to update
   * @param eventForm the submitted new event data
   * @param redirAttr attributes for the view to display some result information
   * @return the event's detail view
   */
  @RequestMapping("/events/{eid}/update")
  public String updateEvent(@PathVariable("eid") String eventId,
      @ModelAttribute("editEventForm") EventForm eventForm, RedirectAttributes redirAttr) {

    eventManager.adoptEventData(eventId, eventFormDataExtractor.extractEvent(eventForm));

    redirAttr.addFlashAttribute("eventUpdated", true);
    return "redirect:/events/" + eventId;
  }

  /**
   * Adds a participant to an event. This may actually not be exectuted if the person to add is too
   * young.
   * 
   * @param eventId the id of the event to add the participant to
   * @param personId the id of the person to add
   * @param redirAttr attributes for the view to display some result information
   * @return the event's detail view
   */
  @RequestMapping("/events/{eid}/participants/add")
  public String addParticipant(@PathVariable("eid") String eventId,
      @RequestParam("person-id") String personId, RedirectAttributes redirAttr) {
    Event event = eventManager.findEvent(eventId).orElseThrow(IllegalArgumentException::new);
    Person person = personManager.findPerson(personId).orElseThrow(IllegalArgumentException::new);

    try {
      event.addParticipant(person);
      eventManager.updateEvent(eventId, event);
      redirAttr.addFlashAttribute("participantAdded", true);
    } catch (ExistingParticipantException e) {
      redirAttr.addFlashAttribute("participatesAlready", true);
    } catch (PersonIsTooYoungException e) {
      redirAttr.addFlashAttribute("participantTooYoung", true);
      redirAttr.addFlashAttribute("newParticipantId", personId);
    } catch (BookedOutException e) {
      redirAttr.addFlashAttribute("bookedOut", true);
    }

    return "redirect:/events/" + eventId;
  }

  /**
   * Adds a participant to an event regardless of his age
   * 
   * @param eventId the id of the event to add the participant to
   * @param personId the id of the person to add
   * @param redirAttr attributes for the view to display some result information
   * @return the event's detail view
   */
  @RequestMapping("/events/{eid}/participants/force-add")
  public String addParticipantIgnoreAge(@PathVariable("eid") String eventId,
      @RequestParam("person-id") String personId, RedirectAttributes redirAttr) {
    Event event = eventManager.findEvent(eventId).orElseThrow(IllegalArgumentException::new);
    Person person = personManager.findPerson(personId).orElseThrow(IllegalArgumentException::new);

    try {
      event.addParticipantIgnoreAge(person);
      eventManager.updateEvent(eventId, event);
      redirAttr.addFlashAttribute("participantAdded", true);
    } catch (ExistingParticipantException e) {
      redirAttr.addFlashAttribute("participatesAlready", true);
    }

    return "redirect:/events/" + eventId;
  }

  /**
   * Updates the participation information about an participant
   * 
   * @param eventId the id of the event the person participates in
   * @param personId the id of the person whose information should be updated
   * @param feePayed whether the participation fee was payed
   * @param formReceived whether the legally binding participation form was already sent from the
   *        person
   * @param remarks remarks regarding the participation
   * @param redirAttr attributes for the view to display some result information
   * @return the event's detail view
   */
  @RequestMapping("/events/{eid}/participants/update")
  public String updateParticipant(@PathVariable("eid") String eventId,
      @RequestParam("person-id") String personId,
      @RequestParam(value = "fee-payed", required = false) boolean feePayed,
      @RequestParam(value = "form-received", required = false) boolean formReceived,
      @RequestParam(value = "remarks") String remarks, RedirectAttributes redirAttr) {
    Event event = eventManager.findEvent(eventId).orElseThrow(IllegalArgumentException::new);
    Person person = personManager.findPerson(personId).orElseThrow(IllegalArgumentException::new);

    ParticipationInfo participationInfo = event.getParticipationInfo(person);
    participationInfo.setParticipationFeePayed(feePayed);
    participationInfo.setRegistrationFormReceived(formReceived);
    participationInfo.setRemarks(remarks);
    event.updateParticipationInfo(person, participationInfo);
    eventManager.updateEvent(eventId, event);

    redirAttr.addFlashAttribute("participationInfoUpdated", true);
    return "redirect:/events/" + eventId;
  }

  /**
   * Removes a participant from an event
   * 
   * @param eventId the id of the event the person participated in
   * @param personId the id of the person that participated
   * @param redirAttr attributes for the view to display some result information
   * @return the event's detail view
   */
  @RequestMapping("/events/{eid}/participants/remove")
  public String removeParticipant(@PathVariable("eid") String eventId,
      @RequestParam("person-id") String personId, RedirectAttributes redirAttr) {
    Event event = eventManager.findEvent(eventId).orElseThrow(IllegalArgumentException::new);
    Person person = personManager.findPerson(personId).orElseThrow(IllegalArgumentException::new);

    event.removeParticipant(person);
    eventManager.updateEvent(eventId, event);

    redirAttr.addFlashAttribute("participantRemoved", true);
    return "redirect:/events/" + eventId;
  }

  /**
   * Adds a counselor to an event
   * 
   * @param eventId the event to which the counselor should be added
   * @param personId the id of the person who should be added as counselor
   * @param redirAttr attributes for the view to display some result information
   * @return the event's detail view
   */
  @RequestMapping("/events/{eid}/counselors/add")
  public String addCounselor(@PathVariable("eid") String eventId,
      @RequestParam("person-id") String personId, RedirectAttributes redirAttr) {
    Event event = eventManager.findEvent(eventId).orElseThrow(IllegalArgumentException::new);
    Person activist = personManager.findPerson(personId).orElseThrow(IllegalArgumentException::new);

    try {
      event.addCounselor(activist);
      eventManager.updateEvent(eventId, event);
      redirAttr.addFlashAttribute("counselorAdded", true);
    } catch (IllegalArgumentException e) {
      redirAttr.addFlashAttribute("existingCounselor", true);
    }

    return "redirect:/events/" + eventId;
  }

  /**
   * Removes a counselor from an event
   * 
   * @param eventId the event from which the counselor should be removed
   * @param personId the id of the person to remove
   * @param redirAttr attributes for the view to display some result information
   * @return the event's detail view
   */
  @RequestMapping("/events/{eid}/counselors/remove")
  public String removeCounselor(@PathVariable("eid") String eventId,
      @RequestParam("person-id") String personId, RedirectAttributes redirAttr) {
    Event event = eventManager.findEvent(eventId).orElseThrow(IllegalArgumentException::new);
    Person activist = personManager.findPerson(personId).orElseThrow(IllegalArgumentException::new);

    event.removeCounselor(activist);
    eventManager.updateEvent(eventId, event);

    redirAttr.addFlashAttribute("counselorRemoved", true);
    return "redirect:/events/" + eventId;
  }

  /**
   * Adds a non-activist as counselor to the event. This will make the person an activist.
   * 
   * @param eventId the event to which the counselor should be added
   * @param personId the id of the person who should be added as counselor
   * @param redirAttr attributes for the view to display some result information
   * @return the event's detail view
   */
  @RequestMapping("/events/{eid}/counselors/add-new")
  public String addNewCounselor(@PathVariable("eid") String eventId,
      @RequestParam("person-id") String personId, RedirectAttributes redirAttr) {
    Event event = eventManager.findEvent(eventId).orElseThrow(IllegalArgumentException::new);
    Person person = personManager.findPerson(personId).orElseThrow(IllegalArgumentException::new);

    try {
      person.makeActivist();
      event.addCounselor(person);
      personManager.updatePerson(new PersonId(personId), person);
      eventManager.updateEvent(eventId, event);
      redirAttr.addFlashAttribute("counselorAdded", true);
    } catch (IllegalStateException e) {
      // the person is already an activist
      redirAttr.addFlashAttribute("existingActivist", true);

    } catch (IllegalArgumentException e) {
      // the person is already an organizer
      redirAttr.addFlashAttribute("existingCounselor", true);
    }
    return "redirect:/events/" + eventId;
  }

  /**
   * Adds an organizer to an event
   * 
   * @param eventId the id of the event to add the organizer to
   * @param personId the id of the person to add as an organizer
   * @param redirAttr attributes for the view to display some result information
   * @return the event's detail view
   */
  @RequestMapping("/events/{eid}/organizers/add")
  public String addOrganizer(@PathVariable("eid") String eventId,
      @RequestParam("person-id") String personId, RedirectAttributes redirAttr) {
    Event event = eventManager.findEvent(eventId).orElseThrow(IllegalArgumentException::new);
    Person activist = personManager.findPerson(personId).orElseThrow(IllegalArgumentException::new);

    try {
      event.addOrganizer(activist);
      eventManager.updateEvent(eventId, event);
      redirAttr.addFlashAttribute("organizerAdded", true);
    } catch (IllegalArgumentException e) {
      redirAttr.addFlashAttribute("existingOrganizer", true);
    }

    return "redirect:/events/" + eventId;
  }

  /**
   * Removes an organizer from an event
   * 
   * @param eventId the event to remove the organizer from
   * @param personId the id of the person to remove
   * @param redirAttr attributes for the view to display some result information
   * @return the event's detail view
   */
  @RequestMapping("/events/{eid}/organizers/remove")
  public String removeOrganizer(@PathVariable("eid") String eventId,
      @RequestParam("person-id") String personId, RedirectAttributes redirAttr) {
    Event event = eventManager.findEvent(eventId).orElseThrow(IllegalArgumentException::new);
    Person activist = personManager.findPerson(personId).orElseThrow(IllegalArgumentException::new);

    event.removeOrganizer(activist);
    eventManager.updateEvent(eventId, event);

    redirAttr.addFlashAttribute("organizerRemoved", true);
    return "redirect:/events/" + eventId;
  }

  /**
   * Adds a non-activist as organizer to the event. This will make the person an activist.
   * 
   * @param eventId the event to which the organizer should be added
   * @param personId the id of the person who should be added as organizer
   * @param redirAttr attributes for the view to display some result information
   * @return the event's detail view
   */
  @RequestMapping("/events/{eid}/organizers/add-new")
  public String addNewOrganizer(@PathVariable("eid") String eventId,
      @RequestParam("person-id") String personId, RedirectAttributes redirAttr) {
    Event event = eventManager.findEvent(eventId).orElseThrow(IllegalArgumentException::new);
    Person person = personManager.findPerson(personId).orElseThrow(IllegalArgumentException::new);

    try {
      person.makeActivist();
      event.addOrganizer(person);
      personManager.updatePerson(new PersonId(personId), person);
      eventManager.updateEvent(eventId, event);
      redirAttr.addFlashAttribute("organizerAdded", true);
    } catch (IllegalStateException e) {
      // the person is already an activist
      redirAttr.addFlashAttribute("existingActivist", true);

    } catch (IllegalArgumentException e) {
      // the person is already an organizer
      redirAttr.addFlashAttribute("existingOrganizer", true);
    }
    return "redirect:/events/" + eventId;
  }

  /**
   * Adds a new person to contact to an event
   * 
   * @param eventId the event to edit
   * @param personId the person to add
   * @param remarks remarks regarding the necessity of the contact
   * @param redirAttr attributes for the view to display some result information
   * @return the event's detail view
   */
  @RequestMapping("/events/{eid}/personsToContact/add")
  public String addPersonToContact(@PathVariable("eid") String eventId,
      @RequestParam("person-id") String personId, @RequestParam("remarks") String remarks,
      RedirectAttributes redirAttr) {
    Event event = eventManager.findEvent(eventId).orElseThrow(IllegalArgumentException::new);
    Person person = personManager.findPerson(personId).orElseThrow(IllegalArgumentException::new);

    event.addPersonToContact(person, remarks);
    eventManager.updateEvent(eventId, event);

    redirAttr.addFlashAttribute("personToContactAdded", true);
    return "redirect:/events/" + eventId;
  }

  /**
   * Updates the remarks of a person to contact
   * 
   * @param eventId the event to edit
   * @param personId the person to edit
   * @param remarks the new remarks
   * @param redirAttr attributes for the view to display some result information
   * @return the event's detail view
   */
  @RequestMapping("/events/{eid}/personsToContact/update")
  public String editPersonToContact(@PathVariable("eid") String eventId,
      @RequestParam("person-id") String personId, @RequestParam("remarks") String remarks,
      RedirectAttributes redirAttr) {
    Event event = eventManager.findEvent(eventId).orElseThrow(IllegalArgumentException::new);
    Person person = personManager.findPerson(personId).orElseThrow(IllegalArgumentException::new);

    event.updatePersonToContact(person, remarks);
    eventManager.updateEvent(eventId, event);

    redirAttr.addFlashAttribute("personToContactUpdated", true);
    return "redirect:/events/" + eventId;
  }

  /**
   * Removes a person to contact from an event
   * 
   * @param eventId the event to update
   * @param personId the person to remove
   * @param redirAttr attributes for the view to display some result information
   * @return the event's detail view
   */
  @RequestMapping("/events/{eid}/personsToContact/remove")
  public String removePersonToContact(@PathVariable("eid") String eventId,
      @RequestParam("person-id") String personId, RedirectAttributes redirAttr) {
    Event event = eventManager.findEvent(eventId).orElseThrow(IllegalArgumentException::new);
    Person person = personManager.findPerson(personId).orElseThrow(IllegalArgumentException::new);

    event.removePersonToContact(person);
    eventManager.updateEvent(eventId, event);

    redirAttr.addFlashAttribute("personToContactRemoved", true);
    return "redirect:/events/" + eventId;
  }

  /**
   * Shows a printable view of a event
   * 
   * @param eventId the event to print
   * @param model model which will contain the data to display
   * @return the print view
   */
  @RequestMapping("/events/{eid}/print")
  public String displayPrintView(@PathVariable("eid") String eventId, Model model) {
    Event event = eventManager.findEvent(eventId).orElseThrow(IllegalArgumentException::new);
    model.addAttribute("event", event);
    return "eventPrint";
  }

  /**
   * Puts a person on the waiting list
   * 
   * @param eventId the event to edit
   * @param personId the person to put on the waiting list
   * @param redirAttr model attributes for the view
   * @return the event's detail view
   */
  @RequestMapping("/events/{eid}/waitingList/add")
  public String addWaitingListEntry(@PathVariable("eid") String eventId,
      @RequestParam("person-id") String personId, RedirectAttributes redirAttr) {
    Event event = eventManager.findEvent(eventId).orElseThrow(IllegalArgumentException::new);
    Person person = personManager.findPerson(personId).orElseThrow(IllegalArgumentException::new);

    try {
      event.putOnWaitingList(person);
      eventManager.updateEvent(eventId, event);
      redirAttr.addFlashAttribute("waitingListEntryAdded", true);
    } catch (ExistingParticipantException e) {
      redirAttr.addFlashAttribute("waitingListEntryParticipates", true);
    } catch (IllegalStateException e) {
      redirAttr.addFlashAttribute("waitingListEntryExists", true);
    }

    return "redirect:/events/" + eventId;
  }

  /**
   * Moves a person from the waiting list to the participants list
   * 
   * @param eventId the event to edit
   * @param applyFirst whether to move the first person on the waiting list
   * @param personId the ID of the person to move, if it is not the first
   * @param redirAttr model attributes for the view
   * @return the event's detail view
   */
  @RequestMapping("/events/{eid}/waitingList/apply")
  public String applyWaitingListEntry(@PathVariable("eid") String eventId,
      @RequestParam(name = "apply-first", defaultValue = "false") boolean applyFirst,
      @RequestParam(name = "person-id", required = false) String personId,
      RedirectAttributes redirAttr) {
    Event event = eventManager.findEvent(eventId).orElseThrow(IllegalArgumentException::new);

    try {
      if (applyFirst) {
        event.applyTopWaitingListSpot();
      } else {
        Person person =
            personManager.findPerson(personId).orElseThrow(IllegalArgumentException::new);
        event.applyWaitingListEntryFor(person);
      }
      eventManager.updateEvent(eventId, event);
      redirAttr.addFlashAttribute("waitingListEntryApplied", true);
    } catch (BookedOutException e) {
      redirAttr.addFlashAttribute("bookedOut", true);
    }

    return "redirect:/events/" + eventId;
  }

  /**
   * Removes a person from the waiting list
   * 
   * @param eventId the event to edit
   * @param personId the ID of the person to remove
   * @param redirAttr model attributes for the view
   * @return the event's detail view
   */
  @RequestMapping("/events/{eid}/waitingList/remove")
  public String removeWaitingListEntry(@PathVariable("eid") String eventId,
      @RequestParam("person-id") String personId, RedirectAttributes redirAttr) {
    Event event = eventManager.findEvent(eventId).orElseThrow(IllegalArgumentException::new);
    Person person = personManager.findPerson(personId).orElseThrow(IllegalArgumentException::new);

    event.removeFromWaitingList(person);
    eventManager.updateEvent(eventId, event);

    redirAttr.addFlashAttribute("waitingListEntryRemoved", true);
    return "redirect:/events/" + eventId;
  }

}
