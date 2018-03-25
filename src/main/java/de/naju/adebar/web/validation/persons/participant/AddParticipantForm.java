package de.naju.adebar.web.validation.persons.participant;

import de.naju.adebar.model.events.Event;
import de.naju.adebar.model.persons.details.Gender;
import de.naju.adebar.model.persons.details.NabuMembershipInformation;
import de.naju.adebar.web.validation.persons.AddPersonForm;
import java.time.LocalDate;
import java.util.List;

/**
 * POJO representation of the participation-related data of the {@link AddPersonForm}.
 *
 * <p>As it shares much of its internal structure (i.e. the data being submitted) with the {@link
 * EditParticipantForm}, it is made a subclass. This is only natural as well: an {@code AddXyzForm}
 * may be considered a special case of an {@code EditXyzForm}. Whether creating is a special case of
 * editing or vice-versa is a philosophical question which sadly cannot be answered here.
 *
 * @author Rico Bergmann
 */
public class AddParticipantForm extends EditParticipantForm {

  private List<Event> events;

  /**
   * Full constructor
   *
   * @param gender the participant's gender
   * @param dateOfBirth the participant's date of birth
   * @param eatingHabits the participant's eating habits
   * @param healthImpairments the participant's health impairments
   * @param nabuMembership nabu membership information for the participant
   * @param remarks additional remarks concerning the participant
   */
  public AddParticipantForm(Gender gender,
      LocalDate dateOfBirth, String eatingHabits, String healthImpairments,
      NabuMembershipInformation nabuMembership,
      String remarks) {
    super(gender, dateOfBirth, eatingHabits, healthImpairments, nabuMembership, remarks);
  }

  /**
   * Default constructor
   */
  public AddParticipantForm() {
  }

  /**
   * @return the events the participant attends
   */
  public List<Event> getEvents() {
    return events;
  }

  /**
   * @param events the events the participant attends
   */
  public void setEvents(List<Event> events) {
    this.events = events;
  }

  /**
   * @return whether the participant attends at least one event
   */
  public boolean hasEvents() {
    return events != null && !events.isEmpty();
  }

  @Override
  public boolean hasData() {
    return hasEvents() || super.hasData();
  }

  @Override
  public String toString() {
    return "AddParticipantForm [" +
        "events=" + events +
        "], " + super.toString();
  }
}
