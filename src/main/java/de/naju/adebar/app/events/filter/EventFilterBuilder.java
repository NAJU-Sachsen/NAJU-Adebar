package de.naju.adebar.app.events.filter;

import de.naju.adebar.app.filter.FilterBuilder;
import de.naju.adebar.model.events.Event;
import java.util.stream.Stream;

/**
 * Filter builder especially for filtering events
 *
 * @author Rico Bergmann
 */
public class EventFilterBuilder extends FilterBuilder<Event> {

  public EventFilterBuilder(Stream<Event> eventStream) {
    super(eventStream);
  }

}
