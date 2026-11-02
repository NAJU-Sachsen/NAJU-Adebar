package de.naju.adebar.model.events;

import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import com.querydsl.core.BooleanBuilder;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.model.persons.events.PersonArchivedEvent;
import de.naju.adebar.util.Assert2;

/**
 * The {@code ParticipationListSanitizer} takes care of removing participants from future events in
 * case the participant is archived.
 *
 */
@Service
public class ParticipationListSanitizer {

	private static final Logger log = LoggerFactory.getLogger(ParticipationListSanitizer.class);
	private static final QEvent qevent = QEvent.event;

	private final EventRepository eventRepo;

	public ParticipationListSanitizer(EventRepository eventRepo) {
		Assert2.noNullArguments("No argument may be null", eventRepo);
		this.eventRepo = eventRepo;
	}

	@EventListener
	public void dropParticipantFromEvents(PersonArchivedEvent archivedEvent) {
		final Person archivedPerson = archivedEvent.getEntity();
		final BooleanBuilder futureEventsQuery = new BooleanBuilder();

		// we may not directly fetch events for the archived person due to a hibernate bug
		// https://github.com/querydsl/querydsl/issues/1897
		futureEventsQuery.and(qevent.endTime.after(LocalDateTime.now()));

		final List<Event> futureEvents = eventRepo.findAll(futureEventsQuery);

		// this loop is slightly ineffective but the best we can do right now
		// see comment above for details
		for (Event event : futureEvents) {
			final ParticipantsList participants = event.getParticipantsList();

			if (participants.isParticipant(archivedPerson)) {
				participants.removeParticipant(archivedPerson);
				log.info("Removing archived person " + archivedPerson.getId() + " from future event "
						+ event.getName());
				eventRepo.save(event);
			} else if (participants.isOnWaitingList(archivedPerson)) {
				participants.removeFromWaitingList(archivedPerson);
				eventRepo.save(event);
			}
		}
	}

}
