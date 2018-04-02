package de.naju.adebar.services.conversion.core;

import de.naju.adebar.model.core.PhoneNumber;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;

/**
 * Converter used to automatically provide required {@link PhoneNumber} instances when a String is
 * given instead.
 *
 * @author Rico Bergmann
 */
public class PhoneNumberConverter implements Converter<String, PhoneNumber> {

  /*
   * (non-Javadoc)
   *
   * @see org.springframework.core.convert.converter.Converter#convert(java.lang.Object)
   */
  @Override
  public PhoneNumber convert(@NonNull String source) {
    if (source.isEmpty()) {
      return null;
    }
    return PhoneNumber.of(source);
  }

}
