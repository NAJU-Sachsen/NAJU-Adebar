package de.naju.adebar.app.events.filter;

import java.util.stream.Stream;
import de.naju.adebar.app.filter.ComparableFilterType;
import de.naju.adebar.model.events.Event;

/**
 * Filter based on the minimum age persons have to have in order to participate in an event
 * 
 * @author Rico Bergmann
 */
public class MinimumParticipantAgeFilter implements EventFilter {
  private int minimumParticipantAge;
  private ComparableFilterType filterType;

  /**
   * @param minimumParticipantAge the minimum age to filter for
   */
  public MinimumParticipantAgeFilter(int minimumParticipantAge) {
    this.minimumParticipantAge = minimumParticipantAge;
    this.filterType = ComparableFilterType.MINIMUM;
  }

  @Override
  public Stream<Event> filter(Stream<Event> input) {
    return input.filter(
        event -> filterType.matching(minimumParticipantAge, event.getMinimumParticipantAge()));
  }
}
