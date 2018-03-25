package de.naju.adebar.model.persons.events;

import de.naju.adebar.model.persons.Person;
import de.naju.adebar.model.persons.exceptions.NoReferentException;

/**
 * Event raised when a person was registered as a referent.
 *
 * @author Rico Bergmann
 */
public class NewReferentRegisteredEvent extends AbstractPersonRelatedEvent {

  /**
   * Creates a new event
   *
   * @param p the new referent
   * @return the event
   * @throws NoReferentException if the person is not a referent
   */
  public static NewReferentRegisteredEvent forPerson(Person p) {
    if (!p.isReferent()) {
      throw new NoReferentException("For person " + p);
    }
    return new NewReferentRegisteredEvent(p);
  }

  /**
   * @param person the person. May not be {@code null}
   */
  private NewReferentRegisteredEvent(Person person) {
    super(person);
  }

  @Override
  public boolean aggregateMayContainMultipleInstances() {
    return false;
  }

}
