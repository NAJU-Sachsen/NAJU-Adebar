package de.naju.adebar.app.events.filter;

import java.util.stream.Stream;
import de.naju.adebar.app.filter.streams.StreamBasedFilterBuilder;
import de.naju.adebar.model.events.Event;

/**
 * Filter builder especially for filtering events
 * 
 * @author Rico Bergmann
 */
public class EventFilterBuilder extends StreamBasedFilterBuilder<Event> {

  public EventFilterBuilder(Stream<Event> eventStream) {
    super(eventStream);
  }

}
