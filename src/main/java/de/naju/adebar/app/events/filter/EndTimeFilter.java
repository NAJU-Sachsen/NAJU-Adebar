package de.naju.adebar.app.events.filter;

import java.time.LocalDateTime;
import java.util.stream.Stream;
import de.naju.adebar.app.filter.DateTimeFilterType;
import de.naju.adebar.model.events.Event;

/**
 * Filter for events based on the time they should end at
 * 
 * @author Rico Bergmann
 */
public class EndTimeFilter implements EventFilter {
  private TimeFilter timeFilter;

  /**
   * @param endTime the time to filter for
   * @param filterType the kind of border the time should form
   */
  public EndTimeFilter(LocalDateTime endTime, DateTimeFilterType filterType) {
    this.timeFilter = new TimeFilter(endTime, filterType);
  }

  @Override
  public Stream<Event> filter(Stream<Event> input) {
    return timeFilter.filter(input, Event::getEndTime);
  }
}
