package de.naju.adebar.app.events;

import com.google.common.collect.Lists;
import de.naju.adebar.app.IdUpdateFailedException;
import de.naju.adebar.model.events.Event;
import de.naju.adebar.model.events.EventFactory;
import de.naju.adebar.model.events.EventId;
import de.naju.adebar.model.events.EventRepository;
import de.naju.adebar.model.events.ReadOnlyEventRepository;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * A {@link EventManager} that persists its data in a database
 *
 * @author Rico Bergmann
 */
@Service
public class PersistentEventManager implements EventManager {

  private EventFactory eventFactory;
  private EventRepository eventRepo;
  private ReadOnlyEventRepository roRepo;

  @Autowired
  public PersistentEventManager(EventFactory eventFactory, EventRepository eventRepo,
      @Qualifier("ro_eventRepo") ReadOnlyEventRepository roRepo) {
    Object[] params = {eventFactory, eventRepo, roRepo};
    Assert.noNullElements(params,
        "No parameter may be null, but at least one was: " + Arrays.toString(params));
    this.eventFactory = eventFactory;
    this.eventRepo = eventRepo;
    this.roRepo = roRepo;
  }

  @Override
  public Event saveEvent(Event event) {
    return eventRepo.save(event);
  }

  @Override
  public Event createEvent(String name, LocalDateTime startTime, LocalDateTime endTime) {
    Event e = eventFactory.build(name, startTime, endTime);
    return eventRepo.save(e);
  }

  @Override
  public Event updateEvent(String id, Event newEvent) {
    try {
      Method changeId = Event.class.getDeclaredMethod("setId", EventId.class);
      changeId.setAccessible(true);
      changeId.invoke(newEvent, new EventId(id));
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      throw new IdUpdateFailedException("Error during invocation of reflection", e);
    }

    return saveEvent(newEvent);
  }

  @Override
  public Event adoptEventData(String eventId, Event eventData) {
    Event event = findEvent(eventId)
        .orElseThrow(() -> new IllegalArgumentException("No event with ID " + eventId));
    event.setName(eventData.getName());
    event.updateTimePeriod(eventData.getStartTime(), eventData.getEndTime());
    event.setParticipantsLimit(eventData.getParticipantsLimit());
    event.setMinimumParticipantAge(eventData.getMinimumParticipantAge());
    event.setInternalParticipationFee(eventData.getInternalParticipationFee());
    event.setExternalParticipationFee(eventData.getExternalParticipationFee());
    event.setPlace(eventData.getPlace());

    return updateEvent(eventId, event);
  }

  @Override
  public Iterable<Event> findOngoingEvents() {
    LocalDateTime currentTime = LocalDateTime.now();
    LocalDateTime yesterdaysLastMoment = LocalDateTime
        .of(currentTime.getYear(), currentTime.getMonth(), currentTime.getDayOfMonth(), 0, 0)
        .minusMinutes(1);
    Iterable<Event> currentEventsIterable =
        repository().findByStartTimeBeforeAndEndTimeAfterOrderByStartTime(currentTime,
            yesterdaysLastMoment);
    List<Event> currentEvents = Lists.newLinkedList(currentEventsIterable);

    for (Event e : currentEvents) {
      if (e.getEndTime().isBefore(currentTime) && !onlyDateSet(e.getEndTime())) {
        currentEvents.remove(e);
      }
    }

    return currentEvents;
  }

  @Override
  public Optional<Event> findEvent(String id) {
    return eventRepo.findById(new EventId(id));
  }

  @Override
  public ReadOnlyEventRepository repository() {
    return roRepo;
  }

  /**
   * Checks, whether only the date part of a {@link LocalDateTime} instance should be considered.
   * For many events it is not important, when they start or end. Therefore the time is set to
   * 00:00. This may distort results when querying for all events that ended before a certain time.
   * To prevent this, one may check the correctness of the results through this function
   *
   * @param time the date to check
   * @return whether the time should be ignored
   */
  protected boolean onlyDateSet(LocalDateTime time) {
    return time.getHour() == 0 && time.getMinute() == 0;
  }
}
