package de.naju.adebar.model.events;

import com.querydsl.core.types.Predicate;
import de.naju.adebar.infrastructure.ReadOnlyRepository;
import de.naju.adebar.model.persons.Person;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

/**
 * A repository that provides read-only access to the saved events
 *
 * @author Rico Bergmann
 */
@Repository("ro_eventRepo")
public interface ReadOnlyEventRepository extends // 
    ReadOnlyRepository<Event, EventId>,
    QuerydslPredicateExecutor<Event> {

  /**
   * @return all events ordered by their start time
   */
  Iterable<Event> findAllByOrderByStartTime();

  /**
   * @param predicate predicate to match the events to
   * @return all events which matched the predicate
   */
  @Override
  List<Event> findAll(Predicate predicate);

  /**
   * @param time the time to query for
   * @return all events which start after the specified time
   */
  List<Event> findByStartTimeIsAfter(LocalDateTime time);

  /**
   * @param time the time to query for
   * @return all events which start after the specified time, ordered by their start time
   */
  Iterable<Event> findByStartTimeAfterOrderByStartTime(LocalDateTime time);

  /**
   * @param time the time to query for
   * @return all events which end before the specified time
   */
  Iterable<Event> findByEndTimeIsBefore(LocalDateTime time);

  /**
   * @param time the time to query for
   * @return all events which end before the specified time, ordered by their start time
   *     (descending)
   */
  Iterable<Event> findByEndTimeBeforeOrderByStartTimeDesc(LocalDateTime time);

  /**
   * @param timeBefore the earlier time
   * @param timeAfter the later time
   * @return all events that take place within the given interval
   */
  Iterable<Event> findByStartTimeIsBeforeAndEndTimeIsAfter(LocalDateTime timeBefore,
      LocalDateTime timeAfter);

  /**
   * @param timeBefore the earlier time
   * @param timeAfter the later time
   * @return all events that take place within the given interval, odered by their start time
   */
  Iterable<Event> findByStartTimeBeforeAndEndTimeAfterOrderByStartTime(LocalDateTime timeBefore,
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
