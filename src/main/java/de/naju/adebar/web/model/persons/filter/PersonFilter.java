package de.naju.adebar.web.model.persons.filter;

import static de.naju.adebar.web.model.html.NodeFactory.id;

import de.naju.adebar.app.persons.filter.ActivistFilterFields;
import de.naju.adebar.app.persons.filter.ParticipantFilterFields;
import de.naju.adebar.app.persons.filter.PersonFilterFields;
import org.springframework.stereotype.Service;

@Service
public class PersonFilter extends WebFilter {

	private FilterGroup personGroup;
	private FilterGroup participantGroup;
	private FilterGroup activistGroup;
	private FilterGroup referentGroup;
	private FilterGroup eventGroup;
	private FilterGroup chapterGroup;

	public PersonFilter(FieldEnhancementService enhancementService) {
		super(enhancementService);
	}

	@Override
	public WebFilterForm getForm() {
		init();
		return WebFilterForm.of( //
				personGroup, participantGroup, activistGroup,
				eventGroup /*
				 * // referentGroup, // chapterGroup
				 */);
	}

	private void init() {
		final PersonFilterFields personFilters = new PersonFilterFields();
		personGroup = FilterGroup //
				.with(id("filter-person")) //
				.toggledBy(id("toggle-person-filter")) //
				.called("Personen") //
				.withContents( //
						personFilters.getFirstNameFilter(), //
						personFilters.getLastNameFilter(), //
						personFilters.getEmailFilter(), //
						personFilters.getPhoneNumberFilter());

		final ParticipantFilterFields participantFilters = new ParticipantFilterFields();
		participantGroup = FilterGroup //
				.with(id("filter-participant")) //
				.toggledBy(id("toggle-participant-filter")) //
				.called("Teilnehmende") //
				.withContents( //
						participantFilters.getGenderFilter(), //
						participantFilters.getDateOfBirthFilter(), //
						participantFilters.getEatingHabitsFilter(), //
						participantFilters.getHealthImpairmentsFilter(), //
						participantFilters.getNabuMembershipFilter(), //
						participantFilters.getRemarksFilter());

		final ActivistFilterFields activistFilters = new ActivistFilterFields();
		activistGroup = FilterGroup //
				.with(id("filter-activist")) //
				.toggledBy(id("toggle-activist-filter")) //
				.called("Aktive") //
				.withContents( //
						activistFilters.getJuleicaFilter(), //
						activistFilters.getJuleicaExpiryDateFilter(), //
						activistFilters.getJuleicaLevelFilter(), //
						activistFilters.getCounselorFilter());

		referentGroup = FilterGroup //
				.with(id("filter-referent")) //
				.toggledBy(id("toggle-referent-filter")) //
				.called("Referierende") //
				.withContents();

		eventGroup = FilterGroup //
				.with(id("filter-event")) //
				.toggledBy(id("toggle-event-filter")) //
				.called("Veranstaltungsteilnahmen") //
				.withContents(participantFilters.getEventsFilter());

		chapterGroup = FilterGroup //
				.with(id("filter-chapter")) //
				.toggledBy(id("toggle-chapter-filter")) //
				.called("Personen") //
				.withContents();

		enhance(personGroup, participantGroup, activistGroup, referentGroup, eventGroup, chapterGroup);
	}

}
