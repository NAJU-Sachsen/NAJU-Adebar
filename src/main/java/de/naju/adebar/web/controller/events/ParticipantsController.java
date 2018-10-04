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
import de.naju.adebar.web.model.events.ParticipantsTable;
import de.naju.adebar.web.model.events.ParticipantsTableFormattingService;
import de.naju.adebar.web.model.events.participation.ParticipationReport;
import de.naju.adebar.web.validation.events.participation.AddParticipantsForm;
import de.naju.adebar.web.validation.events.participation.AddParticipantsForm.AddParticipantForm;
import de.naju.adebar.web.validation.events.participation.ParticipantsTableForm;
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

/**
 * Handles all requests to the participant's overview of an event.
 *
 * @author Rico Bergmann
 */
@Controller
public class ParticipantsController {

  private static final Logger log = LoggerFactory.getLogger(ParticipantsController.class);

  private final ParticipationManager participationManager;
  private final PersonSearchServer searchServer;
  private final EventDataProcessingService eventDataProcessingService;
  private final ParticipantsTableFormattingService participantsTableFormattingService;

  /**
   * Full constructor. No parameter may be {@code null}.
   *
   * @param participationManager service for the actual business logic involved with managing
   *     participants
   * @param searchServer service to search for persons based on queries
   * @param eventDataProcessingService service to conveniently extract data from {@link Event
   *     Events}
   * @param participantsTableFormattingService service to render the contents of the
   *     participants table
   */
  public ParticipantsController(ParticipationManager participationManager,
      PersonSearchServer searchServer, EventDataProcessingService eventDataProcessingService,
      ParticipantsTableFormattingService participantsTableFormattingService) {
    Assert2.noNullArguments("No argument may be null", participationManager, searchServer,
        eventDataProcessingService, participantsTableFormattingService);
    this.participationManager = participationManager;
    this.searchServer = searchServer;
    this.eventDataProcessingService = eventDataProcessingService;
    this.participantsTableFormattingService = participantsTableFormattingService;
  }

  /**
   * Renders the participant's overview for a specific event.
   *
   * @param event the event
   * @param model data model to use for the template
   * @return the event details template. It will include the appropriate fragments.
   */
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

  /**
   * Renders the view to customize the columns in the participants table.
   *
   * @param event the event whose participants table should be edited
   * @param columnSelection whether the column selection dialog should be shown
   * @param model data model to use for the template
   * @return the event details template. It will include the appropriate fragments.
   */
  @GetMapping("/events/{id}/participants/table")
  public String showCustomizeParticipantsTable(@PathVariable("id") Event event,
      @RequestParam(name = "columnSelection", defaultValue = "true") boolean columnSelection,
      Model model) {

    model.addAttribute("tab", "participantsTable");
    model.addAttribute("showTable", false);
    model.addAttribute("event", event);
    model.addAttribute("processingService", eventDataProcessingService);

    if (!model.containsAttribute("participantsTableForm")) {
      model.addAttribute("participantsTableForm", new ParticipantsTableForm());
    }

    return "events/eventDetails";
  }

  /**
   * Adapts the participants table.
   *
   * @param event the event whose participants table will be edited
   * @param form form containing which columns should be shown
   * @param model data model to for the resulting template
   * @return the event details template. It will include the appropriate fragments.
   */
  @PostMapping("/events/{id}/participants/table")
  public String customizeParticipantsTable(@PathVariable("id") Event event,
      @ModelAttribute("participantsTable") ParticipantsTableForm form, Model model) {

    ParticipantsTable table = form.toParticipantsTable().forEvent(event);
    model.addAttribute("participantsTable", table);
    model.addAttribute("participantsTableForm", form);
    model.addAttribute("formattingService", participantsTableFormattingService);

    model.addAttribute("tab", "participantsTable");
    model.addAttribute("showTable", true);
    model.addAttribute("event", event);
    model.addAttribute("processingService", eventDataProcessingService);

    return "events/eventDetails";
  }

  /**
   * Searches for persons based on some (natural language) query. A redirection to the participants
   * view will be performed afterwards.
   *
   * @param event the event for which the search is performed. This is necessary to redirect to
   *     the correct page after the search returned.
   * @param form the current data entered in the add participants dialog
   * @param query the search query. May be a name, email address or the like.
   * @param returnAction action to perform after the search finished
   * @param redirAttr attributes for the model and URL to use after redirection
   * @return a redirection to the participants view of the event
   */
  @PostMapping("/events/{id}/participants/search")
  public String searchParticipants(@PathVariable("id") Event event,
      @ModelAttribute("addParticipants") AddParticipantsForm form,
      @RequestParam("person-search-query") String query,
      @RequestParam(value = "return-action", defaultValue = "") String returnAction,
      RedirectAttributes redirAttr) {

    redirAttr.addFlashAttribute("matchingPersons", searchServer.runQuery(query.trim()));

    redirAttr.addAttribute("search", query);
    redirAttr.addAttribute("do", returnAction);
    redirAttr.addFlashAttribute("addParticipants", form.prepareForm());

    return "redirect:/events/" + event.getId() + "/participants";
  }

  // TODO rework the 'add participant' workflow to make it less itchy
  /*
    Right now the whole workflow is a bit hacky, maybe a framework such as Spring Webflow may help
    here.
    Hacky in this case means an unintuitive behaviour and ugly design of the AddParticipantsForm
    as well as a complicated display in the template.
   */

