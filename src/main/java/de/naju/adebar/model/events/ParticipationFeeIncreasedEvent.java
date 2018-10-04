package de.naju.adebar.model.events;

/**
 * Domain event to indicate that some event now has a bigger participation fee than before.
 *
 * @author Rico Bergmann
 */
public class ParticipationFeeIncreasedEvent extends EventUpdatedDomainEvent {

  /**
   * Generates a new domain event
   *
   * @param event the event which had its participation fee increased. May not be {@code null}.
   * @return the domain event
   */
  public static ParticipationFeeIncreasedEvent of(Event event) {
    return new ParticipationFeeIncreasedEvent(event);
  }

  /**
   * Primary constructor.
   *
   * @param event the event which had its participation fee increased
   */
  private ParticipationFeeIncreasedEvent(Event event) {
    super(event);
  }

}
