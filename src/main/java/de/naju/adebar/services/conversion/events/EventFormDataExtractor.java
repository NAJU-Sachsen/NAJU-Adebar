package de.naju.adebar.services.conversion.events;

import static de.naju.adebar.web.validation.events.EventForm.CURRENCY_UNIT;

import de.naju.adebar.model.core.Address;
import de.naju.adebar.model.core.Age;
import de.naju.adebar.model.events.Event;
import de.naju.adebar.model.events.EventFactory;
import de.naju.adebar.web.validation.events.EventForm;
import de.naju.adebar.web.validation.events.EventForm.Belonging;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import org.javamoney.moneta.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Service to extract the necessary data from a 'add event' or 'edit event' form
 *
 * @author Rico Bergmann
 * @see EventForm
 */
@Service
public class EventFormDataExtractor {

  private EventFactory eventFactory;
  private DateTimeFormatter dateTimeFormatter;

  @Autowired
  public EventFormDataExtractor(EventFactory eventFactory) {
    Assert.notNull(eventFactory, "Event factory may not be null!");
    this.eventFactory = eventFactory;
    this.dateTimeFormatter = DateTimeFormatter.ofPattern(EventForm.DATE_TIME_FORMAT, Locale.GERMAN);
  }

  /**
   * @param eventForm the form containing the data to extract
   * @return the {@link Event} object encoded by the form
   */
  public Event extractEvent(EventForm eventForm) {
    Address address = new Address(eventForm.getStreet(), eventForm.getZip(), eventForm.getCity());

    Event event = eventFactory.build(eventForm.getName(),
        LocalDateTime.parse(eventForm.getStartTime(), dateTimeFormatter),
        LocalDateTime.parse(eventForm.getEndTime(), dateTimeFormatter));

    event.setPlace(address);

    if (eventForm.hasParticipantsAge()) {
      event.setMinimumParticipantAge(Age.of(Integer.parseInt(eventForm.getParticipantsAge())));
    }

    if (eventForm.hasParticipantsLimit()) {
      event.setParticipantsLimit(Integer.parseInt(eventForm.getParticipantsLimit()));
    }

    if (eventForm.hasInternalParticipationFee()) {
      event.setInternalParticipationFee(
          Money.of(new BigDecimal(eventForm.getInternalParticipationFee()), CURRENCY_UNIT));
    }

    if (eventForm.hasExternalParticipationFee()) {
      event.setExternalParticipationFee(
          Money.of(new BigDecimal(eventForm.getExternalParticipationFee()), CURRENCY_UNIT));
    }

    return event;
  }

  public Belonging extractBelonging(EventForm eventForm) {
    return Belonging.valueOf(eventForm.getBelonging());
  }
}
