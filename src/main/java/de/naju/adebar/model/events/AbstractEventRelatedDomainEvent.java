package de.naju.adebar.model.events;

import java.util.Collection;
import de.naju.adebar.model.ChangeSetEntry;
import de.naju.adebar.model.EntityUpdatedEvent;

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
