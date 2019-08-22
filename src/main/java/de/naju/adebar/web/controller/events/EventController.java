package de.naju.adebar.web.controller.events;

import java.time.LocalDateTime;
import javax.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import de.naju.adebar.app.events.search.EventSearchServer;
import de.naju.adebar.documentation.DesignSmell;
import de.naju.adebar.model.chapter.LocalGroup;
import de.naju.adebar.model.chapter.LocalGroupRepository;
import de.naju.adebar.model.chapter.Project;
import de.naju.adebar.model.chapter.ProjectRepository;
import de.naju.adebar.model.events.Event;
import de.naju.adebar.model.events.EventRepository;
import de.naju.adebar.util.Assert2;
import de.naju.adebar.web.validation.events.EditEventForm;
import de.naju.adebar.web.validation.events.EditEventForm.Belonging;
import de.naju.adebar.web.validation.events.EditEventFormConverter;

@Controller
public class EventController {

	private final EventRepository eventRepo;
	private final LocalGroupRepository localGroupRepo;
	private final ProjectRepository projectRepo;
	private final EventSearchServer searchServer;
	private final EditEventFormConverter eventFormConverter;

	public EventController(EventRepository eventRepo, LocalGroupRepository localGroupRepo,
			ProjectRepository projectRepo, EventSearchServer searchServer,
			EditEventFormConverter eventFormConverter) {
		Assert2.noNullArguments("No argument may be null", eventRepo, localGroupRepo, projectRepo,
				searchServer, eventFormConverter);
		this.eventRepo = eventRepo;
		this.localGroupRepo = localGroupRepo;
		this.projectRepo = projectRepo;
		this.searchServer = searchServer;
		this.eventFormConverter = eventFormConverter;
	}

	@GetMapping("/events")
	public String showAllEvents(Model model, @PageableDefault(size = 20) Pageable pageable) {

		/*
		 * This will also include all events where an end time has been created with a meaningful time
		 * portion and today is the date of the end time's date portion but the time set is already
		 * past. The simplification of storing a DateTime where the time portion is irrelevant as 0
		 * o'clock is impractical here. However solving this problem would required much more code and
		 * the inconvenience caused by it is pretty small. Therefore these events will still be
		 * included. One may even argue, whether this is an inconvenience at all - having an event which
		 * just ended still in the list may be useful for doing some kind of follow-up work.
		 */
		LocalDateTime now =
				LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0).minusSeconds(1);

		model.addAttribute("events",
				eventRepo.findAllPagedByEndTimeIsAfterAndCanceledIsFalseOrderByStartTime(now, pageable));

		return "events/overview";
	}

	@GetMapping("/events/past")
	public String showPastEvents(Model model, @PageableDefault(size = 20) Pageable pageable) {
		LocalDateTime now = LocalDateTime.now();
		Page<Event> pastEvents =
				eventRepo.findAllPagedByEndTimeIsBeforeOrCanceledIsTrueOrderByStartTimeDesc(now, pageable);
		model.addAttribute("events", pastEvents);
		return "events/pastEvents";
	}

	@GetMapping("/events/search")
	public String searchEvents(@RequestParam("query") String query,
			@PageableDefault(size = 20) Pageable pageable, Model model) {

		model.addAttribute("events", searchServer.runQuery(query.trim(), pageable));

		return "events/overview";
	}

	@GetMapping("/events/{eid}")
	public String showEventDetailsOverview(@PathVariable("eid") Event event, Model model) {
		model.addAttribute("tab", "general");
		model.addAttribute("event", event);
		model.addAttribute("localGroups", localGroupRepo.findAll());
		model.addAttribute("projects", projectRepo.findAll());

		if (!model.containsAttribute("eventForm")) {
			model.addAttribute("eventForm", eventFormConverter.toForm(event));
		}

		return "events/eventDetails";
	}


	@PostMapping("/events/{id}/update")
	@Transactional
	public String updateEvent(@PathVariable("id") Event event, //
			@ModelAttribute("eventForm") @Valid EditEventForm form, BindingResult errors, //
			RedirectAttributes redirAttr) {

		// FIXME removing old (but used) arrival opts => do we actually need to change anything?
		if (errors.hasErrors()) {
			redirAttr.addFlashAttribute("eventForm", form);
			redirAttr.addFlashAttribute("org.springframework.validation.BindingResult.eventForm", errors);
			return "redirect:/events/" + event.getId();
		}

		eventFormConverter.applyFormToEntity(form, event);
		updateEventBelongingIfNecessary(event, form);

		return "redirect:/events/" + event.getId();
	}

	@PostMapping("/events/{id}/cancel")
	@Transactional
	@PreAuthorize("hasRole('ROLE_CHAIRMAN')")
	public String cancelEvent(@PathVariable("id") Event event, RedirectAttributes redirAttr) {
		event.cancel();
		return "redirect:/events/" + event.getId();
	}

	@InitBinder("eventForm")
	protected void initBinders(WebDataBinder binder) {
		binder.addValidators(eventFormConverter);
	}

	@DesignSmell(description = "This is just ugly")
	@Transactional
	protected void updateEventBelongingIfNecessary(Event event, EditEventForm newData) {
		if (newData.getBelonging() == Belonging.LOCAL_GROUP) {
			LocalGroup newLocalGroup = localGroupRepo.findById(newData.getLocalGroup()).orElseThrow(
					() -> new IllegalArgumentException("No local group with ID " + newData.getLocalGroup()));

			if (event.isForLocalGroup()) {
				LocalGroup currentLocalGroup = localGroupRepo.findById(event.getLocalGroup().getId()).orElseThrow(IllegalArgumentException::new);

				if (!newLocalGroup.equals(currentLocalGroup)) {
					currentLocalGroup.removeEvent(event);
					event.updateLocalGroup(newLocalGroup);
					newLocalGroup.addEvent(event);
				}

			} else {
				event.dropProjectAndAddLocalGroup(newLocalGroup);
				newLocalGroup.addEvent(event);
			}

		} else if (newData.getBelonging() == Belonging.PROJECT) {
			Project newProject = projectRepo.findById(newData.getProject()) //
					.orElseThrow(
							() -> new IllegalArgumentException("No project with ID " + newData.getProject()));

			if (event.isForProject()) {
				Project currentProject = event.getProject();

				if (!newProject.equals(currentProject)) {
					currentProject.removeEvent(event);
					event.updateProject(newProject);
					currentProject.addEvent(event);
				}

			} else {
				event.dropLocalGroupAndAddProject(newProject);
				newProject.addEvent(event);
			}
		}


	}

}
