package de.naju.adebar.services.conversion.core;

import de.naju.adebar.model.events.ArrivalOption;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class ArrivalOptionConverter implements Converter<String, ArrivalOption> {

	@Override
	public ArrivalOption convert(@NonNull String source) {
		if (source.isEmpty()) {
			return null;
		}
		return ArrivalOption.of(source);
	}

}
