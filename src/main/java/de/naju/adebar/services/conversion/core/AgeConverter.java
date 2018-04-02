package de.naju.adebar.services.conversion.core;

import de.naju.adebar.model.core.Age;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;

/**
 * Converter used to provide {@link Age} instances when a String is given instead
 *
 * @author Rico Bergmann
 */
public class AgeConverter implements Converter<String, Age> {

  /*
   * (non-Javadoc)
   *
   * @see org.springframework.core.convert.converter.Converter#convert(java.lang.Object)
   */
  @Override
  public Age convert(@NonNull String source) {
    if (source.isEmpty()) {
      return null;
    }
    Integer val = Integer.parseInt(source);
    return Age.of(val);
  }

}
