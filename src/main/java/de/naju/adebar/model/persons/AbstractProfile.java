package de.naju.adebar.model.persons;

import java.util.Optional;
import javax.persistence.Transient;
import org.springframework.util.Assert;
import de.naju.adebar.model.persons.events.AbstractPersonRelatedEvent;

/**
 * Base class for all profiles. Takes care of the event propagation to the {@link Person}
 * 
 * @author Rico Bergmann
 */
public class AbstractProfile {

  @Transient
  private Person relatedPerson;

  /**
   * Sets the person the profile belongs to
   * 
   * @param person the person
   */
  protected void provideRelatedPerson(Person person) {
    Assert.notNull(person, "The related person may not be null");
    this.relatedPerson = person;
  }

  /**
   * @return whether a related person was set for this person
   */
  protected boolean hasRelatedPerson() {
    return relatedPerson != null;
  }

  /**
   * @return the related person if it was provided
   */
  protected Optional<Person> getRelatedPerson() {
    return hasRelatedPerson() //
        ? Optional.of(relatedPerson) //
        : Optional.empty();
  }

  /**
   * Notifies the {@link Person} about a new event if a person was provided and the event actually
   * may be registered.
   * 
   * @param event the event
   */
  protected <E extends AbstractPersonRelatedEvent> void registerEventIfPossible(E event) {
    if (!hasRelatedPerson() || !mayRegisterEventOf(event)) {
      return;
    }
    relatedPerson.registerEvent(event);
  }

  /**
   * Checks, whether another instance of the event may be registered on the related person. This is
   * going to fail if no person was provided.
   * 
   * @param event the event to check
   * @return whether the event may be registered
   */
  private <E extends AbstractPersonRelatedEvent> boolean mayRegisterEventOf(E event) {
    return event.aggregateMayContainMultipleInstances() //
        || !relatedPerson.hasRegisteredEventOf(event.getClass());
  }

}
