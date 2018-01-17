package de.naju.adebar.model.events.rooms.scheduling;

/**
 * Service to merge a set of potentially overlapping participation times into a minimal set of
 * non-overlapping participation times which describe the same time spans.
 * <p>
 * All scheduling algorithms should always be used on minimal sets to ensure their correct workings.
 *
 * @author Rico Bergmann
 */
public interface ParticipationTimeCombiner {

  /**
   * Performs the combination
   * 
   * @param rawTimes the potentially overlapping participation times
   * @return the minimal yet equivalent set of participation times.
   */
  Iterable<ParticipationTime> combine(Iterable<ParticipationTime> rawTimes);

}
