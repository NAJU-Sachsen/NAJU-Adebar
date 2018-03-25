package de.naju.adebar.services.conversion.events;

import de.naju.adebar.model.events.Event;
import de.naju.adebar.model.events.EventId;
import de.naju.adebar.model.events.ReadOnlyEventRepository;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.Assert;

public class EventConverter implements Converter<String, Event> {

  private final ReadOnlyEventRepository eventRepo;

  public EventConverter(ReadOnlyEventRepository eventRepo) {
    Assert.notNull(eventRepo, "Event repository may not be null");
    this.eventRepo = eventRepo;
  }

  @Override
  public Event convert(String source) {
    EventId eventId = new EventId(source);
    return eventRepo.findById(eventId)
        .orElseThrow(() -> new IllegalArgumentException("No event with ID " + source));
  }

}
