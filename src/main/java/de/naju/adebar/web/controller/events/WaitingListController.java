package de.naju.adebar.web.controller.events;

import de.naju.adebar.app.persons.search.PersonSearchServer;
import de.naju.adebar.model.events.Event;
import de.naju.adebar.model.events.ParticipationManager;
import de.naju.adebar.model.events.ParticipationManager.Result;
import de.naju.adebar.model.events.RegistrationInfo;
import de.naju.adebar.model.events.RegistrationInfo.RegistrationInfoBuilder;
import de.naju.adebar.model.events.rooms.scheduling.ParticipationTime;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.util.Assert2;
import de.naju.adebar.web.validation.events.participation.AddParticipantsForm.AddParticipantForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class WaitingListController {

	private static final Logger log = LoggerFactory.getLogger(WaitingListController.class);

	private final ParticipationManager participationManager;
	private final PersonSearchServer searchServer;

	public WaitingListController(ParticipationManager participationManager,
			PersonSearchServer searchServer) {
		Assert2.noNullArguments("No argument may be null", participationManager, searchServer);
		this.participationManager = participationManager;
		this.searchServer = searchServer;
	}

	@GetMapping("/events/{id}/participants/waiting-list/search")
	public String searchPersonsForWaitingList(@PathVariable("id") Event event,
			@RequestParam("person-search-query") String query,
			@RequestParam(value = "return-action", defaultValue = "") String returnAction,
			RedirectAttributes redirAttr) {

		redirAttr.addFlashAttribute("matchingPersons", searchServer.runQuery(query.trim()));

		redirAttr.addAttribute("search", query);
		redirAttr.addAttribute("do", returnAction);

		return "redirect:/events/" + event.getId() + "/participants";
	}

	@PostMapping("/events/{id}/waiting-list/add")
	@Transactional
	public String addPersonToWaitingList(@PathVariable("id") Event event,
			@RequestParam("person-id") Person person) {
		event.getParticipantsList().putOnWaitingList(person);
		return "redirect:/events/" + event.getId() + "/participants";
	}

	@PostMapping("/events/{id}/waiting-list/apply")
	@Transactional
	public String applyWaitingListEntry(@PathVariable("id") Event event,
			@ModelAttribute("applyWaitingListEntryForm") AddParticipantForm form,
			RedirectAttributes redirAttr) {

		RegistrationInfoBuilder registrationInfoBuilder = form.prepareRegistrationInfo();

		if (event.getParticipationInfo().supportsFlexibleParticipationTimes()) {

			registrationInfoBuilder.withParticipationDuring(new ParticipationTime(
					form.generateFirstNightAsLDT(), form.generateLastNightAsLDT(), event.getStartTime()));
		}

		RegistrationInfo registrationInfo = registrationInfoBuilder.build();
		Result result = participationManager.movePersonFromWaitingListToParticipants(event,
				form.getParticipant(), registrationInfo);

		if (result != Result.OK) {
			redirAttr.addAttribute("apply-waiting-list", "failed");
			redirAttr.addFlashAttribute("failure", result);
		}

		return "redirect:/events/" + event.getId() + "/participants";

	}

	@PostMapping("/events/{id}/waiting-list/drop")
	@Transactional
	public String dropWaitingListEntry(@PathVariable("id") Event event,
			@RequestParam("person-id") Person person) {
		event.getParticipantsList().removeFromWaitingList(person);
		return "redirect:/events/" + event.getId() + "/participants";
	}


	@PostMapping("/events/{id}/waiting-list/clear")
	@Transactional
	public String clearWaitingList(@PathVariable("id") Event event) {
		event.getParticipantsList().clearWaitingList();
		return "redirect:/events/" + event.getId() + "/participants";
	}

}
