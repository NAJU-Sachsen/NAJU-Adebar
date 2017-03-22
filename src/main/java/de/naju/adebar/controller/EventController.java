package de.naju.adebar.controller;

import de.naju.adebar.app.human.DataProcessor;
import de.naju.adebar.controller.forms.events.EventForm;
import de.naju.adebar.controller.forms.events.FilterEventsForm;
import de.naju.adebar.model.events.*;
import de.naju.adebar.model.human.Activist;
import de.naju.adebar.model.human.HumanManager;
import de.naju.adebar.model.human.Person;
import de.naju.adebar.util.conversion.PersonConverter;
import de.naju.adebar.util.conversion.events.EventFormDataExtractor;
import de.naju.adebar.util.conversion.events.EventToEventFormConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * @author Rico Bergmann
 */
@Controller
public class EventController {
    private final static String EMAIL_DELIMITER = ";";

    private HumanManager humanManager;
    private EventManager eventManager;
    private EventFormDataExtractor eventFormDataExtractor;
    private EventToEventFormConverter eventToEventFormConverter;
    private PersonConverter personConverter;
    private DataProcessor dataProcessor;

    @Autowired
    public EventController(HumanManager humanManager, EventManager eventManager, EventFormDataExtractor eventFormDataExtractor, EventToEventFormConverter eventToEventFormConverter, PersonConverter personConverter, DataProcessor dataProcessor) {
        Object[] params = {humanManager, eventManager, eventFormDataExtractor, eventToEventFormConverter, personConverter, dataProcessor};
        Assert.noNullElements(params, "No parameter may be null: " + Arrays.toString(params));
        this.humanManager = humanManager;
        this.eventManager = eventManager;
        this.eventFormDataExtractor = eventFormDataExtractor;
        this.eventToEventFormConverter = eventToEventFormConverter;
        this.personConverter = personConverter;
        this.dataProcessor = dataProcessor;
    }

    @RequestMapping("/events")
    public String showEventOverview(Model model) {

        Iterable<Event> currentEvents = eventManager.repository().findByStartTimeIsBeforeAndEndTimeIsAfter(LocalDateTime.now(), LocalDateTime.now());
        Iterable<Event> futureEvents = eventManager.repository().findByStartTimeIsAfter(LocalDateTime.now());

        model.addAttribute("currentEvents", currentEvents);
        model.addAttribute("futureEvents", futureEvents);
        model.addAttribute("addEventForm", new EventForm());
        model.addAttribute("filterEventsForm", new FilterEventsForm());

        return "events";
    }

    @RequestMapping("/events/add")
    public String addEvent(@ModelAttribute("addEventForm") EventForm eventForm, RedirectAttributes redirAttr) {
        Event event = eventManager.saveEvent(eventFormDataExtractor.extractEvent(eventForm));

        return "redirect:/events/" + event.getId();
    }

    @RequestMapping("/events/{eid}")
    public String showEventDetails(@PathVariable("eid") Long eventId, Model model) {
        Event event = eventManager.findEvent(eventId).orElseThrow(IllegalArgumentException::new);
        Iterable<Person> organizers = personConverter.convertActivists(event.getOrganizers());
        Iterable<Person> counselors = personConverter.convertActivists(event.getCounselors());
        Iterable<Person> participants = event.getParticipants();

        model.addAttribute("event", event);
        model.addAttribute("organizers", organizers);
        model.addAttribute("organizerEmails", dataProcessor.extractEmailAddressesAsString(organizers, EMAIL_DELIMITER));

        model.addAttribute("counselors", counselors);
        model.addAttribute("counselorEmails", dataProcessor.extractEmailAddressesAsString(counselors, EMAIL_DELIMITER));

        model.addAttribute("participantEmails", dataProcessor.extractEmailAddressesAsString(participants, EMAIL_DELIMITER));
        model.addAttribute("participantEmailsNoFee", dataProcessor.extractEmailAddressesAsString(event.getParticipantsWithFeeNotPayed(), EMAIL_DELIMITER));
        model.addAttribute("participantEmailsNoForm", dataProcessor.extractEmailAddressesAsString(event.getParticipantsWithFormNotReceived(), EMAIL_DELIMITER));

        model.addAttribute("noOrganizers", !organizers.iterator().hasNext());
        model.addAttribute("noCounselors", !counselors.iterator().hasNext());
        model.addAttribute("noParticipants", !participants.iterator().hasNext());

        model.addAttribute("editEventForm", eventToEventFormConverter.convertToEventForm(event));
        return "eventDetails";
    }

    @RequestMapping("/events/{eid}/update")
    public String updateEvent(@PathVariable("eid") Long eventId, @ModelAttribute("editEventForm") EventForm eventForm, RedirectAttributes redirAttr) {

        eventManager.adoptEventData(eventId, eventFormDataExtractor.extractEvent(eventForm));

        redirAttr.addFlashAttribute("eventUpdated", true);
        return "redirect:/events/" + eventId;
    }

