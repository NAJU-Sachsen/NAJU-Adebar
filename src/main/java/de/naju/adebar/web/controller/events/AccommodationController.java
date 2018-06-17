package de.naju.adebar.web.controller.events;

import de.naju.adebar.model.events.Event;
import de.naju.adebar.model.events.EventRepository;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.util.Assert2;
import de.naju.adebar.web.model.events.AccommodationReport;
import de.naju.adebar.web.validation.events.accommodation.EditAccommodationForm;
import de.naju.adebar.web.validation.events.accommodation.EditAccommodationFormConverter;
import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AccommodationController {

  private final EventRepository eventRepo;
  private final EditAccommodationFormConverter formConverter;

  public AccommodationController(EventRepository eventRepo,
      EditAccommodationFormConverter formConverter) {
    Assert2.noNullArguments("No argument may be null", eventRepo, formConverter);
    this.eventRepo = eventRepo;
    this.formConverter = formConverter;
  }

  @GetMapping("/events/{id}/accommodation")
  public String showEventAccommodation(@PathVariable("id") Event event, Model model) {

    model.addAttribute("tab", "accommodation");
    model.addAttribute("event", event);
    model.addAttribute("report", AccommodationReport.generateFor(event));

    if (!model.containsAttribute("editAccommodation")) {
      model.addAttribute("editAccommodation", new EditAccommodationForm());
    }

    return "events/eventDetails";
  }

  @PostMapping("/events/{id}/accommodation/update")
  public String updateAccommodation(@PathVariable("id") Event event,
      @ModelAttribute("editAccommodation") @Valid EditAccommodationForm form,
      BindingResult result, RedirectAttributes redirAttr) {

    if (result.hasErrors()) {
      redirAttr.addFlashAttribute("editAccommodation", form);
      redirAttr.addFlashAttribute( //
          "org.springframework.validation.BindingResult.editAccommodation", result);
    } else {
      event.getParticipantsList().updateAccommodation(formConverter.toEntity(form));
      event.getParticipationInfo()
          .updateFlexibleParticipationTimesEnabled(form.isFlexibleParticipationTimes());
      eventRepo.save(event);
    }

    return "redirect:/events/" + event.getId() + "/accommodation";
  }

  @PostMapping("/api/events/participant-details")
  public String loadParticipantDetails(@RequestParam("event") Event event,
      @RequestParam("participant") Person participant, Model model) {

    model.addAttribute("event", event);
    model.addAttribute("participant", participant);
    model.addAttribute("participationDetails",
        event.getParticipantsList().getParticipationDetailsFor(participant));

    return "events/accommodation :: participantDetails";
  }

  @InitBinder("editAccommodation")
  protected void initBinders(WebDataBinder binder) {
    binder.addValidators(formConverter);
  }

}
