package de.naju.adebar.app.events.processing;

import de.naju.adebar.model.core.Email;
import de.naju.adebar.model.events.Event;
import de.naju.adebar.model.events.Reservation;
import de.naju.adebar.model.persons.Person;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * Service providing useful data extraction capabilities for events.
 * <p>
 * This should be merely seen as a loose collection rather than a grouping by functionality
 *
 * @author Rico Bergmann
 */
@Service
public class EventDataProcessingService {

  public Iterable<Email> getParticipantEmailAddresses(Event event) {
    return event.getParticipantsList().getParticipantsList().stream() //
        .filter(Person::hasOwnOrParentsEmail) // ignore participants with no email at all
        .map(Person::getOwnOrParentsEmail) // use the parent's email if the participant has none
        .collect(Collectors.toSet()); // eliminate duplicates
  }

  public Iterable<Email> getEmailAddressesOfParticipantsWithoutPayment(Event event) {
    return event.getParticipantsList().getParticipants().entrySet().stream()
        .filter(entry -> entry.getKey().hasOwnOrParentsEmail()) // ignore participants with no email
        .filter(entry -> !entry.getValue().isParticipationFeePayed()) // ignore payed participants
        .map(entry -> entry.getKey().getOwnOrParentsEmail()) // use the parent's email if necessary
        .collect(Collectors.toSet()); // eliminate duplicates
  }

  public Iterable<Email> getEmailAddressesOfParticpantsWithoutSentRegistrationForm(Event event) {
    return event.getParticipantsList().getParticipants().entrySet().stream()
        .filter(entry -> entry.getKey().hasOwnOrParentsEmail()) // ignore participants with no email
        // ignore participants who received the registration form already
        .filter(entry -> !entry.getValue().isRegistrationFormSent())
        .map(entry -> entry.getKey().getOwnOrParentsEmail()) // use the parent's email if necessary
        .collect(Collectors.toSet()); // eliminate duplicates
  }

  public Iterable<Email> getEmailAddressesOfParticipantsWithoutSignedRegistrationForm(Event event) {
    return event.getParticipantsList().getParticipants().entrySet().stream()
        .filter(entry -> entry.getKey().hasOwnOrParentsEmail()) // ignore participants with no email
        // ignore participants who filled the registration form
        .filter(entry -> !entry.getValue().isRegistrationFormFilled())
        .map(entry -> entry.getKey().getOwnOrParentsEmail()) // use the parent's email if necessary
        .collect(Collectors.toSet()); // eliminate duplicates
  }

  public Iterable<Email> getEmailAddressesFromWaitingList(Event event) {
    return event.getParticipantsList().getWaitingList().stream()
        .filter(Person::hasOwnOrParentsEmail) // ignore persons which have no email at all
        .map(Person::getOwnOrParentsEmail) // use the parent's email if necessary
        .collect(Collectors.toSet()); // eliminate duplicates
  }

  public Iterable<Email> getEmailAddressesFromReservations(Event event) {
    return event.getParticipantsList().getReservations().stream()
        .filter(Reservation::hasContactEmail) // ignore reservations without email
        .map(Reservation::getContactEmail) // extract the email addresses
        .collect(Collectors.toSet()); // eliminate duplicates
  }

  public Iterable<Email> getOrganizerEmailAddresses(Event event) {
    return event.getOrganizationInfo().getOrganizers().stream() //
        .filter(Person::hasEmail) // ignore organizers without an email address
        .map(Person::getEmail) // an organizer should have an own email address - ignore parents
        .collect(Collectors.toSet()); // eliminate duplicates
  }

  public Iterable<Email> getCounselorEmailAddresses(Event event) {
    return event.getOrganizationInfo().getCounselorList().stream() //
        .filter(Person::hasEmail) // ignore counselors without an email address
        .map(Person::getEmail) // a counselor should have an own email address - ignore parents
        .collect(Collectors.toSet()); // eliminate duplicates
  }

}
