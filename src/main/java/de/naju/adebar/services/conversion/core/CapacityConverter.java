package de.naju.adebar.services.conversion.core;

import de.naju.adebar.model.core.Capacity;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;

public class CapacityConverter implements Converter<String, Capacity> {

  @Override
  public Capacity convert(@NonNull String source) {
    if (source.isEmpty()) {
      return null;
    }

    return Capacity.of(Integer.parseInt(source));
  }
  
}
