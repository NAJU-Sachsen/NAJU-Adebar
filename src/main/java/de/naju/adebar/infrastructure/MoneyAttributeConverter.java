package de.naju.adebar.infrastructure;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.javamoney.moneta.Money;

/**
 * @author Rico Bergmann
 */
@Converter(autoApply = true)
public class MoneyAttributeConverter implements AttributeConverter<Money, String> {

	@Override
	public String convertToDatabaseColumn(Money attribute) {
		if (attribute == null) {
			return null;
		}
		return attribute.toString();
	}

	@Override
	public Money convertToEntityAttribute(String dbData) {
		if (dbData == null || dbData.isEmpty()) {
			return null;
		}
		return Money.parse(dbData);
	}
}
