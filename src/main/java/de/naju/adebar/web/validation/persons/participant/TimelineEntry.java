package de.naju.adebar.web.validation.persons.participant;

import com.google.common.collect.Sets;
import de.naju.adebar.model.events.Event;
import java.time.Year;
import java.util.Collections;
import java.util.Comparator;
import java.util.SortedSet;
import org.springframework.util.Assert;

/**
 * Simple wrapper for a number of events taking place in the same year
 */
public class TimelineEntry implements Comparable<TimelineEntry> {

  private Year year;
  private SortedSet<Event> events;

  /**
   * Full constructor.
   *
   * <p> The constraint that all events have to take place in the same year is not enforced although
   * putting events from different years into the same entry completely contradicts the meaning and
   * sense of the wrapper.
   *
   * @param year the year the events took place
   * @param events the events
   */
  private TimelineEntry(Year year, Iterable<Event> events) {
    Assert.notNull(year, "Year may not be null");
    Assert.notNull(events, "Events may not be null");
    this.year = year;
    this.events = Sets.newTreeSet(Comparator.reverseOrder());
    events.forEach(event -> this.events.add(event));
  }

  /**
   * Factory method to create timeline entries
   *
   * @param year the year the events took place
   * @param events the events
   * @return the entry
   */
  public static TimelineEntry createFor(Year year, Iterable<Event> events) {
    return new TimelineEntry(year, events);
  }

  /**
   * @return the year in which the entry's events took place
   */
  public Year getYear() {
    return year;
  }

  /**
   * @return the events in the entry, sorted in descending order by their start date
   */
  public SortedSet<Event> getEvents() {
    return Collections.unmodifiableSortedSet(events);
  }

  @Override
  public int compareTo(TimelineEntry other) {
    return this.year.compareTo(other.year);
  }

  @Override
  public String toString() {
    return "TimelineEntry [" +
        "year=" + year +
        ", events=" + events +
        ']';
  }
}
