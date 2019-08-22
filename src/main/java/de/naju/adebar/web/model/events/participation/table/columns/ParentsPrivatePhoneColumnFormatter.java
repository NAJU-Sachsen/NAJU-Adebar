package de.naju.adebar.web.model.events.participation.table.columns;

import de.naju.adebar.model.events.Event;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.web.model.events.participation.table.ParticipantsTable;
import java.util.StringJoiner;
import java.util.stream.Stream;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

/**
 * Formatter used in the {@link ParticipantsTable} to display the private phone numbers of parents.
 *
 * @author Rico Bergmann
 */
@Service
public class ParentsPrivatePhoneColumnFormatter implements TableColumnFormatter {

	private static final String PHONE_DELIM = ", ";

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.naju.adebar.web.model.events.participation.table.columns.TableColumnFormatter#isApplicable(
	 * de.naju.adebar.model.events.Event)
	 */
	@Override
	public boolean isApplicable(Event event) {
		// the phone numbers of a participant's parents may always be displayed
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.naju.adebar.web.model.events.participation.table.columns.TableColumnFormatter#
	 * formatColumnFor(de.naju.adebar.model.persons.Person, de.naju.adebar.model.events.Event)
	 */
	@Override
	public String formatColumnFor(Person participant, Event event) {
		if (!participant.hasParents()) {
			return "";
		}
		StringJoiner phoneJoiner = new StringJoiner(PHONE_DELIM);

		Stream<Person> parents = Streamable.of(participant.getParents()).stream();

		parents //
				.filter(Person::hasPhoneNumber) //
				.map(par -> par.getPhoneNumber().getValue()) //
				.distinct() //
				.forEach(phoneJoiner::add);

		return phoneJoiner.toString();
	}

}
