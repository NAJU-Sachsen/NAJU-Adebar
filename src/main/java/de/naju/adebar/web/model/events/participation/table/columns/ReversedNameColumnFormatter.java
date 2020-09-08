package de.naju.adebar.web.model.events.participation.table.columns;

import org.springframework.stereotype.Service;
import de.naju.adebar.model.events.Event;
import de.naju.adebar.model.persons.Person;

@Service
public class ReversedNameColumnFormatter implements TableColumnFormatter {

	@Override
	public boolean isApplicable(Event event) {
		return true;
	}

	@Override
	public String formatColumnFor(Person participant, Event event) {
		assertIsApplicable(event);
		return participant.getLastName() + ", " + participant.getFirstName();
	}

}
