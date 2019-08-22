package de.naju.adebar.web.model.persons.filter;

import java.util.Locale;
import java.util.function.Function;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import de.naju.adebar.app.filter.AbstractFilterableField;
import de.naju.adebar.app.filter.EntityField;
import de.naju.adebar.app.filter.EnumeratedField;
import de.naju.adebar.util.Assert2;

@Service
public class WebFilterFieldFactory {

	private static final Object[] EMPTY_MSG_ARGS = {};

	private final MessageSource messageSource;
	private final Locale currentLocale;

	public WebFilterFieldFactory(MessageSource messageSource) {
		Assert2.noNullArguments("No argument may be null", messageSource);
		this.messageSource = messageSource;
		this.currentLocale = LocaleContextHolder.getLocale();
	}

	public WebEntityField enhance(EntityField field) {
		String dialogId = String.format("filter.%s.dialog", field.getName());
		String dialogTitle = messageSource.getMessage(dialogId, EMPTY_MSG_ARGS, currentLocale);

		String actionId = String.format("filter.%s.action", field.getName());
		String dialogAction = messageSource.getMessage(actionId, EMPTY_MSG_ARGS, currentLocale);

		return WebEntityField.augment(field, dialogTitle, dialogAction);
	}

	public WebEnumeratedField enhance(EnumeratedField field) {
		return new WebEnumeratedField(field, createEnumDisplayGeneratorFor(field));
	}

	public AbstractFilterableField enhanceIfNecessary(AbstractFilterableField field) {
		switch (field.getType()) {
			case ENUMERATED:
				return enhance((EnumeratedField) field);
			case ENTITY:
				return enhance((EntityField) field);
			default:
				return field;
		}
	}

	private Function<Object, String> createEnumDisplayGeneratorFor(EnumeratedField field) {
		String elemIdPrefix = String.format("filter.%s.", field.getName());
		return (elem) -> {
			return messageSource.getMessage(elemIdPrefix + field.getValueNameFor(elem), EMPTY_MSG_ARGS,
					currentLocale);
		};
	}


}
