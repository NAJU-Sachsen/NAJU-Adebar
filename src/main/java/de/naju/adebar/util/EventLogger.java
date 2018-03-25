package de.naju.adebar.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;

/**
 * Debug utility to print all events that are published.
 *
 * @author Rico Bergmann
 */
// @Component
public class EventLogger {

  private static final Logger log = LoggerFactory.getLogger(EventLogger.class);

  /**
   * Prints all events to the log. The log must have info enabled.
   *
   * @param event the event
   */
  @EventListener
  public void log(Object event) {
    if (log.isInfoEnabled()) {
      log.info("Event published: {}", event);
    }
  }

}
