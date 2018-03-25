package de.naju.adebar.model.events;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository to access {@link Event} instances
 *
 * @author Rico Bergmann
 * @see Event
 */
@Repository("eventRepo")
public interface EventRepository extends ReadOnlyEventRepository, CrudRepository<Event, EventId> {

  @Override
  Optional<Event> findById(EventId id);

}
