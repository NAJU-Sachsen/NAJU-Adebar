package de.naju.adebar.services.conversion.core;

import org.springframework.core.convert.converter.Converter;
import de.naju.adebar.model.PhoneNumber;

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
  public PhoneNumber convert(String source) {
    if (source == null || source.isEmpty()) {
      return null;
    }
    return PhoneNumber.of(source);
  }

}
