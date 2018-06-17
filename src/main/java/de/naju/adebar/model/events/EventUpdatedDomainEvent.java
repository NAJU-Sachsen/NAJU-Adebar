package de.naju.adebar.model.events;

public class EventUpdatedDomainEvent extends AbstractEventRelatedDomainEvent {

  public static EventUpdatedDomainEvent forEvent(Event event) {
    return new EventUpdatedDomainEvent(event);
  }

  protected EventUpdatedDomainEvent(Event event) {
    super(event);
  }

  @Override
  public boolean aggregateMayContainMultipleInstances() {
    return false;
  }

}
