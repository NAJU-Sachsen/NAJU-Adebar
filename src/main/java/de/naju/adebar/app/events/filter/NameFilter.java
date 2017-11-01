package de.naju.adebar.app.events.filter;

import java.util.stream.Stream;
import de.naju.adebar.model.events.Event;

/**
 * Filter for events depending on their name
 * 
 * @author Rico Bergmann
 */
public class NameFilter implements EventFilter {
  private String name;

  /**
   * @param name the name to base the filter on
   */
  public NameFilter(String name) {
    this.name = name;
  }

  @Override
  public Stream<Event> filter(Stream<Event> input) {
    if (name == null || name.isEmpty()) {
      return input;
    }
    return input.filter(event -> event.getName().contains(name));
  }
}
