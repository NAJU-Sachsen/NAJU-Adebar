package de.naju.adebar.app.events;

import de.naju.adebar.model.events.Event;
import de.naju.adebar.model.events.EventFactory;
import de.naju.adebar.model.events.EventId;
import de.naju.adebar.model.events.EventRepository;
import de.naju.adebar.model.events.ReadOnlyEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

/**
 * A {@link EventManager} that persists its data in a database
 * @author Rico Bergmann
 */
@Service
public class PersistentEventManager implements EventManager {
	private EventFactory eventFactory;
    private EventRepository eventRepo;
    private ReadOnlyEventRepository roRepo;

    @Autowired
    public PersistentEventManager(EventFactory eventFactory, EventRepository eventRepo, @Qualifier("ro_eventRepo") ReadOnlyEventRepository roRepo) {
        Object[] params = {eventFactory, eventRepo, roRepo};
        Assert.noNullElements(params, "No parameter may be null, but at least one was: " + Arrays.toString(params));
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
            throw new RuntimeException("Error during invocation of reflection", e);
        }

        return saveEvent(newEvent);
    }

    @Override
    public Event adoptEventData(String eventId, Event eventData) {
    	Event event = findEvent(eventId).orElseThrow(() -> new IllegalArgumentException("No event with ID " + eventId));
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
    public Optional<Event> findEvent(String id) {
    	EventId eventId = new EventId(id);
        return eventRepo.exists(eventId) ? Optional.of(eventRepo.findOne(eventId)) : Optional.empty();
    }

    @Override
    public ReadOnlyEventRepository repository() {
        return roRepo;
    }
}
