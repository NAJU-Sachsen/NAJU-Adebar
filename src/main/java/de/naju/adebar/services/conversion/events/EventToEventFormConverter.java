package de.naju.adebar.services.conversion.events;

import de.naju.adebar.model.events.Event;
import de.naju.adebar.web.validation.events.EventForm;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import org.springframework.stereotype.Service;

/**
 * Service to convert an {@link Event} to a corresponding {@link EventForm}
 *
 * @author Rico Bergmann
 */
@Service
public class EventToEventFormConverter {

  private DateTimeFormatter dateTimeFormatter;

  public EventToEventFormConverter() {
    dateTimeFormatter = DateTimeFormatter.ofPattern(EventForm.DATE_TIME_FORMAT, Locale.GERMAN);
  }

  /**
   * Performs the conversion
   *
   * @param event the event to convert
   * @return the created form
   */
  public EventForm convertToEventForm(Event event) {
    String street;
    String zip;
    String city;

    if (event.getPlace() == null) {
      street = zip = city = null;
    } else {
      street = event.getPlace().getStreet();
      zip = event.getPlace().getZip();
      city = event.getPlace().getCity();
    }

    String participantsLimit = event.getParticipantsLimit() == Integer.MAX_VALUE ? null
        : Integer.toString(event.getParticipantsLimit());
    String minimumParticipantAge =
        event.hasMinimumParticipantAge() ? event.getMinimumParticipantAge().toString() : null;

    String internalFee = event.getInternalParticipationFee() == null ? null
        : event.getInternalParticipationFee().getNumberStripped().toPlainString();
    String externalFee = event.getExternalParticipationFee() == null ? null
        : event.getExternalParticipationFee().getNumberStripped().toPlainString();

    EventForm eventForm =
        new EventForm(event.getName(), dateTimeFormatter.format(event.getStartTime()),
            dateTimeFormatter.format(event.getEndTime()), street, zip, city);
    eventForm.setParticipationInfo(participantsLimit,
        minimumParticipantAge, internalFee, externalFee);
    return eventForm;
  }

}
