package de.naju.adebar.model.persons.events;

import de.naju.adebar.model.persons.Person;
import de.naju.adebar.model.persons.exceptions.NoParticipantException;

/**
 * Event raised when a person was registered as camp participant.
 *
 * @author Rico Bergmann
 */
public class NewCampParticipantRegisteredEvent extends AbstractPersonRelatedEvent {

  /**
   * Creates a new event
   *
   * @param p the new camp participant
   * @return the event
   * @throws NoParticipantException if the person is no camp participant
   */
  public static NewCampParticipantRegisteredEvent forPerson(Person p) {
    if (!p.isParticipant()) {
      throw new NoParticipantException("For person " + p);
    }
    return new NewCampParticipantRegisteredEvent(p);
  }

  /**
   * @param person the person. May not be {@code null}
   */
  private NewCampParticipantRegisteredEvent(Person person) {
    super(person);
  }

  @Override
  public boolean aggregateMayContainMultipleInstances() {
    return false;
  }

}
