package de.naju.adebar.model.persons.events;

import de.naju.adebar.model.persons.Person;

/**
 * Event raised when a person was archived.
 *
 * @author Rico Bergmann
 */
public class PersonArchivedEvent extends AbstractPersonRelatedEvent {

  /**
   * Creates a new event
   *
   * @param p the person
   * @return the event
   * @throws IllegalStateException if the person is not archived
   */
  public static PersonArchivedEvent forPerson(Person p) {
    if (!p.isArchived()) {
      throw new IllegalStateException("Person is not archived: " + p);
    }
    return new PersonArchivedEvent(p);
  }

  /**
   * @param person the person. May not be {@code null}
   */
  private PersonArchivedEvent(Person person) {
    super(person);
  }

  @Override
  public boolean aggregateMayContainMultipleInstances() {
    return false;
  }

}
