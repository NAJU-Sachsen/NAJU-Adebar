package de.naju.adebar.app.events;

import de.naju.adebar.model.events.Event;
import de.naju.adebar.model.events.ReadOnlyEventRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Service to take care of {@link Event Events}
 * @author Rico Bergmann
 * @see Event
 */
@Service
public interface EventManager {

    /**
     * Saves the given event. It may or may not be saved already
     * @param event the event to save
     * @return the saved event. As its internal state may differ after the save, this instance should be used
     * for future operations
     */
    Event saveEvent(Event event);

    /**
     * Creates a new event
     * @param name the event's name
     * @param startTime the event's start time
     * @param endTime the event's end time
     * @return the freshly created event instance
     */
    Event createEvent(String name, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * Changes the state of a saved event
     * @param id the event to update
     * @param newEvent the new event data
     * @return the updated (and saved) event
     */
    Event updateEvent(long id, Event newEvent);

    /**
     * Changes the state of a saved event. In difference to {@link #updateEvent(long, Event)} this does only modify
     * "static" information such as name or participation fee but leaves "dynamic" content such as the participants,
     * organizers and counselors untouched.
     * @param eventId the event to update
     * @param eventData the event's "static" data to adopt
     * @return the updated (and saved) event
     */
    Event adoptEventData(long eventId, Event eventData);

    /**
     * Queries for a specific event
     * @param id the event's ID
     * @return an optional containing the event if it exists, otherwise the optional is empty
     */
    Optional<Event> findEvent(long id);

    /**
     * Provides access to the underlying data
     * @return a read only repository instance
     */
    ReadOnlyEventRepository repository();

}
