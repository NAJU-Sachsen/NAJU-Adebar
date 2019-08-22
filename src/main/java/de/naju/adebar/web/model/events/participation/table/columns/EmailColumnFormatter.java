package de.naju.adebar.web.model.events.participation.table.columns;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import de.naju.adebar.model.events.Event;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.web.model.events.participation.table.ParticipantsTable;

/**
 * Formatter used in the {@link ParticipantsTable} to display the participants' email addresses.
 *
 * @author Rico Bergmann
 */
@Service
public class EmailColumnFormatter implements TableColumnFormatter {

	private final MessageSource messageSource;

	/**
	 * Constructs a new formatter.
	 *
	 * @param messageSource which contains default messages if a date of birth is not set. Must not be
	 *        {@code null}.
	 */
	public EmailColumnFormatter(MessageSource messageSource) {
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
		// the participant's email may always be formatted
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
		if (participant.hasEmail()) {
			return participant.getEmail().getValue();
		} else {
			return messageSource.getMessage("field.not-set", new Object[] {},
					LocaleContextHolder.getLocale());
		}
	}

}
