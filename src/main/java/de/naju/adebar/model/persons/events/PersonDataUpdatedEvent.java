package de.naju.adebar.model.persons.events;

import java.util.ArrayList;
import java.util.Collection;
import de.naju.adebar.model.ChangeSetEntry;
import de.naju.adebar.model.persons.Person;

/**
 * Event raised when a person's information was updated.
 *
 * @author Rico Bergmann
 *
 */
public class PersonDataUpdatedEvent extends AbstractPersonRelatedEvent {

  /**
   * Creates a new event
   *
   * @param p the person
   * @return the event
   */
  public static PersonDataUpdatedEvent forPerson(Person p) {
    return new PersonDataUpdatedEvent(p);
  }

  /**
   * Creates a new event with an attached change set
   *
   * @param updatedPerson the person
   * @param changeset the change set
   * @return the event
   */
  public static PersonDataUpdatedEvent withChangeset(Person updatedPerson,
      Collection<ChangeSetEntry> changeset) {
    return new PersonDataUpdatedEvent(updatedPerson, changeset);
  }

  /**
   * Combines two events into one, retaining the original change set
   *
   * @param other the other event
   * @return the combined event
   */
  public static PersonDataUpdatedEvent mergeWith(PersonDataUpdatedEvent other) {
    return mergeWith(other, new ArrayList<>());
  }

  /**
   * Combines two events into one, while also merging their change sets
   *
   * @param other the other event
   * @param changeset the additional change set
   * @return the combined event
   */
  public static PersonDataUpdatedEvent mergeWith(PersonDataUpdatedEvent other,
      Collection<ChangeSetEntry> changeset) {
    Collection<ChangeSetEntry> mergedChangeset = new ArrayList<>(other.getChangeset());
    mergedChangeset.addAll(changeset);
    return new PersonDataUpdatedEvent(other.getEntity(), mergedChangeset);
  }

  /**
   * @param person the person. May not be {@code null}
   */
  private PersonDataUpdatedEvent(Person person) {
    super(person);
  }

  /**
   * @param person the person. May not be {@code null}
   * @param changeset the change set
   */
  private PersonDataUpdatedEvent(Person person, Collection<ChangeSetEntry> changeset) {
    super(person, changeset);
  }

  @Override
  public boolean aggregateMayContainMultipleInstances() {
    return false;
  }

}
