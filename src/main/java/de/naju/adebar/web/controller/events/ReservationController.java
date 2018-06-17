package de.naju.adebar.web.controller.events;

import de.naju.adebar.model.events.Event;
import de.naju.adebar.model.events.Reservation;
import de.naju.adebar.model.support.NumericEntityId;
import de.naju.adebar.util.Assert2;
import de.naju.adebar.web.validation.events.participation.ReservationForm;
import de.naju.adebar.web.validation.events.participation.ReservationFormConverter;
import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ReservationController {

  private final ReservationFormConverter reservationFormConverter;

  public ReservationController(ReservationFormConverter reservationFormConverter) {
    Assert2.noNullArguments("No argument may be null", reservationFormConverter);
    this.reservationFormConverter = reservationFormConverter;
  }

  @PostMapping("/events/{id}/reservations/add")
  @Transactional
  public String addReservation(@PathVariable("id") Event event,
      @ModelAttribute("addReservationForm") @Valid ReservationForm form, Errors errors) {

    if (!errors.hasErrors()) {
      event.getParticipantsList().addReservation(reservationFormConverter.toEntity(form));
    }

    return "redirect:/events/" + event.getId() + "/participants";

  }

  @PostMapping("/events/{id}/reservations/remove")
  @Transactional
  public String deleteReservation(@PathVariable("id") Event event,
      @RequestParam("reservation-id") Reservation theReservation) {
    event.getParticipantsList().removeReservation(theReservation);
    return "redirect:/events/" + event.getId() + "/participants";
  }

  @PostMapping("/events/{id}/reservations/update")
  @Transactional
  public String updateReservation(@PathVariable("id") Event event, @RequestParam("reservation-id")
      NumericEntityId reservationId,
      @ModelAttribute("editReservationForm") @Valid ReservationForm newInfo, Errors errors) {

    if (!errors.hasErrors()) {
      event.getParticipantsList()
          .updateReservation(reservationId, reservationFormConverter.toEntity(newInfo));
    }

    return "redirect:/events/" + event.getId() + "/participants";
  }

  @InitBinder("editReservationForm")
  protected void initBinders(WebDataBinder binder) {
    binder.addValidators(reservationFormConverter);
  }

}