  /**
   * Enqueues a person to be added as participant, but enables still adding more persons.
   * <p>
   * The participant will not be saved yet, this action will be performed after the last person has
   * been added.
   *
   * @param event the event the participant wants to attend.
   * @param form the form containing the new participants
   * @param returnAction action to perform after the person was enqueued
   * @param returnTab the tab to show after the person was enqueued. Currently unused.
   * @param redirAttr attributes for the model and URL to use after redirection
   * @return the participants overview of the event. A dialog to add more participants will be
   *     shown.
   */
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

  /**
   * Adds new participants to an event.
   * <p>
   * If some participants may not be added, a detailed list displaying the reason for failure will
   * be provided. There are just two possible reasons for why a person could not become a
   * participant: either because he/she is too young, or because there were no more slots available
   * (during the requested time). In the first case the user may choose to add the participant
   * anyway.
   *
   * @param event the event to add the participants to
   * @param form form containing the participants' data
   * @param redirAttr attributes for the model and URL to use after redirection
   * @return a redirection to the event's participants overview (a.k.a. participants table)
   * @see #addParticipantsIgnoreAge(Event, AddParticipantsForm, RedirectAttributes)
   */
  @PostMapping("/events/{id}/participants/add/done")
  @Transactional
  public String addParticipants(@PathVariable("id") Event event,
      @ModelAttribute("addParticipants") AddParticipantsForm form, RedirectAttributes redirAttr) {
    return doAddParticipants(event, form, redirAttr, false);
  }

  /**
   * Adds new participants to an event, ignoring the minimum participant age required by the event.
   *
   * @param event the event to add the participants to
   * @param form form containing the participant's data
   * @param redirAttr attributes for the model and URL to use after redirection
   * @return a redirection to the event's participants table
   */
  @PostMapping("/events/{id}/participants/add/ignore-age")
  @Transactional
  public String addParticipantsIgnoreAge(@PathVariable("id") Event event,
      @ModelAttribute("addParticipants") AddParticipantsForm form, RedirectAttributes redirAttr) {
    return doAddParticipants(event, form, redirAttr, true);
  }

  /**
   * Updates the registration of a participant.
   * <p>
   * This update will involve invoking the manager checking, whether the person may still
   * participate (if the participation time changed). Therefore an update may easily fail.
   *
   * @param event the event for which the participant should be updated
   * @param newInfo the updated registration info. This contains the participant as well.
   * @param redirAttr attributes for the model and URL to use after redirection
   * @return a redirection to the event's participants table
   */
  @PostMapping("/events/{id}/participants/update")
  @Transactional
  public String updateParticipationInfo(@PathVariable("id") Event event,
      @ModelAttribute("editParticipantForm") AddParticipantForm newInfo,
      RedirectAttributes redirAttr) {

    RegistrationInfoBuilder registrationInfoBuilder = newInfo.prepareRegistrationInfo();

    if (event.getParticipationInfo().supportsFlexibleParticipationTimes()) {

      registrationInfoBuilder
          .withParticipationDuring(new ParticipationTime(newInfo.generateFirstNightAsLDT(),
              newInfo.generateLastNightAsLDT(), event.getStartTime()));
    }

    RegistrationInfo registrationInfo = registrationInfoBuilder.build();

    Result result =
        participationManager.updateParticipation(event, newInfo.getParticipant(), registrationInfo);

    if (result != Result.OK) {
      redirAttr.addAttribute("update-participation", "failed");
    }

    return "redirect:/events/" + event.getId() + "/participants";
  }

  /**
   * Deletes a participant from an event.
   *
   * @param event the event to remove the participant from
   * @param participant the participant to remove
   * @return a redirection to the event's participants table
   */
  @PostMapping("/events/{id}/participants/remove")
  public String removeParticipant(@PathVariable("id") Event event,
      @RequestParam("participant") Person participant) {
    participationManager.removeParticipant(event, participant);
    return "redirect:/events/" + event.getId() + "/participants";
  }

  /**
   * Performs the actual addition of participants to some event.
   *
   * @param event the event
   * @param form the form containing the participants to add
   * @param redirAttr attributes for the model and URL to use after redirection
   * @param ignoreAgeRestrictions whether the minimum participant age of the event should be
   *     respected
   * @return a redirection to the event's participants table
   */
  private String doAddParticipants(Event event, AddParticipantsForm form,
      RedirectAttributes redirAttr, boolean ignoreAgeRestrictions) {

    ParticipationReport report = new ParticipationReport();

    for (AddParticipantForm newParticipant : form.getParticipants()) {
      RegistrationInfoBuilder registrationInfoBuilder = newParticipant.prepareRegistrationInfo();

      if (event.getParticipationInfo().supportsFlexibleParticipationTimes()) {

        registrationInfoBuilder
            .withParticipationDuring(new ParticipationTime(newParticipant.generateFirstNightAsLDT(),
                newParticipant.generateLastNightAsLDT(), event.getStartTime()));
      }

      RegistrationInfo registrationInfo = registrationInfoBuilder.build();
      Result result = participationManager.addParticipant(event, newParticipant.getParticipant(),
          registrationInfo, ignoreAgeRestrictions);

      report.appendEntry(newParticipant, result);
    }

    log.info("Tried to add participants with report {}", report);

    redirAttr.addAttribute("add-participants", "finished");
    redirAttr.addFlashAttribute("participationReport", report);

    return "redirect:/events/" + event.getId() + "/participants";

  }

}
