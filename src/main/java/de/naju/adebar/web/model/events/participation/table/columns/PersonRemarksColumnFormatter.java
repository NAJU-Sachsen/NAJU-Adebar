package de.naju.adebar.web.model.events.participation.table.columns;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import de.naju.adebar.model.events.Event;
import de.naju.adebar.model.persons.ParticipantProfile;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.web.model.events.participation.table.ParticipantsTable;

/**
 * Formatter used in the {@link ParticipantsTable} to display remarks about the participating
 * persons.
 *
 * @author Rico Bergmann
 */
@Service
public class PersonRemarksColumnFormatter implements TableColumnFormatter {

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.naju.adebar.web.model.events.participation.table.columns.TableColumnFormatter#isApplicable(
	 * de.naju.adebar.model.events.Event)
	 */
	@Override
	public boolean isApplicable(Event event) {
		// remarks about the participant may always be shown
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
		Assert.state(participant.isParticipant(), "Person is no participant: " + participant);
		final ParticipantProfile profile = participant.getParticipantProfile();
		return profile.hasAdditionalRemarks() //
				? profile.getRemarks() //
				: "";
	}

}
