package de.naju.adebar.web.model.persons.participants;

import de.naju.adebar.model.chapter.LocalGroup;
import de.naju.adebar.model.chapter.Project;
import de.naju.adebar.model.events.Event;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import org.springframework.lang.NonNull;

//@formatter:off
/**
 * Wrapper to group events by the project or event in which context they are hosted.
 *
 * <p> There are three meta groups:
 * 
 * <ol>
 *   <li>
 *     local group mapped to the events it hosts
 *   </li>
 *   <li>
 *     project mapped to the event it hosts
 *   </li>
 *   <li>
 *     events which neither belong to a project nor local group (currently there should be none)
 *   </li>
 * </ol>
 *
 * @author Rico Bergmann
 */
//@formatter:on
public class EventCollection implements Iterable<EventCollectionEntry> {

  private Map<String, EventCollectionEntry> localGroupEvents;
  private Map<String, EventCollectionEntry> projectEvents;
  private EventCollectionEntry rawEvents;

  /**
   * Default constructor
   */
  private EventCollection() {
    this.localGroupEvents = new HashMap<>();
    this.projectEvents = new HashMap<>();
    this.rawEvents = new EventCollectionEntry("");
  }

  /**
   * Factory method to start building a new collection
   *
   * @return a builder
   */
  public static EventCollectionBuilder newCollection() {
    return new EventCollectionBuilder();
  }

  @Override
  @NonNull
  public Iterator<EventCollectionEntry> iterator() {
    return new EvenCollectionIterator(this);
  }

  @Override
  public String toString() {
    return "EventCollection [" +
        "localGroupEvents=" + localGroupEvents +
        ", projectEvents=" + projectEvents +
        ", rawEvents=" + rawEvents +
        ']';
  }

  /**
   * Builder to construct new event collections
   *
   * @author Rico Bergmann
   */
  public static class EventCollectionBuilder {

    private EventCollection theCollection;

    /**
     * Default constructor
     */
    EventCollectionBuilder() {
      this.theCollection = new EventCollection();
    }

    /**
     * Puts a new event, mapped by its local group into the collection
     *
     * @param localGroup the local group
     * @param event the event
     * @return the builder for convenient method chaining
     */
    public EventCollectionBuilder appendFor(LocalGroup localGroup, Event event) {
      String key = localGroup.getName();

      if (theCollection.localGroupEvents.containsKey(key)) {
        theCollection.localGroupEvents.get(key).appendEvent(event);
      } else {
        theCollection.localGroupEvents.put(key, new EventCollectionEntry(key, event));
      }

      return this;
    }

    /**
     * Puts a new event, mapped by its project into the collection
     *
     * @param project the project
     * @param event the event
     * @return the builder for convenient method chaining
     */
    public EventCollectionBuilder appendFor(Project project, Event event) {
      String key = project.getName();

      if (theCollection.projectEvents.containsKey(key)) {
        theCollection.projectEvents.get(key).appendEvent(event);
      } else {
        theCollection.projectEvents.put(key, new EventCollectionEntry(key, event));
      }

      return this;
    }

    /**
     * Puts a new event without project nor local group into the collection
     *
     * @param event the event
     * @return the builder for convenient method chaining
     */
    public EventCollectionBuilder appendRaw(Event event) {
      theCollection.rawEvents.appendEvent(event);
      return this;
    }

    /**
     * Finishes construction. The collection will be immutable afterwards
     *
     * @return the completed collection
     */
    public EventCollection done() {
      return theCollection;
    }

  }

  /**
   * Iterator over the entries within a collection.
   *
   * <p> The iterator will return the local-group events first, then the project events and lastly
   * the raw events.
   *
   * @author Rico Bergmann
   */
  private static class EvenCollectionIterator implements Iterator<EventCollectionEntry> {

    private Iterator<EventCollectionEntry> localGroupIterator;
    private Iterator<EventCollectionEntry> projectIterator;
    private EventCollectionEntry rawEvents;

    /**
     * Full constructor
     *
     * @param eventCollection the collection to iterate
     */
    EvenCollectionIterator(EventCollection eventCollection) {
      // we will just use iterators directly here
      this.localGroupIterator = eventCollection.localGroupEvents.values().iterator();
      this.projectIterator = eventCollection.projectEvents.values().iterator();

      // raw events are all treated equally, therefore we safe them directly
      this.rawEvents = eventCollection.rawEvents;
    }

    @Override
    public boolean hasNext() {
      return localGroupIterator.hasNext() // 
          || projectIterator.hasNext() //
          || (rawEvents != null && !rawEvents.isEmpty());
    }

    @Override
    public EventCollectionEntry next() {
      // it's actually quite simple: just check each iterator, if there are any elements left
      // and the first one which does, is used
      // the sequence of checks constitutes the order of iteration

      if (localGroupIterator.hasNext()) {
        return localGroupIterator.next();
      } else if (projectIterator.hasNext()) {
        return projectIterator.next();
      } else if (rawEvents != null) {
        // if we used the raw events, their "iteration" is finished as well, which we are going to
        // indicate by setting them null
        EventCollectionEntry events = rawEvents;
        rawEvents = null;
        return events;
      } else {
        throw new NoSuchElementException();
      }
    }
  }

}
