package de.naju.adebar.services.conversion.core;

import de.naju.adebar.model.support.NumericEntityId;
import javax.annotation.Nonnull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public class NumericEntityIdConverter implements Converter<String, NumericEntityId> {

  @Override
  public NumericEntityId convert(@Nonnull String source) {
    if (source.isEmpty()) {
      return null;
    }
    return new NumericEntityId(Long.parseLong(source));
  }
}
