package de.naju.adebar.web.controller.persons;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import de.naju.adebar.model.events.Event;
import de.naju.adebar.model.events.EventRepository;
import de.naju.adebar.model.events.ParticipationManager;
import de.naju.adebar.model.events.ParticipationManager.Result;
import de.naju.adebar.model.events.QEvent;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.web.model.persons.participants.ParticipationTimeline;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Handles all requests to participation timelines
 *
 * @author Rico Bergmann
 * @see Event
 */
@Controller
public class ParticipationController {

	private static final QEvent event = QEvent.event;

	private final EventRepository eventRepo;
	private final ParticipationManager participationManager;

	/**
	 * Full constructor
	 *
	 * @param eventRepo repository containing all events
	 */
	public ParticipationController(EventRepository eventRepo,
			ParticipationManager participationManager) {
		Assert.notNull(eventRepo, "eventRepo may not be null");
		Assert.notNull(participationManager, "participationManager may not be null");
		this.eventRepo = eventRepo;
		this.participationManager = participationManager;
	}

	/**
	 * Renders the participation timeline for a specific person
	 *
	 * @param person the person
	 * @param model model to put the data to render into
	 * @return the participation timeline page
	 */
	@GetMapping("/persons/{id}/events")
	public String showParticipationTimeline(@PathVariable("id") Person person, Model model) {

		model.addAttribute("person", person);

		if (person.isParticipant()) {
			final ParticipationTimeline timeline = person.isActivist() //
					? ParticipationTimeline
					.createForCounselor(person, eventRepo.findByCounselor(person))
					//
					: ParticipationTimeline.createFor(person);
			model.addAttribute("participationTimeline", timeline);
		} else {
			model.addAttribute("participationTimeline", null);
		}

		List<Event> futureEvents = eventRepo.findAll(futureEventsWithout(person));
		futureEvents = futureEvents.stream() //
				.filter(e -> !e.isBookedOut()) //
				.collect(Collectors.toList());

		model.addAttribute("futureEvents", futureEvents);
		model.addAttribute("tab", "events");
		return "persons/personDetails";
	}

	/**
	 * Adds a person as participant to a list of events
	 *
	 * @param person the person
	 * @param events the events
	 * @return a redirection to the participation timeline page
	 */
	@PostMapping("/persons/{id}/events/add")
	@Transactional
	public String addParticipantToEvents(@PathVariable("id") Person person,
			@RequestParam("event-ids") List<Event> events, RedirectAttributes redirAttr) {

		Map<Event, Result> failedParticipations = new HashMap<>();

		events.forEach(e -> {
			Result res = participationManager.addParticipant(e, person);
			if (res != Result.OK) {
				failedParticipations.put(e, res);
			}
		});

		if (!failedParticipations.isEmpty()) {
			redirAttr.addAttribute("add", "failed");
			redirAttr.addFlashAttribute("failedParticipations", failedParticipations);
		}

		return "redirect:/persons/" + person.getId() + "/events";
	}

	/**
	 * Creates a predicate that matches all future events in which a person does not participate
	 *
	 * @param participant the person
	 * @return the predicate
	 */
	private Predicate futureEventsWithout(Person participant) {
		BooleanBuilder predicate = new BooleanBuilder();
		predicate.and(event.startTime.after(LocalDateTime.now()));
		predicate.and(event.notIn(participant.getParticipatingEvents()));
		return predicate;
	}

}
