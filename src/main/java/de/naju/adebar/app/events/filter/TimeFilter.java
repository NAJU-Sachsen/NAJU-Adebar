package de.naju.adebar.app.events.filter;

import java.time.LocalDateTime;
import java.util.function.Function;
import java.util.stream.Stream;
import de.naju.adebar.app.filter.DateTimeFilterType;
import de.naju.adebar.model.events.Event;

/**
 * Simple filter for {@link LocalDateTime LocalDateTimes} in {@link Event} objects
 * 
 * @author Rico Bergmann
 */
class TimeFilter {
  private LocalDateTime time;
  private DateTimeFilterType matchType;

  /**
   * @param time the time to base the filter on
   * @param matchType the border the time should form
   */
  public TimeFilter(LocalDateTime time, DateTimeFilterType matchType) {
    this.time = time;
    this.matchType = matchType;
  }

  /**
   * @param input the events to filter
   * @param accessor function providing access to the {@link LocalDateTime LocalDateTimes}
   * @return all events, whose provided {@link LocalDateTime} matched the filtering based on the
   *         matchType
   */
  public Stream<Event> filter(Stream<Event> input, Function<Event, LocalDateTime> accessor) {
    return input.filter(event -> matchType.matching(time, accessor.apply(event)));
  }
}
