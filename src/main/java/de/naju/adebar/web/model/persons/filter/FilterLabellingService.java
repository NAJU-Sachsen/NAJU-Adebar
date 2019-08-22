package de.naju.adebar.web.model.persons.filter;

import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import de.naju.adebar.app.filter.AbstractFilter;
import de.naju.adebar.app.filter.AbstractFilterableField;
import de.naju.adebar.app.filter.ComparingFilter;
import de.naju.adebar.app.filter.InvertableFilter;

@Service
public class FilterLabellingService {

	private static final String FILTER_PREFIX = "filter.";
	private static final String MESSAGE_KEY_SEPARATOR = ".";
	private static final Object[] NO_ARGS = {};

	private final MessageSource messageSource;

	public FilterLabellingService(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public InvertableFilterLabel labelFor(InvertableFilter filter) {
		return new InvertableFilterLabel(filter);
	}

	public ComparingFilterLabel labelFor(ComparingFilter filter) {
		return new ComparingFilterLabel(filter);
	}

	public String title(AbstractFilterableField field) {
		return fetchFromMessageSource(FILTER_PREFIX + field.getName() + ".title");
	}

	private String fetchFromMessageSource(String code) {
		return messageSource.getMessage(code, NO_ARGS, LocaleContextHolder.getLocale());
	}

	private String fetchFromMessageSource(String code, String defaultCode) {
		try {
			return fetchFromMessageSource(code);
		} catch (NoSuchMessageException e) {
			return fetchFromMessageSource(defaultCode);
		}
	}

	private String buildFilterMessageCode(AbstractFilter filter) {
		return FILTER_PREFIX + filter.getFilteredField().getName() + MESSAGE_KEY_SEPARATOR;
	}

	public class InvertableFilterLabel {

		private static final String INACTIVE_DEFAULT = "filter.inverted.false";
		private static final String ACTIVE_DEFAULT = "filter.inverted.true";

		private final String inactive;
		private final String active;

		private InvertableFilterLabel(InvertableFilter filter) {
			final String messageCodePrefix = buildFilterMessageCode(filter);
			this.inactive = fetchFromMessageSource(messageCodePrefix + "false", INACTIVE_DEFAULT);
			this.active = fetchFromMessageSource(messageCodePrefix + "true", ACTIVE_DEFAULT);
		}

		public String inactive() {
			return inactive;
		}

		public String active() {
			return active;
		}

	}

	public class ComparingFilterLabel {

		private static final String MIN_DEFAULT = "filter.comparing.min";
		private static final String MAX_DEFAULT = "filter.comparing.max";

		private final String min;
		private final String max;

		private ComparingFilterLabel(ComparingFilter filter) {
			final String messageCodePrefix = buildFilterMessageCode(filter);
			this.min = fetchFromMessageSource(messageCodePrefix + "min", MIN_DEFAULT);
			this.max = fetchFromMessageSource(messageCodePrefix + "max", MAX_DEFAULT);
		}

		public String min() {
			return min;
		}

		public String max() {
			return max;
		}

	}

}
