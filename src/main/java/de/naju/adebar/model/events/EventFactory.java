package de.naju.adebar.model.events;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Factory to create {@link Event} instances
 * 
 * @author Rico Bergmann
 *
 */
@Service
public class EventFactory {
  private EventIdGenerator idGenerator;

  @Autowired
  public EventFactory(EventIdGenerator idGenerator) {
    this.idGenerator = idGenerator;
  }

  /**
   * Creates a new event
   * 
   * @param name the event's name
   * @param startTime the start time
   * @param endTime the end time
   * @return the event
   */
  public Event build(String name, LocalDateTime startTime, LocalDateTime endTime) {
    return new Event(idGenerator.next(), name, startTime, endTime);
  }

}
