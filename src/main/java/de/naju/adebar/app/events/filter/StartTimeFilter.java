package de.naju.adebar.app.events.filter;

import java.time.LocalDateTime;
import java.util.stream.Stream;
import de.naju.adebar.app.filter.DateTimeFilterType;
import de.naju.adebar.model.events.Event;

/**
 * Filter for events based on the time the events start
 * 
 * @author Rico Bergmann
 */
public class StartTimeFilter implements EventFilter {
  private TimeFilter timeFilter;

  /**
   * @param startTime the start time to base the filter on
   * @param filterType the kind of border the given start time should form
   */
  public StartTimeFilter(LocalDateTime startTime, DateTimeFilterType filterType) {
    this.timeFilter = new TimeFilter(startTime, filterType);
  }

  @Override
  public Stream<Event> filter(Stream<Event> input) {
    return timeFilter.filter(input, Event::getStartTime);
  }
}
