package de.naju.adebar.web.controller.events;

import de.naju.adebar.app.events.processing.EventDataProcessingService;
import de.naju.adebar.app.persons.search.PersonSearchServer;
import de.naju.adebar.model.events.CounselorInfo.CounselorInfoBuilder;
import de.naju.adebar.model.events.Event;
import de.naju.adebar.model.events.ParticipationManager;
import de.naju.adebar.model.events.ParticipationManager.Result;
import de.naju.adebar.model.events.rooms.scheduling.ParticipationTime;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.util.Assert2;
import de.naju.adebar.web.validation.events.organization.CounselorForm;
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
public class OrganizationController {

  private final ParticipationManager participationManager;
  private final PersonSearchServer searchServer;
  private final EventDataProcessingService eventDataProcessingService;

  public OrganizationController(ParticipationManager participationManager,
      PersonSearchServer searchServer,
      EventDataProcessingService eventDataProcessingService) {
    Assert2.noNullArguments("No argument may be null", participationManager, searchServer,
        eventDataProcessingService);
    this.participationManager = participationManager;
    this.searchServer = searchServer;
    this.eventDataProcessingService = eventDataProcessingService;
  }

  @GetMapping("/events/{id}/organization")
  public String showOrganization(@PathVariable("id") Event event, Model model) {

    model.addAttribute("tab", "organization");
    model.addAttribute("event", event);
    model.addAttribute("addCounselor", new CounselorForm(event));
    model.addAttribute("editCounselor", new CounselorForm(event));
    model.addAttribute("processingService", eventDataProcessingService);

    return "events/eventDetails";
  }

  @GetMapping("/events/{id}/organizers/search")
  public String searchOrganizer(@PathVariable("id") Event event,
      @RequestParam("person-search-query") String query,
      @RequestParam(value = "return-action", defaultValue = "") String returnAction,
      RedirectAttributes redirAttr) {

    redirAttr.addFlashAttribute("matchingPersons",
        searchServer.runQuery(query.trim()));

    redirAttr.addAttribute("search", query);
    redirAttr.addAttribute("do", returnAction);

    return "redirect:/events/" + event.getId() + "/organization";
  }

  @PostMapping("/events/{id}/organizers/add")
  @Transactional
  public String addOrganizer(@PathVariable("id") Event event,
      @RequestParam("person-id") Person person) {

    if (!person.isActivist()) {
      person.makeActivist();
    }

    event.getOrganizationInfo().addOrganizer(person);

    return "redirect:/events/" + event.getId() + "/organization";
  }

  @PostMapping("/events/{id}/organizers/remove")
  @Transactional
  public String removeOrganizer(@PathVariable("id") Event event,
      @RequestParam("person-id") Person organizer) {
    event.getOrganizationInfo().removeOrganizer(organizer);
    return "redirect:/events/" + event.getId() + "/organization";
  }

  @PostMapping("/events/{id}/counselors/search")
  public String searchCounselors(@PathVariable("id") Event event,
      @ModelAttribute("addCounselor") CounselorForm form,
      @RequestParam("person-search-query") String query,
      @RequestParam(value = "return-action", defaultValue = "") String returnAction,
      RedirectAttributes redirAttr) {

    redirAttr.addFlashAttribute("matchingPersons",
        searchServer.runQuery(query.trim()));

    redirAttr.addAttribute("do", returnAction);
    redirAttr.addAttribute("search", query);

    return "redirect:/events/" + event.getId() + "/organization";
  }

  @PostMapping("/events/{id}/counselors/add")
  @Transactional
  public String addCounselor(@PathVariable("id") Event event,
      @ModelAttribute("addCounselor") CounselorForm form, RedirectAttributes redirAttr) {

    CounselorInfoBuilder counselorInfoBuilder = form.prepareCounselorInfo();

    if (event.getParticipationInfo().supportsFlexibleParticipationTimes()) {
      counselorInfoBuilder.withParticipationDuring(
          new ParticipationTime(form.generateFirstNightAsLDT(), form.generateLastNightAsLDT(),
              event.getStartTime()));
    }

    if (participationManager.addCounselor(event, form.getCounselor(), counselorInfoBuilder.build())
        != Result.OK) {
      redirAttr.addAttribute("add-counselor", "failed");
    }

    return "redirect:/events/" + event.getId() + "/organization";
  }

  @PostMapping("/events/{id}/counselors/update")
  @Transactional
  public String updateCounselor(@PathVariable("id") Event event,
      @ModelAttribute("addCounselor") CounselorForm form) {

    CounselorInfoBuilder counselorInfoBuilder = form.prepareCounselorInfo();

    if (event.getParticipationInfo().supportsFlexibleParticipationTimes()) {
      counselorInfoBuilder.withParticipationDuring(
          new ParticipationTime(form.generateFirstNightAsLDT(), form.generateLastNightAsLDT(),
              event.getStartTime()));
    }

    participationManager.updateCounselor(event, form.getCounselor(), counselorInfoBuilder.build());

    return "redirect:/events/" + event.getId() + "/organization";
  }

  @PostMapping("/events/{id}/counselors/remove")
  @Transactional
  public String removeCounselor(@PathVariable("id") Event event,
      @RequestParam("person-id") Person counselor) {
    participationManager.removeCounselor(event, counselor);
    return "redirect:/events/" + event.getId() + "/organization";
  }

}
