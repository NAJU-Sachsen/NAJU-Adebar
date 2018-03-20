package de.naju.adebar.web.model.persons.participants;

import de.naju.adebar.model.events.Event;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * An entry in the {@link EventCollection}. It simply consists of a name and a number of events.
 *
 * @author Rico Bergmann
 */
class EventCollectionEntry {

  private String entryName;
  private SortedSet<Event> events;

  /**
   * Full constructor
   *
   * @param entryName the name
   * @param firstEntry the entry's first event
   */
  EventCollectionEntry(String entryName, Event firstEntry) {
    this(entryName);
    this.events.add(firstEntry);
  }

  /**
   * Simplified constructor. Necessary to initialize the raw events correctly
   *
   * @param entryName the name
   */
  EventCollectionEntry(String entryName) {
    this.entryName = entryName;
    this.events = new TreeSet<>();
  }

  /**
   * @return the name
   */
  public String getName() {
    return entryName;
  }

  /**
   * @return the events
   */
  public SortedSet<Event> getEvents() {
    return events;
  }

  /**
   * @return whether the entry contains any events
   */
  public boolean isEmpty() {
    return events.isEmpty();
  }

  /**
   * Inserts a new event
   *
   * @param event the event
   */
  void appendEvent(Event event) {
    this.events.add(event);
  }

  @Override
  public String toString() {
    return "[" +
        "name='" + entryName + '\'' +
        ", events=" + events +
        ']';
  }

}
