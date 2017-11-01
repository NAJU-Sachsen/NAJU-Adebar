package de.naju.adebar.app.events.filter;

import java.util.stream.Stream;
import de.naju.adebar.app.filter.ComparableFilterType;
import de.naju.adebar.model.events.Event;

/**
 * Filter for events based on the maximum number of participants
 * 
 * @author Rico Bergmann
 */
public class ParticipantsLimitFilter implements EventFilter {
  private int participantsLimit;
  private ComparableFilterType filterType;

  public ParticipantsLimitFilter(int participantsLimit, ComparableFilterType filterType) {
    this.participantsLimit = participantsLimit;
    this.filterType = filterType;
  }

  @Override
  public Stream<Event> filter(Stream<Event> input) {
    return input
        .filter(event -> filterType.matching(participantsLimit, event.getParticipantsLimit()));
  }
}
