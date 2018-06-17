package de.naju.adebar.model.events;

import de.naju.adebar.model.core.TimeSpan;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.model.persons.PersonId;

/**
 * Exception to indicate that a person already participates in an event
 *
 * @author Rico Bergmann
 */
public class ExistingParticipantException extends RuntimeException {

  private static final long serialVersionUID = 1370585756956834005L;

  private final EventId eventId;
  private final String eventName;
  private final TimeSpan eventDate;
  private final PersonId participantId;
  private final String participantName;

  public ExistingParticipantException(Event event, Person participant) {
    this.eventId = event.getId();
    this.eventName = event.getName();
    this.eventDate = TimeSpan.between( //
        event.getStartTime().toLocalDate(), event.getEndTime().toLocalDate());
    this.participantId = participant.getId();
    this.participantName = participant.getName();
  }

  public final EventId getEventId() {
    return eventId;
  }

  public final String getEventName() {
    return eventName;
  }

  public final TimeSpan getEventDate() {
    return eventDate;
  }

  public final PersonId getParticipantId() {
    return participantId;
  }

  public final String getParticipantName() {
    return participantName;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Throwable#toString()
   */
  @Override
  public String toString() {
    return "ExistingParticipantException [eventId=" + eventId + ", eventName=" + eventName
        + ", eventDate=" + eventDate + ", participantId=" + participantId + ", participantName="
        + participantName + "]";
  }

}
