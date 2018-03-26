package de.naju.adebar.model.persons.events;

import de.naju.adebar.model.persons.Person;

/**
 * Event raised when a person was registered as parent
 * 
 * @author Rico Bergmann
 */
public class NewParentRegisteredEvent extends AbstractPersonRelatedEvent {

  /**
   * Creates a new event
   * 
   * @param p the new parent
   * @return the event
   * @throws IllegalArgumentException if the person is no parent
   */
  public static NewParentRegisteredEvent forPerson(Person p) {
    if (!p.isParent()) {
      throw new IllegalArgumentException("No parent: " + p);
    }
    return new NewParentRegisteredEvent(p);
  }

  /**
   * @param person the person
   */
  private NewParentRegisteredEvent(Person person) {
    super(person);
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.naju.adebar.model.persons.events.AbstractPersonRelatedEvent#
   * aggregateMayContainMultipleInstances()
   */
  @Override
  public boolean aggregateMayContainMultipleInstances() {
    return false;
  }

}
