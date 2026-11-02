package de.naju.adebar.web.model.events.participation.table.columns;

import de.naju.adebar.model.events.Event;
import de.naju.adebar.model.persons.ParticipantProfile;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.web.model.events.participation.table.ParticipantsTable;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Formatter used in the {@link ParticipantsTable} to display participants' ages.
 *
 * @author Rico Bergmann
 */
@Service
public class AgeColumnFormatter implements TableColumnFormatter {

	private final MessageSource messageSource;

	/**
	 * Constructs a new formatter.
	 *
	 * @param messageSource which contains default messages if a date of birth is not set. Must not be
	 *        {@code null}.
	 */
	public AgeColumnFormatter(MessageSource messageSource) {
		Assert.notNull(messageSource, "Message source may not be null");
		this.messageSource = messageSource;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.naju.adebar.web.model.events.participation.table.columns.TableColumnFormatter#isApplicable(
	 * de.naju.adebar.model.events.Event)
	 */
	@Override
	public boolean isApplicable(Event event) {
		// the participant's age may always be formatted (even if it is unknown)
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
		Assert.state(participant.isParticipant(), "Person is not participant: " + participant);
		final ParticipantProfile profile = participant.getParticipantProfile();
		if (!profile.hasDateOfBirth()) {
			return messageSource.getMessage("field.unknown", new Object[] {},
					LocaleContextHolder.getLocale());
		}
		return profile.calculateAgeOn(event.getStartTime().toLocalDate()).toString();
	}

}
