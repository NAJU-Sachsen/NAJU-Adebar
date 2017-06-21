package de.naju.adebar.model.events;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository to access {@link Event} instances
 * @author Rico Bergmann
 * @see Event
 */
@Repository("eventRepo")
public interface EventRepository extends ReadOnlyEventRepository, CrudRepository<Event, EventId> {}
