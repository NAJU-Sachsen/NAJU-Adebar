package de.naju.adebar.model.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

/**
 * A {@link EventManager} that persists its data in a database
 * @author Rico Bergmann
 */
@Service
public class PersistentEventManager implements EventManager {
    private EventRepository eventRepo;
    private ReadOnlyEventRepository roRepo;

    @Autowired
    public PersistentEventManager(EventRepository eventRepo, ReadOnlyEventRepository roRepo) {
        Object[] params = {eventRepo, roRepo};
        Assert.noNullElements(params, "No parameter may be null, but at least one was: " + Arrays.toString(params));
        this.eventRepo = eventRepo;
        this.roRepo = roRepo;
    }

    @Override
    public Event saveEvent(Event event) {
        return eventRepo.save(event);
    }

    @Override
    public Event createEvent(String name, LocalDateTime startTime, LocalDateTime endTime) {
        return eventRepo.save(new Event(name, startTime, endTime));
    }

    @Override
    public Event updateEvent(long id, Event newEvent) {
        newEvent.setId(id);
        return eventRepo.save(newEvent);
    }

    @Override
    public Event adoptEventData(long eventId, Event eventData) {
        Event event = findEvent(eventId).orElseThrow(() -> new IllegalArgumentException("No event with ID " + eventId));
        event.setName(eventData.getName());
        event.setStartTime(eventData.getStartTime());
        event.setEndTime(eventData.getEndTime());
        event.setParticipantsLimit(eventData.getParticipantsLimit());
        event.setMinimumParticipantAge(eventData.getMinimumParticipantAge());
        event.setParticipationFee(eventData.getParticipationFee());
        event.setPlace(eventData.getPlace());

        return updateEvent(eventId, event);
    }

    @Override
    public Optional<Event> findEvent(long id) {
        return eventRepo.exists(id) ? Optional.of(eventRepo.findOne(id)) : Optional.empty();
    }

    @Override
    public ReadOnlyEventRepository repository() {
        return roRepo;
    }
}
