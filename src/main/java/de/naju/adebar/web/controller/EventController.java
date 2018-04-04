package de.naju.adebar.web.controller;

import de.naju.adebar.app.events.EventDataProcessor.EventType;
import de.naju.adebar.app.events.filter.EventFilterBuilder;
import de.naju.adebar.model.chapter.LocalGroup;
import de.naju.adebar.model.chapter.Project;
import de.naju.adebar.model.events.BookedOutException;
import de.naju.adebar.model.events.Event;
import de.naju.adebar.model.events.participants.ExistingParticipantException;
import de.naju.adebar.model.events.participants.ParticipationInfo;
import de.naju.adebar.model.events.participants.PersonIsTooYoungException;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.web.validation.events.EventForm;
import de.naju.adebar.web.validation.events.EventForm.Belonging;
import de.naju.adebar.web.validation.events.FilterEventsForm;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

// FIXME: create a ParticipationManager to take care of adding participants, turning persons into participants, etc.

/**
 * Event related controller mappings
 *
 * @author Rico Bergmann
 * @see Event
 */
@Controller
public class EventController {

  private static final String EMAIL_DELIMITER = ";";
  private static final String ADD_EVENT_FORM = "addEventForm";
  private static final String FILTER_EVENTS_FORM = "filterEventsForm";
  private static final String LOCAL_GROUPS = "localGroups";
  private static final String PROJECTS = "projects";
  private static final String EVENTS_VIEW = "events";
  private static final String REDIRECT_EVENTS = "redirect:/events/";

  private final EventControllerManagers managers;
  private final EventControllerDataProcessors dataProcessors;

  @Autowired
  public EventController(EventControllerManagers managers,
      EventControllerDataProcessors dataProcessors) {
    Assert.notNull(managers, "Manager may not be null");
    Assert.notNull(dataProcessors, "Data processor may not be null");
    this.managers = managers;
    this.dataProcessors = dataProcessors;
  }