    @RequestMapping("/events/{eid}/participants/add")
    public String addParticipant(@PathVariable("eid") Long eventId, @RequestParam("person-id") String personId, RedirectAttributes redirAttr) {
        Event event = eventManager.findEvent(eventId).orElseThrow(IllegalArgumentException::new);
        Person person = humanManager.findPerson(personId).orElseThrow(IllegalArgumentException::new);

        try {
            event.addParticipant(person);
            eventManager.updateEvent(eventId, event);
            redirAttr.addFlashAttribute("participantAdded", true);
        } catch (ExistingParticipantException e) {
            redirAttr.addFlashAttribute("participatesAlready", true);
        } catch (PersonIsTooYoungException e) {
            redirAttr.addFlashAttribute("participantTooYoung", true);
            redirAttr.addFlashAttribute("newParticipantId", personId);
        }

        return "redirect:/events/" + eventId;
    }

    @RequestMapping("/events/{eid}/participants/force-add")
    public String addParticipantIgnoreAge(@PathVariable("eid") Long eventId, @RequestParam("person-id") String personId, RedirectAttributes redirAttr) {
        Event event = eventManager.findEvent(eventId).orElseThrow(IllegalArgumentException::new);
        Person person = humanManager.findPerson(personId).orElseThrow(IllegalArgumentException::new);

        try {
            event.addParticipantIgnoreAge(person);
            eventManager.updateEvent(eventId, event);
            redirAttr.addFlashAttribute("participantAdded", true);
        } catch (ExistingParticipantException e) {
            redirAttr.addFlashAttribute("participatesAlready", true);
        }

        return "redirect:/events/" + eventId;
    }

    @RequestMapping("/events/{eid}/participants/update")
    public String updateParticipant(@PathVariable("eid") Long eventId, @RequestParam("person-id") String personId, @RequestParam(value = "fee-payed", required = false) boolean feePayed, @RequestParam(value = "form-received", required = false) boolean formReceived) {
        Event event = eventManager.findEvent(eventId).orElseThrow(IllegalArgumentException::new);
        Person person = humanManager.findPerson(personId).orElseThrow(IllegalArgumentException::new);

        ParticipationInfo participationInfo = event.getParticipationInfo(person);
        participationInfo.setParticipationFeePayed(feePayed);
        participationInfo.setRegistrationFormReceived(formReceived);
        event.updateParticipationInfo(person, participationInfo);
        eventManager.updateEvent(eventId, event);

        return "redirect:/events/" + eventId;
    }

    @RequestMapping("/events/{eid}/participants/remove")
    public String removeParticipant(@PathVariable("eid") Long eventId, @RequestParam("person-id") String personId, RedirectAttributes redirAttr) {
        Event event = eventManager.findEvent(eventId).orElseThrow(IllegalArgumentException::new);
        Person person = humanManager.findPerson(personId).orElseThrow(IllegalArgumentException::new);

        event.removeParticipant(person);
        eventManager.updateEvent(eventId, event);

        redirAttr.addFlashAttribute("participantRemoved", true);
        return "redirect:/events/" + eventId;
    }

    @RequestMapping("/events/{eid}/counselors/add")
    public String addCounselor(@PathVariable("eid") Long eventId, @RequestParam("person-id") String personId, RedirectAttributes redirAttr) {
        Event event = eventManager.findEvent(eventId).orElseThrow(IllegalArgumentException::new);
        Activist activist = humanManager.findActivist(humanManager.findPerson(personId).orElseThrow(IllegalArgumentException::new));

        event.addCounselor(activist);
        eventManager.updateEvent(eventId, event);

        redirAttr.addFlashAttribute("counselorAdded", true);
        return "redirect:/events/" + eventId;
    }

    @RequestMapping("/events/{eid}/counselors/remove")
    public String removeCounselor(@PathVariable("eid") Long eventId, @RequestParam("person-id") String personId, RedirectAttributes redirAttr) {
        Event event = eventManager.findEvent(eventId).orElseThrow(IllegalArgumentException::new);
        Activist activist = humanManager.findActivist(humanManager.findPerson(personId).orElseThrow(IllegalArgumentException::new));

        event.removeCounselor(activist);
        eventManager.updateEvent(eventId, event);

        redirAttr.addFlashAttribute("counselorRemoved", true);
        return "redirect:/events/" + eventId;
    }

    @RequestMapping("/events/{eid}/organizers/add")
    public String addOrganizer(@PathVariable("eid") Long eventId, @RequestParam("person-id") String personId, RedirectAttributes redirAttr) {
        Event event = eventManager.findEvent(eventId).orElseThrow(IllegalArgumentException::new);
        Activist activist = humanManager.findActivist(humanManager.findPerson(personId).orElseThrow(IllegalArgumentException::new));

        event.addOrganizer(activist);
        eventManager.updateEvent(eventId, event);

        redirAttr.addFlashAttribute("organizerAdded", true);
        return "redirect:/events/" + eventId;
    }

    @RequestMapping("/events/{eid}/organizers/remove")
    public String removeOrganizer(@PathVariable("eid") Long eventId, @RequestParam("person-id") String personId, RedirectAttributes redirAttr) {
        Event event = eventManager.findEvent(eventId).orElseThrow(IllegalArgumentException::new);
        Activist activist = humanManager.findActivist(humanManager.findPerson(personId).orElseThrow(IllegalArgumentException::new));

        event.removeOrganizer(activist);
        eventManager.updateEvent(eventId, event);

        redirAttr.addFlashAttribute("organizerRemoved", true);
        return "redirect:/events/" + eventId;
    }

}
