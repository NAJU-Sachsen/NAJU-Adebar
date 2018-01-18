package de.naju.adebar.model.events;

import de.naju.adebar.infrastructure.ReadOnlyRepository;
import de.naju.adebar.model.persons.Person;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.stream.Stream;

/**
 * A repository that provides read-only access to the saved events
 * 
 * @author Rico Bergmann
 */
@Repository("ro_eventRepo")
public interface ReadOnlyEventRepository extends ReadOnlyRepository<Event, EventId> {

  /**
   * @param time the time to query for
   * @return all events which start after the specified time
   */
  Iterable<Event> findByStartTimeIsAfter(LocalDateTime time);

  /**
   * @param time the time to query for
   * @return all events which end before the specified time
   */
  Iterable<Event> findByEndTimeIsBefore(LocalDateTime time);

  /**
   * @param timeBefore the earlier time
   * @param timeAfter the later time
   * @return all events that take place within the given interval
   */
  Iterable<Event> findByStartTimeIsBeforeAndEndTimeIsAfter(LocalDateTime timeBefore,
      LocalDateTime timeAfter);

  /**
   * @param person the participant to query for
   * @return all events in which the person participates
   */
  Iterable<Event> findByParticipantsListParticipantsContains(Person person);

  /**
   * @param activist the activist to query for
   * @return all events in which the activist participated as counsellor
   */
  Iterable<Event> findByCounselorsContains(Person activist);

  /**
   * @param activist the activist to query for
   * @return all events in which the activist participated as organizer
   */
  Iterable<Event> findByOrganizersContains(Person activist);

  /**
   * @return all persisted events as a stream
   */
  @Query("select e from event e")
  Stream<Event> streamAll();
}
