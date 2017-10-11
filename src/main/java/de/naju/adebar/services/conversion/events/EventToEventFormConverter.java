package de.naju.adebar.services.conversion.events;

import de.naju.adebar.controller.forms.events.EventForm;
import de.naju.adebar.model.events.Event;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Service to convert an {@link Event} to a corresponding {@link EventForm}
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
     * @param event the event to convert
     * @return the created form
     */
    public EventForm convertToEventForm(Event event) {
        String street, zip, city;
        if (event.getPlace() == null) {
            street = zip = city = null;
        } else {
            street = event.getPlace().getStreet();
            zip = event.getPlace().getZip();
            city = event.getPlace().getCity();
        }

        String participantsLimit = event.getParticipantsLimit() == Integer.MAX_VALUE ? null : Integer.toString(event.getParticipantsLimit());
        String internalFee = event.getInternalParticipationFee() == null ? null : event.getInternalParticipationFee().getNumberStripped().toPlainString();
        String externalFee = event.getExternalParticipationFee() == null ? null : event.getExternalParticipationFee().getNumberStripped().toPlainString();

        return new EventForm(event.getName(), dateTimeFormatter.format(event.getStartTime()), dateTimeFormatter.format(event.getEndTime()), participantsLimit, Integer.toString(event.getMinimumParticipantAge()), internalFee, externalFee, street, zip, city);
    }

}
