package de.naju.adebar.web.controller.events;

import de.naju.adebar.app.persons.search.PersonSearchServer;
import de.naju.adebar.model.events.Event;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.util.Assert2;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PersonsToContactController {

	private final PersonSearchServer searchServer;

	public PersonsToContactController(PersonSearchServer searchServer) {
		Assert2.noNullArguments("No argument may be null", searchServer);
		this.searchServer = searchServer;
	}

	@GetMapping("/events/{id}/persons-to-contact/search")
	public String searchPersons(@PathVariable("id") Event event,
			@RequestParam("person-search-query") String query,
			@RequestParam(value = "return-action", defaultValue = "") String returnAction,
			RedirectAttributes redirAttr) {

		redirAttr.addFlashAttribute("matchingPersons", searchServer.runQuery(query.trim()));

		redirAttr.addAttribute("search", query);
		redirAttr.addAttribute("do", returnAction);

		return "redirect:/events/" + event.getId();
	}

	@PostMapping("/events/{id}/persons-to-contact/add")
	@Transactional
	public String addPersonToContact(@PathVariable("id") Event event, //
			@RequestParam("person-id") Person contact, @RequestParam("description") String reason) {

		event.getOrganizationInfo().addPersonToContact(contact, reason);

		return "redirect:/events/" + event.getId();
	}

	@PostMapping("/events/{id}/persons-to-contact/update")
	@Transactional
	public String updatePersonToContact(@PathVariable("id") Event event, //
			@RequestParam("person-id") Person contact, @RequestParam("description") String newReason) {

		event.getOrganizationInfo().updatePersonToContact(contact, newReason);

		return "redirect:/events/" + event.getId();
	}

	@PostMapping("/events/{id}/persons-to-contact/delete")
	@Transactional
	public String removePersonToContact(@PathVariable("id") Event event, //
			@RequestParam("person-id") Person contact) {
		event.getOrganizationInfo().removePersonToContact(contact);
		return "redirect:/events/" + event.getId();
	}

}
