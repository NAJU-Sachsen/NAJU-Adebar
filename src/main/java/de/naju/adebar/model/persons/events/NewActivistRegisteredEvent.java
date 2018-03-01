package de.naju.adebar.model.persons.events;

import de.naju.adebar.model.persons.Person;
import de.naju.adebar.model.persons.exceptions.NoActivistException;

/**
 * Event raised when a person was registered as activist.
 *
 * @author Rico Bergmann
 */
public class NewActivistRegisteredEvent extends AbstractPersonRelatedEvent {

  /**
   * Creates a new event
   *
   * @param p the person which is an activist now
   * @return the event
   * @throws NoActivistException if the person is not an activist
   */
  public static NewActivistRegisteredEvent forPerson(Person p) {
    if (!p.isActivist()) {
      throw new NoActivistException("For person " + p);
    }
    return new NewActivistRegisteredEvent(p);
  }

  /**
   * @param person the person. May not be {@code null}
   */
  private NewActivistRegisteredEvent(Person person) {
    super(person);
  }

  @Override
  public boolean aggregateMayContainMultipleInstances() {
    return false;
  }

}
