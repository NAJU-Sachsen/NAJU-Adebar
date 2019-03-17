package de.naju.adebar.model.events;

import java.util.Collection;
import de.naju.adebar.model.ChangeSetEntry;
import de.naju.adebar.model.EntityUpdatedEvent;

/**
 * Base class for all domain events that where raised due to some updates on {@link Event}
 * instances.
 *
 * @author Rico Bergmann
 */
public abstract class AbstractEventRelatedDomainEvent extends EntityUpdatedEvent<Event> {

  /**
   * @see EntityUpdatedEvent#EntityUpdatedEvent(E)
   */
  protected AbstractEventRelatedDomainEvent(Event event) {
    super(event);
  }

  /**
   * @see EntityUpdatedEvent#EntityUpdatedEvent(E, Collection)
   */
  protected AbstractEventRelatedDomainEvent(Event event, Collection<ChangeSetEntry> changeset) {
    super(event, changeset);
  }

}
