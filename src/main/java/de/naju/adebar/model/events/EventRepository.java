package de.naju.adebar.model.events;

import de.naju.adebar.model.human.Person;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.stream.Stream;

/**
 * Repository to access {@link Event} instances
 * @author Rico Bergmann
 * @see Event
 */
@Repository("eventRepo")
public interface EventRepository extends ReadOnlyEventRepository, CrudRepository<Event, Long> {}
