package de.naju.adebar.web.validation.events.participation;

import de.naju.adebar.model.core.Capacity;
import de.naju.adebar.model.core.Email;
import de.naju.adebar.model.events.Reservation;

public class ReservationForm {

  private String description;
  private Capacity slots = Capacity.of(1);
  private Email contactEmail;

  public ReservationForm() {}

  public ReservationForm(Reservation reservation) {
    this.description = reservation.getDescription();
    this.slots = reservation.getNumberOfSlots();
    this.contactEmail = reservation.getContactEmail();
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Capacity getSlots() {
    return slots;
  }

  public void setSlots(Capacity slots) {
    this.slots = slots;
  }

  public Email getContactEmail() {
    return contactEmail;
  }

  public void setContactEmail(Email contactEmail) {
    this.contactEmail = contactEmail;
  }

  Reservation toReservation() {
    return Reservation.generateFor(description, contactEmail, slots);
  }

  @Override
  public String toString() {
    return "ReservationForm [" +
        "description='" + description + '\'' +
        ", slots=" + slots +
        ", contactEmail=" + contactEmail +
        ']';
  }
}