  /**
   * Displays the event overview
   *
   * @param model model containing the data to display
   * @return the events' overview view
   */
  @RequestMapping("/events")
  public String showEventOverview(Model model) {

    Iterable<Event> currentEvents = managers.events.findOngoingEvents();
    Iterable<Event> futureEvents =
        managers.events.repository().findByStartTimeAfterOrderByStartTime(LocalDateTime.now());

    model.addAttribute("currentEvents", currentEvents);
    model.addAttribute("currentEventsLocalGroups",
        dataProcessors.events.getLocalGroupBelonging(EventType.RUNNING));
    model.addAttribute("currentEventsProjects",
        dataProcessors.events.getProjectBelonging(EventType.RUNNING));

    model.addAttribute("futureEvents", futureEvents);
    model.addAttribute("futureEventsLocalGroups",
        dataProcessors.events.getLocalGroupBelonging(EventType.FUTURE));
    model.addAttribute("futureEventsProjects",
        dataProcessors.events.getProjectBelonging(EventType.FUTURE));

    model.addAttribute(ADD_EVENT_FORM, new EventForm());
    model.addAttribute(FILTER_EVENTS_FORM, new FilterEventsForm());
    model.addAttribute(LOCAL_GROUPS, managers.localGroups.repository().findAll());
    model.addAttribute(PROJECTS, managers.projects.repository().findAll());
    model.addAttribute("normalDisplay", true);

    return EVENTS_VIEW;
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
        managers.events.repository().findByEndTimeBeforeOrderByStartTimeDesc(LocalDateTime.now());

    model.addAttribute("pastEvents", pastEvents);
    model.addAttribute("pastEventsLocalGroups",
        dataProcessors.events.getLocalGroupBelonging(EventType.PAST));
    model.addAttribute("pastEventsProjects",
        dataProcessors.events.getProjectBelonging(EventType.PAST));

    model.addAttribute(ADD_EVENT_FORM, new EventForm());
    model.addAttribute(FILTER_EVENTS_FORM, new FilterEventsForm());
    model.addAttribute(LOCAL_GROUPS, managers.localGroups.repository().findAll());
    model.addAttribute(PROJECTS, managers.projects.repository().findAll());

    return EVENTS_VIEW;
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
  public String filterEvents(@ModelAttribute(FILTER_EVENTS_FORM) FilterEventsForm eventsForm,
      Model model) {
    Stream<Event> events = managers.events.repository().streamAll();
    EventFilterBuilder filterBuilder = new EventFilterBuilder(events);
    dataProcessors.filterEventsExtractor.extractAllFilters(eventsForm)
        .forEach(filterBuilder::applyFilter);

    Iterable<Event> matchingEvents = filterBuilder.filterAndCollect();

    model.addAttribute("filteredEvents", matchingEvents);
    model.addAttribute("filteredEventsLocalGroups",
        dataProcessors.events.getLocalGroupBelonging(matchingEvents));
    model.addAttribute("filteredEventsProjects",
        dataProcessors.events.getProjectBelonging(matchingEvents));

    model.addAttribute(ADD_EVENT_FORM, new EventForm());
    model.addAttribute(FILTER_EVENTS_FORM, new FilterEventsForm());
    model.addAttribute(LOCAL_GROUPS, managers.localGroups.repository().findAll());
    model.addAttribute(PROJECTS, managers.projects.repository().findAll());
    return EVENTS_VIEW;
  }

  /**
   * Adds a new event to the database
   *
   * @param eventForm the submitted event data
   * @return the event's detail view
   */
  @RequestMapping("/events/add")
  public String addEvent(@ModelAttribute(ADD_EVENT_FORM) EventForm eventForm) {
    Event event = managers.events.saveEvent(dataProcessors.eventExtractor.extractEvent(eventForm));

    if (dataProcessors.eventExtractor.extractBelonging(eventForm) == Belonging.PROJECT) {
      Project project = managers.projects.findProject(eventForm.getProjectId())
          .orElseThrow(IllegalArgumentException::new);
      project.addEvent(event);
      managers.projects.updateProject(project.getId(), project);
    } else if (dataProcessors.eventExtractor.extractBelonging(eventForm) == Belonging.LOCALGROUP) {
      LocalGroup localGroup = managers.localGroups.findLocalGroup(eventForm.getLocalGroupId())
          .orElseThrow(IllegalArgumentException::new);
      localGroup.addEvent(event);
      managers.localGroups.updateLocalGroup(localGroup.getId(), localGroup);
    }

    return REDIRECT_EVENTS + event.getId();
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
    Event event = managers.events.findEvent(eventId).orElseThrow(IllegalArgumentException::new);
    Iterable<Person> organizers = event.getOrganizers();
    Iterable<Person> counselors = event.getCounselors();
    Iterable<Person> participants = event.getParticipants();

    model.addAttribute("event", event);
    model.addAttribute("organizers", organizers);
    model.addAttribute("organizerEmails",
        dataProcessors.persons.extractEmailAddressesAsString(organizers, EMAIL_DELIMITER));

    model.addAttribute("counselors", counselors);
    model.addAttribute("counselorEmails",
        dataProcessors.persons.extractEmailAddressesAsString(counselors, EMAIL_DELIMITER));

    model.addAttribute("participantEmails",
        dataProcessors.persons.extractEmailAddressesAsString(participants, EMAIL_DELIMITER));
    model.addAttribute("participantEmailsNoFee", dataProcessors.persons
        .extractEmailAddressesAsString(event.getParticipantsWithFeeNotPayed(), EMAIL_DELIMITER));
    model.addAttribute("participantEmailsNoForm",
        dataProcessors.persons.extractEmailAddressesAsString(
            event.getParticipantsWithFormNotReceived(), EMAIL_DELIMITER));

    model.addAttribute("noOrganizers", !organizers.iterator().hasNext());
    model.addAttribute("noCounselors", !counselors.iterator().hasNext());
    model.addAttribute("noParticipants", !participants.iterator().hasNext());

    model.addAttribute("editEventForm", dataProcessors.eventConverter.convertToEventForm(event));
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

    managers.events.adoptEventData(eventId, dataProcessors.eventExtractor.extractEvent(eventForm));

    redirAttr.addFlashAttribute("eventUpdated", true);
    return REDIRECT_EVENTS + eventId;
  }

  /**
   * Adds a participant to an event. This may actually not be exectuted if the person to add is too
   * young.
   *
   * @param eventId the id of the event to add the participant to
   * @param personIds the ids of the persons to add
   * @param redirAttr attributes for the view to display some result information
   * @return the event's detail view
   */
  @RequestMapping("/events/{eid}/participants/add")
  public String addParticipant(@PathVariable("eid") String eventId,
      @RequestParam("person-id") List<String> personIds, RedirectAttributes redirAttr) {
    Event event = managers.events.findEvent(eventId).orElseThrow(IllegalArgumentException::new);

    if (personIds.isEmpty()) {
      return REDIRECT_EVENTS + eventId;
    }

    ArrayList<Person> addedParticipants = new ArrayList<>(personIds.size());
    ArrayList<Person> existingParticipants = new ArrayList<>(personIds.size());
    ArrayList<Person> tooYoungPersons = new ArrayList<>(personIds.size());
    ArrayList<Person> bookedOutPersons = new ArrayList<>(personIds.size());

    for (String id : personIds) {
      Person person = managers.persons.findPerson(id).orElseThrow(IllegalArgumentException::new);

      try {
        if (!person.isParticipant()) {
          person.makeParticipant();
          managers.persons.savePerson(person);
        }

        event.addParticipant(person);
        managers.events.updateEvent(eventId, event);
        addedParticipants.add(person);
      } catch (ExistingParticipantException e) {
        existingParticipants.add(person);
      } catch (PersonIsTooYoungException e) {
        tooYoungPersons.add(person);
      } catch (BookedOutException e) {
        bookedOutPersons.add(person);
      }
    }

    addedParticipants.trimToSize();
    existingParticipants.trimToSize();
    tooYoungPersons.trimToSize();
    bookedOutPersons.trimToSize();

    if (!addedParticipants.isEmpty()) {
      redirAttr.addFlashAttribute("participantsAdded", addedParticipants);
    }
    if (!existingParticipants.isEmpty()) {
      redirAttr.addFlashAttribute("participatesAlready", existingParticipants);
    }
    if (!tooYoungPersons.isEmpty()) {
      redirAttr.addFlashAttribute("participantTooYoung", tooYoungPersons);
    }
    if (!bookedOutPersons.isEmpty()) {
      redirAttr.addFlashAttribute("bookedOut", bookedOutPersons);
    }

    return REDIRECT_EVENTS + eventId;
  }

  /**
   * Adds a participant to an event regardless of his age
   *
   * @param eventId the id of the event to add the participant to
   * @param personIds the ids of the persons to add
   * @param redirAttr attributes for the view to display some result information
   * @return the event's detail view
   */
  @RequestMapping("/events/{eid}/participants/force-add")
  public String addParticipantIgnoreAge(@PathVariable("eid") String eventId,
      @RequestParam("person-id") List<String> personIds, RedirectAttributes redirAttr) {
    Event event = managers.events.findEvent(eventId).orElseThrow(IllegalArgumentException::new);

    if (personIds.isEmpty()) {
      return REDIRECT_EVENTS + eventId;
    }

    ArrayList<Person> addedParticipants = new ArrayList<>(personIds.size());
    ArrayList<Person> existingParticipants = new ArrayList<>(personIds.size());

    for (String personId : personIds) {
      Person person =
          managers.persons.findPerson(personId).orElseThrow(IllegalArgumentException::new);

      try {
        if (!person.isParticipant()) {
          person.makeParticipant();
          managers.persons.savePerson(person);
        }

        event.addParticipantIgnoreAge(person);
        managers.events.updateEvent(eventId, event);
        addedParticipants.add(person);
      } catch (ExistingParticipantException e) {
        existingParticipants.add(person);
      }
    }

    addedParticipants.trimToSize();
    existingParticipants.trimToSize();

    if (!addedParticipants.isEmpty()) {
      redirAttr.addFlashAttribute("participantsAdded", addedParticipants);
    }
    if (!existingParticipants.isEmpty()) {
      redirAttr.addFlashAttribute("participatesAlready", existingParticipants);
    }

    return REDIRECT_EVENTS + eventId;
  }

  /**
   * Updates the participation information about an participant
   *
   * @param eventId the id of the event the person participates in
   * @param personId the id of the person whose information should be updated
   * @param feePayed whether the participation fee was payed
   * @param formReceived whether the legally binding participation form was already sent from
   *     the person
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
    Event event = managers.events.findEvent(eventId).orElseThrow(IllegalArgumentException::new);
    Person person =
        managers.persons.findPerson(personId).orElseThrow(IllegalArgumentException::new);

    ParticipationInfo participationInfo = event.getParticipationInfo(person);
    participationInfo.setParticipationFeePayed(feePayed);
    participationInfo.setRegistrationFormFilled(formReceived);
    participationInfo.setRemarks(remarks);
    event.updateParticipationInfo(person, participationInfo);
    managers.events.updateEvent(eventId, event);

    redirAttr.addFlashAttribute("participationInfoUpdated", true);
    return REDIRECT_EVENTS + eventId;
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
    Event event = managers.events.findEvent(eventId).orElseThrow(IllegalArgumentException::new);
    Person person =
        managers.persons.findPerson(personId).orElseThrow(IllegalArgumentException::new);

    event.removeParticipant(person);
    managers.events.updateEvent(eventId, event);

    redirAttr.addFlashAttribute("participantRemoved", true);
    return REDIRECT_EVENTS + eventId;
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
    Event event = managers.events.findEvent(eventId).orElseThrow(IllegalArgumentException::new);
    Person activist =
        managers.persons.findPerson(personId).orElseThrow(IllegalArgumentException::new);

    try {
      event.addCounselor(activist);
      managers.events.updateEvent(eventId, event);
      redirAttr.addFlashAttribute("counselorAdded", true);
    } catch (IllegalArgumentException e) {
      redirAttr.addFlashAttribute("existingCounselor", true);
    }

    return REDIRECT_EVENTS + eventId;
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
    Event event = managers.events.findEvent(eventId).orElseThrow(IllegalArgumentException::new);
    Person activist =
        managers.persons.findPerson(personId).orElseThrow(IllegalArgumentException::new);

    event.removeCounselor(activist);
    managers.events.updateEvent(eventId, event);

    redirAttr.addFlashAttribute("counselorRemoved", true);
    return REDIRECT_EVENTS + eventId;
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
    Event event = managers.events.findEvent(eventId).orElseThrow(IllegalArgumentException::new);
    Person person =
        managers.persons.findPerson(personId).orElseThrow(IllegalArgumentException::new);

    try {
      person.makeActivist();
      event.addCounselor(person);
      managers.persons.updatePerson(person);
      managers.events.updateEvent(eventId, event);
      redirAttr.addFlashAttribute("counselorAdded", true);
    } catch (IllegalStateException e) {
      // the person is already an activist
      redirAttr.addFlashAttribute("existingActivist", true);

    } catch (IllegalArgumentException e) {
      // the person is already an organizer
      redirAttr.addFlashAttribute("existingCounselor", true);
    }
    return REDIRECT_EVENTS + eventId;
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
    Event event = managers.events.findEvent(eventId).orElseThrow(IllegalArgumentException::new);
    Person activist =
        managers.persons.findPerson(personId).orElseThrow(IllegalArgumentException::new);

    try {
      event.addOrganizer(activist);
      managers.events.updateEvent(eventId, event);
      redirAttr.addFlashAttribute("organizerAdded", true);
    } catch (IllegalArgumentException e) {
      redirAttr.addFlashAttribute("existingOrganizer", true);
    }

    return REDIRECT_EVENTS + eventId;
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
    Event event = managers.events.findEvent(eventId).orElseThrow(IllegalArgumentException::new);
    Person activist =
        managers.persons.findPerson(personId).orElseThrow(IllegalArgumentException::new);

    event.removeOrganizer(activist);
    managers.events.updateEvent(eventId, event);

    redirAttr.addFlashAttribute("organizerRemoved", true);
    return REDIRECT_EVENTS + eventId;
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
    Event event = managers.events.findEvent(eventId).orElseThrow(IllegalArgumentException::new);
    Person person =
        managers.persons.findPerson(personId).orElseThrow(IllegalArgumentException::new);

    try {
      person.makeActivist();
      event.addOrganizer(person);
      managers.persons.updatePerson(person);
      managers.events.updateEvent(eventId, event);
      redirAttr.addFlashAttribute("organizerAdded", true);
    } catch (IllegalStateException e) {
      // the person is already an activist
      redirAttr.addFlashAttribute("existingActivist", true);

    } catch (IllegalArgumentException e) {
      // the person is already an organizer
      redirAttr.addFlashAttribute("existingOrganizer", true);
    }
    return REDIRECT_EVENTS + eventId;
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
    Event event = managers.events.findEvent(eventId).orElseThrow(IllegalArgumentException::new);
    Person person =
        managers.persons.findPerson(personId).orElseThrow(IllegalArgumentException::new);

    event.addPersonToContact(person, remarks);
    managers.events.updateEvent(eventId, event);

    redirAttr.addFlashAttribute("personToContactAdded", true);
    return REDIRECT_EVENTS + eventId;
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
    Event event = managers.events.findEvent(eventId).orElseThrow(IllegalArgumentException::new);
    Person person =
        managers.persons.findPerson(personId).orElseThrow(IllegalArgumentException::new);

    event.updatePersonToContact(person, remarks);
    managers.events.updateEvent(eventId, event);

    redirAttr.addFlashAttribute("personToContactUpdated", true);
    return REDIRECT_EVENTS + eventId;
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
    Event event = managers.events.findEvent(eventId).orElseThrow(IllegalArgumentException::new);
    Person person =
        managers.persons.findPerson(personId).orElseThrow(IllegalArgumentException::new);

    event.removePersonToContact(person);
    managers.events.updateEvent(eventId, event);

    redirAttr.addFlashAttribute("personToContactRemoved", true);
    return REDIRECT_EVENTS + eventId;
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
    Event event = managers.events.findEvent(eventId).orElseThrow(IllegalArgumentException::new);
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
    Event event = managers.events.findEvent(eventId).orElseThrow(IllegalArgumentException::new);
    Person person =
        managers.persons.findPerson(personId).orElseThrow(IllegalArgumentException::new);

    try {
      event.putOnWaitingList(person);
      managers.events.updateEvent(eventId, event);
      redirAttr.addFlashAttribute("waitingListEntryAdded", true);
    } catch (ExistingParticipantException e) {
      redirAttr.addFlashAttribute("waitingListEntryParticipates", true);
    } catch (IllegalStateException e) {
      redirAttr.addFlashAttribute("waitingListEntryExists", true);
    }

    return REDIRECT_EVENTS + eventId;
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
    Event event = managers.events.findEvent(eventId).orElseThrow(IllegalArgumentException::new);

    try {
      if (applyFirst) {
        event.applyTopWaitingListSpot();
      } else {
        Person person =
            managers.persons.findPerson(personId).orElseThrow(IllegalArgumentException::new);
        event.applyWaitingListEntryFor(person);
      }
      managers.events.updateEvent(eventId, event);
      redirAttr.addFlashAttribute("waitingListEntryApplied", true);
    } catch (BookedOutException e) {
      redirAttr.addFlashAttribute("bookedOut", true);
    }

    return REDIRECT_EVENTS + eventId;
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
    Event event = managers.events.findEvent(eventId).orElseThrow(IllegalArgumentException::new);
    Person person =
        managers.persons.findPerson(personId).orElseThrow(IllegalArgumentException::new);

    event.removeFromWaitingList(person);
    managers.events.updateEvent(eventId, event);

    redirAttr.addFlashAttribute("waitingListEntryRemoved", true);
    return REDIRECT_EVENTS + eventId;
  }

}
