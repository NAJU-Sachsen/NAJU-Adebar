package de.naju.adebar.model.events;

import org.springframework.stereotype.Service;

/**
 * Basic implementation of the {@link EventIdGenerator}
 * 
 * @author Rico Bergmann
 */
@Service
public class SimpleEventIdGenerator implements EventIdGenerator {

  @Override
  public boolean hasNext() {
    return true;
  }

  @Override
  public EventId next() {
    return new EventId();
  }

}
