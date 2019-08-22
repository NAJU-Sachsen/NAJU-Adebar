package de.naju.adebar.model.events;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import com.querydsl.core.types.Predicate;
import de.naju.adebar.infrastructure.ReadOnlyRepository;
import de.naju.adebar.model.chapter.LocalGroup;

/**
 * A repository that provides read-only access to the saved events
 *
 * @author Rico Bergmann
 */
@Repository("ro_eventRepo")
public interface ReadOnlyEventRepository extends //
		ReadOnlyRepository<Event, EventId>, //
		QuerydslPredicateExecutor<Event>, //
		PagingAndSortingRepository<Event, EventId> {

	@Override
	@NonNull
	Optional<Event> findById(@NonNull EventId id);

	Stream<Event> findByNameContainsIgnoreCase(String name);

	/**
	 * @return all events ordered by their start time
	 */
	Iterable<Event> findAllByOrderByStartTime();

	/**
	 * @param predicate predicate to match the events to
	 * @return all events which matched the predicate
	 */
	@Override
	@NonNull
	List<Event> findAll(@NonNull Predicate predicate);

	List<Event> findByStartTimeIsAfterAndParticipantsListBookedOutIsFalse(LocalDateTime time);

	@Query(
			value = "SELECT e.* FROM event e JOIN event_reservations r ON e.id = r.event_id WHERE r.id = ?1",
			nativeQuery = true)
	Optional<Event> findByReservation(long id);

	List<Event> findByLocalGroup(LocalGroup localGroup);

	/**
	 * @return all persisted events as a stream
	 */
	@Query("select e from event e")
	Stream<Event> streamAll();

	Page<Event> findAllPagedByEndTimeIsBeforeOrCanceledIsTrueOrderByStartTimeDesc(LocalDateTime time,
			Pageable pageable);

	Page<Event> findAllPagedByEndTimeIsAfterAndCanceledIsFalseOrderByStartTime(LocalDateTime time,
			Pageable pageable);

}
