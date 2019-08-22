package de.naju.adebar.services.conversion.core;

import org.javamoney.moneta.Money;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;

public class MoneyConverter implements Converter<String, Money> {

	@Override
	public Money convert(@NonNull String source) {
		if (source.isEmpty()) {
			return null;
		}
		return Money.parse("EUR " + source);
	}
}
