package de.naju.adebar.web.model.events.participation.table.columns;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import de.naju.adebar.model.events.Event;
import de.naju.adebar.model.persons.ParticipantProfile;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.web.model.events.participation.table.ParticipantsTable;

/**
 * Formatter used in the {@link ParticipantsTable} to display the NABU membership status of the
 * participants.
 *
 * @author Rico Bergmann
 */
@Service
public class NabuMembershipColumnFormatter implements TableColumnFormatter {

	private final MessageSource messageSource;

	/**
	 * Constructs a new formatter.
	 *
	 * @param messageSource which contains default messages if addresses are empty. Must not be
	 *        {@code null}.
	 */
	public NabuMembershipColumnFormatter(MessageSource messageSource) {
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
		// the participant's may always be shown
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

		if (profile.isNabuMember()) {
			return messageSource.getMessage("participant.nabu-member", new Object[] {},
					LocaleContextHolder.getLocale());
		} else if (profile.isNabuMembershipUnknown()) {
			return messageSource.getMessage("field.unknown", new Object[] {},
					LocaleContextHolder.getLocale());
		} else {
			return messageSource.getMessage("participant.nabu-member.false", new Object[] {},
					LocaleContextHolder.getLocale());
		}
	}

}
