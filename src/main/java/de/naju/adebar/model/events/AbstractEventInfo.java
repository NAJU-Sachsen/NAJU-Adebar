package de.naju.adebar.model.events;

import java.util.Optional;
import javax.persistence.Transient;
import org.springframework.util.Assert;

abstract class AbstractEventInfo {

  @Transient
  private Event relatedEvent;

  protected void provideRelatedEvent(Event event) {
    Assert.notNull(event, "event may not be null");
    this.relatedEvent = event;
  }

  protected boolean hasRelatedEvent() {
    return relatedEvent != null;
  }

  protected Optional<Event> getRelatedEvent() {
    return Optional.ofNullable(relatedEvent);
  }

  protected <E extends AbstractEventRelatedDomainEvent> void registerDomainEventIfPossible(
      E domainEvent) {
    if (!hasRelatedEvent()) {
      return;
    }

    if (domainEvent.aggregateMayContainMultipleInstances()) {
      relatedEvent.registerEvent(domainEvent);
    } else if (!relatedEvent.hasRegisteredEventOf(domainEvent.getClass())) {
      relatedEvent.registerEvent(domainEvent);
    }
  }

  protected void registerGenericEventUpdatedDomainEventIfPossible() {
    registerDomainEventIfPossible(EventUpdatedDomainEvent.forEvent(relatedEvent));
  }

  protected <E extends AbstractEventRelatedDomainEvent> boolean mayRegisterEventOf(E domainEvent) {
    return domainEvent.aggregateMayContainMultipleInstances()
        || !relatedEvent.hasRegisteredEventOf(domainEvent.getClass());
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((relatedEvent == null) ? 0 : relatedEvent.hashCode());
    return result;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    AbstractEventInfo other = (AbstractEventInfo) obj;
    if (relatedEvent == null) {
      if (other.relatedEvent != null) {
        return false;
      }
    } else if (!relatedEvent.equals(other.relatedEvent)) {
      return false;
    }
    return true;
  }

}
