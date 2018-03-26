package de.naju.adebar.model.persons.events;

import java.util.Collection;
import de.naju.adebar.model.ChangeSetEntry;
import de.naju.adebar.model.EntityUpdatedEvent;
import de.naju.adebar.model.persons.Person;

/**
 * Base for all events which are raised by the {@link Person} aggregate
 *
 * @author Rico Bergmann
 *
 */
public abstract class AbstractPersonRelatedEvent extends EntityUpdatedEvent<Person> {

  /**
   * @see EntityUpdatedEvent#EntityUpdatedEvent(E)
   */
  protected AbstractPersonRelatedEvent(Person person) {
    super(person);
  }

  /**
   * @see EntityUpdatedEvent#EntityUpdatedEvent(E, Collection)
   */
  protected AbstractPersonRelatedEvent(Person person, Collection<ChangeSetEntry> changeset) {
    super(person, changeset);
  }

  /**
   * @return whether an aggregate may publish multiple instances of this event at the same time
   */
  public abstract boolean aggregateMayContainMultipleInstances();
}
