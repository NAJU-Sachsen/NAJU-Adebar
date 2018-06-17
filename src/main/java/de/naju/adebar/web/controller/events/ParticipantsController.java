package de.naju.adebar.web.controller.events;

import de.naju.adebar.app.events.processing.EventDataProcessingService;
import de.naju.adebar.app.persons.search.PersonSearchServer;
import de.naju.adebar.model.events.Event;
import de.naju.adebar.model.events.ParticipationManager;
import de.naju.adebar.model.events.ParticipationManager.Result;
import de.naju.adebar.model.events.RegistrationInfo;
import de.naju.adebar.model.events.RegistrationInfo.RegistrationInfoBuilder;
import de.naju.adebar.model.events.rooms.scheduling.ParticipationTime;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.util.Assert2;
import de.naju.adebar.web.model.events.participation.ParticipationReport;
import de.naju.adebar.web.validation.events.participation.AddParticipantsForm;
import de.naju.adebar.web.validation.events.participation.AddParticipantsForm.AddParticipantForm;
import de.naju.adebar.web.validation.events.participation.ReservationForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ParticipantsController {

  private static final Logger log = LoggerFactory.getLogger(ParticipantsController.class);

  private final ParticipationManager participationManager;
  private final PersonSearchServer searchServer;
  private final EventDataProcessingService eventDataProcessingService;

  public ParticipantsController(ParticipationManager participationManager,
      PersonSearchServer searchServer, EventDataProcessingService eventDataProcessingService) {
    Assert2.noNullArguments("No argument may be null",
        participationManager, searchServer, eventDataProcessingService);
    this.participationManager = participationManager;
    this.searchServer = searchServer;
    this.eventDataProcessingService = eventDataProcessingService;
  }

  @GetMapping("/events/{id}/participants")
  public String showEventParticipants(@PathVariable("id") Event event, Model model) {

    model.addAttribute("tab", "participants");
    model.addAttribute("event", event);
    model.addAttribute("processingService", eventDataProcessingService);

    model.addAttribute("editParticipantForm", new AddParticipantForm(event));
    model.addAttribute("addReservationForm", new ReservationForm());
    model.addAttribute("editReservationForm", new ReservationForm());

    if (!model.containsAttribute("addParticipants")) {
      model.addAttribute("addParticipants", new AddParticipantsForm(event));
    }

    if (event.getParticipantsList().hasWaitingList()) {
      model.addAttribute("applyWaitingListEntryForm", new AddParticipantForm(event));
    }

    return "events/eventDetails";
  }

  @PostMapping("/events/{id}/participants/search")
  public String searchParticipants(@PathVariable("id") Event event,
      @ModelAttribute("addParticipants") AddParticipantsForm form,
      @RequestParam("person-search-query") String query,
      @RequestParam(value = "return-action", defaultValue = "") String returnAction,
      RedirectAttributes redirAttr) {

    redirAttr.addFlashAttribute("matchingPersons",
        searchServer.runQuery(query.trim()));

    redirAttr.addAttribute("search", query);
    redirAttr.addAttribute("do", returnAction);
    redirAttr.addFlashAttribute("addParticipants", form.prepareForm());

    return "redirect:/events/" + event.getId() + "/participants";
  }

  @PostMapping("/events/{id}/participants/add/continue")
  public String addMultipleParticipants(@PathVariable("id") Event event,
      @ModelAttribute("addParticipants") AddParticipantsForm form,
      @RequestParam(value = "return-action", defaultValue = "") String returnAction,
      @RequestParam(value = "return-tab", defaultValue = "") String returnTab,
      RedirectAttributes redirAttr) {

    redirAttr.addAttribute("do", returnAction);
    redirAttr.addFlashAttribute("addParticipants", form.prepareForm());

    return "redirect:/events/" + event.getId() + "/participants";
  }

  @PostMapping("/events/{id}/participants/add/done")
  @Transactional
  public String addParticipants(@PathVariable("id") Event event,
      @ModelAttribute("addParticipants") AddParticipantsForm form, RedirectAttributes redirAttr) {
    return doAddParticipants(event, form, redirAttr, false);
  }

  @PostMapping("/events/{id}/participants/add/ignore-age")
  @Transactional
  public String addParticipantsIgnoreAge(@PathVariable("id") Event event,
      @ModelAttribute("addParticipants") AddParticipantsForm form,
      RedirectAttributes redirAttr) {
    return doAddParticipants(event, form, redirAttr, true);
  }

  @PostMapping("/events/{id}/participants/update")
  @Transactional
  public String updateParticipationInfo(@PathVariable("id") Event event,
      @ModelAttribute("editParticipantForm") AddParticipantForm newInfo,
      RedirectAttributes redirAttr) {

    RegistrationInfoBuilder registrationInfoBuilder = newInfo.prepareRegistrationInfo();

    if (event.getParticipationInfo().supportsFlexibleParticipationTimes()) {

      registrationInfoBuilder.withParticipationDuring(
          new ParticipationTime(newInfo.generateFirstNightAsLDT(),
              newInfo.generateLastNightAsLDT(), event.getStartTime()));
    }

    RegistrationInfo registrationInfo = registrationInfoBuilder.build();

    Result result = participationManager
        .updateParticipation(event, newInfo.getParticipant(), registrationInfo);

    if (result != Result.OK) {
      redirAttr.addAttribute("update-participation", "failed");
    }

    return "redirect:/events/" + event.getId() + "/participants";
  }

  @PostMapping("/events/{id}/participants/remove")
  public String removeParticipant(@PathVariable("id") Event event,
      @RequestParam("participant") Person participant) {
    participationManager.removeParticipant(event, participant);
    return "redirect:/events/" + event.getId() + "/participants";
  }

  private String doAddParticipants(Event event, AddParticipantsForm form,
      RedirectAttributes redirAttr, boolean ignoreAgeRestrictions) {

    ParticipationReport report = new ParticipationReport();

    for (AddParticipantForm newParticipant : form.getParticipants()) {
      RegistrationInfoBuilder registrationInfoBuilder = newParticipant.prepareRegistrationInfo();

      if (event.getParticipationInfo().supportsFlexibleParticipationTimes()) {

        registrationInfoBuilder.withParticipationDuring(
            new ParticipationTime(newParticipant.generateFirstNightAsLDT(),
                newParticipant.generateLastNightAsLDT(), event.getStartTime()));
      }

      RegistrationInfo registrationInfo = registrationInfoBuilder.build();
      Result result = participationManager
          .addParticipant(event, newParticipant.getParticipant(), registrationInfo,
              ignoreAgeRestrictions);

      report.appendEntry(newParticipant, result);
    }

    log.info("Tried to add participants with report {}", report);

    redirAttr.addAttribute("add-participants", "finished");
    redirAttr.addFlashAttribute("participationReport", report);

    return "redirect:/events/" + event.getId() + "/participants";

  }

}
