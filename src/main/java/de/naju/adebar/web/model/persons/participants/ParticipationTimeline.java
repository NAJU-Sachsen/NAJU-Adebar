package de.naju.adebar.web.model.persons.participants;

import de.naju.adebar.model.events.Event;
import de.naju.adebar.model.persons.Person;
import java.time.Year;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import org.springframework.util.Assert;

/**
 * Simple wrapper for events sorted by the year they took place in
 */
public class ParticipationTimeline implements Iterable<TimelineEntry> {

  private SortedSet<Year> participationYears;
  private SortedSet<TimelineEntry> participationEntries;

  /**
   * Full constructor
   *
   * @param participant the participant to create the timeline for
   */
  private ParticipationTimeline(Person participant) {
    Assert.notNull(participant, "participant may not be null");
    Assert.isTrue(participant.isParticipant(), "Person must be a participant " + participant);

    // first we will create a tree mapping each year to the events the person participated in in
    // that year

    TreeMap<Year, List<Event>> sortTree = new TreeMap<>(Comparator.reverseOrder());
    participant.getParticipatingEvents().forEach(event -> addParticipationEntry(sortTree, event));

    // second we will use this tree to initialize the timeline's attributes

    this.participationYears = sortTree.navigableKeySet();
    this.participationEntries = new TreeSet<>(Comparator.reverseOrder());

    sortTree.forEach(
        (year, events) -> this.participationEntries.add(TimelineEntry.createFor(year, events)));
  }

  /**
   * Factory method for creating timelines
   *
   * @param participant the participant to create the timeline for
   * @return the timeline
   */
  public static ParticipationTimeline createFor(Person participant) {
    return new ParticipationTimeline(participant);
  }

  /**
   * @return all years in the timeline. This must not be a complete sequence, i.e. some years may be
   *     left out if the person the timeline was created for did not participate in any events that
   *     year. The years are sorted in descending order.
   */
  public SortedSet<Year> getParticipationYears() {
    return Collections.unmodifiableSortedSet(participationYears);
  }

  /**
   * @return the timeline entries, sorted in descending order by their year
   */
  public SortedSet<TimelineEntry> getParticipationEntries() {
    return Collections.unmodifiableSortedSet(participationEntries);
  }

  /**
   * @return whether the timeline contains entries or not
   */
  public boolean isEmpty() {
    return participationEntries.isEmpty();
  }

  /**
   * Places an event in the sort tree
   *
   * @param sortTree the sort tree
   * @param event the event
   */
  private void addParticipationEntry(TreeMap<Year, List<Event>> sortTree, Event event) {
    Year year = Year.from(event.getStartTime());

    if (!sortTree.containsKey(year)) {
      // if the tree does not contain that year, create a new list for it and insert the event
      List<Event> events = new ArrayList<>();
      events.add(event);
      sortTree.put(year, events);
    } else {
      // otherwise just put the event at the end
      // note that the events are not yet sorted here
      List<Event> events = sortTree.get(year);
      events.add(event);
    }

  }

  @Override
  public Iterator<TimelineEntry> iterator() {
    return participationEntries.iterator();
  }

  @Override
  public String toString() {
    return "ParticipationTimeline [" +
        "participationYears=" + participationYears +
        ", participationEntries=" + participationEntries +
        ']';
  }
}
