package de.naju.adebar.model.events;

import de.naju.adebar.model.human.Activist;
import de.naju.adebar.model.human.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

/**
 * Repository to access {@link Event} instances
 * @author Rico Bergmann
 * @see Event
 */
@Repository
public interface EventRepository extends CrudRepository<Event, Long> {

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
    Iterable<Event> findByStartTimeIsBeforeAndEndTimeIsAfter(LocalDateTime timeBefore, LocalDateTime timeAfter);

    /**
     * @param person the participant to query for
     * @return all events in which the person participates
     */
    Iterable<Event> findByParticipantsContains(Person person);

    /**
     * @param activist the activist to query for
     * @return all events in which the activist participated as counsellor
     */
    Iterable<Event> findByCounselorsContains(Activist activist);

    /**
     * @param activist the activist to query for
     * @return all events in which the activist participated as organizer
     */
    Iterable<Event> findByOrganizersContains(Activist activist);

}
