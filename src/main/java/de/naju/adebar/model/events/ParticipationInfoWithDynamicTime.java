package de.naju.adebar.model.events;

import de.naju.adebar.model.events.rooms.scheduling.ParticipationTime;

/**
 * Information about a participant which allows the person to attend an event for a certain period
 * of time only. The time span may start after the first day of the event and may end before it
 * finishes.
 *
 * @author Rico Bergmann
 */
public interface ParticipationInfoWithDynamicTime {

  /**
   * Provides the actual participation time
   *
   * @return the time. May be {@code null}
   */
  ParticipationTime getParticipationTime();

  /**
   * Checks, whether the information about the participation time was stored.
   */
  boolean hasParticipationTime();

}
