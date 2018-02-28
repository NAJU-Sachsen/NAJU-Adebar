package de.naju.adebar.services.conversion.events;

import org.springframework.core.convert.converter.Converter;
import org.springframework.util.Assert;
import de.naju.adebar.model.events.Event;
import de.naju.adebar.model.events.EventId;
import de.naju.adebar.model.events.ReadOnlyEventRepository;

public class EventConverter implements Converter<String, Event> {

  private final ReadOnlyEventRepository eventRepo;

  public EventConverter(ReadOnlyEventRepository eventRepo) {
    Assert.notNull(eventRepo, "Event repository may not be null");
    this.eventRepo = eventRepo;
  }

  @Override
  public Event convert(String source) {
    EventId eventId = new EventId(source);
    Event event = eventRepo.findOne(eventId);

    if (event == null) {
      throw new IllegalArgumentException("No event with given ID: " + source);
    }

    return event;
  }

}
