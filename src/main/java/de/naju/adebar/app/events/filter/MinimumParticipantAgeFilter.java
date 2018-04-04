package de.naju.adebar.app.events.filter;

import de.naju.adebar.app.filter.ComparableFilterType;
import de.naju.adebar.model.core.Age;
import de.naju.adebar.model.events.Event;
import java.util.stream.Stream;

/**
 * Filter based on the minimum age persons have to have in order to participate in an event
 *
 * @author Rico Bergmann
 */
public class MinimumParticipantAgeFilter implements EventFilter {

  private Age minimumParticipantAge;
  private ComparableFilterType filterType;

  /**
   * @param minimumParticipantAge the minimum age to filter for
   */
  public MinimumParticipantAgeFilter(Age minimumParticipantAge) {
    this.minimumParticipantAge = minimumParticipantAge;
    this.filterType = ComparableFilterType.MINIMUM;
  }

  @Override
  public Stream<Event> filter(Stream<Event> input) {
    return input.filter(
        event -> filterType.matching(minimumParticipantAge, event.getMinimumParticipantAge()));
  }
}
