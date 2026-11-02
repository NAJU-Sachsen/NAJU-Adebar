package de.naju.adebar.web.model.events.participation.table.columns;

import de.naju.adebar.model.events.Event;
import de.naju.adebar.model.events.RegistrationInfo;
import de.naju.adebar.model.persons.Person;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class MarketingStatusColumnFormatter implements TableColumnFormatter {

	private final MessageSource messageSource;

	/**
	 * Constructs a new formatter.
	 *
	 * @param messageSource which contains default messages if a date of birth is not set. Must not be
	 *        {@code null}.
	 */
	public MarketingStatusColumnFormatter(MessageSource messageSource) {
		Assert.notNull(messageSource, "MessageSource may not be null");
		this.messageSource = messageSource;
	}

	@Override
	public boolean isApplicable(Event event) {
		return true;
	}

	@Override
	public String formatColumnFor(Person participant, Event event) {
		Assert.notNull(participant, "Participant may not be null");
		Assert.notNull(event, "Event may not be null");


		String code = participant.optedOutOfMarketing() ? "no" : "yes";
		return messageSource.getMessage(code, new Object[] {}, LocaleContextHolder.getLocale());
	}
}
