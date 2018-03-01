package de.naju.adebar.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.naju.adebar.app.events.EventDataProcessor;
import de.naju.adebar.app.persons.HumanDataProcessor;
import de.naju.adebar.services.conversion.events.EventFormDataExtractor;
import de.naju.adebar.services.conversion.events.EventToEventFormConverter;
import de.naju.adebar.services.conversion.events.FilterEventsFormDataExtractor;

/**
 * Data processors for the {@link EventController}
 *
 * @author Rico Bergmann
 */
@Component
public class EventControllerDataProcessors {
  public final EventFormDataExtractor eventExtractor;
  public final EventToEventFormConverter eventConverter;
  public final FilterEventsFormDataExtractor filterEventsExtractor;
  public final HumanDataProcessor persons;
  public final EventDataProcessor events;

  @Autowired
  public EventControllerDataProcessors(EventFormDataExtractor eventFormDataExtractor,
      EventToEventFormConverter eventToEventFormConverter,
      FilterEventsFormDataExtractor filterEventsFormDataExtractor,
      HumanDataProcessor humanDataProcessor, EventDataProcessor eventDataProcessor) {
    this.eventExtractor = eventFormDataExtractor;
    this.eventConverter = eventToEventFormConverter;
    this.filterEventsExtractor = filterEventsFormDataExtractor;
    this.persons = humanDataProcessor;
    this.events = eventDataProcessor;
  }

}
