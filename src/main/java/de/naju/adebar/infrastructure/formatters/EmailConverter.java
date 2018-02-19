package de.naju.adebar.infrastructure.formatters;

import org.springframework.core.convert.converter.Converter;
import de.naju.adebar.model.Email;

/**
 * Converter used to automatically provide required {@link Email} instances when a String is given
 * instead.
 *
 * @author Rico Bergmann
 */
public class EmailConverter implements Converter<String, Email> {

  /*
   * (non-Javadoc)
   *
   * @see org.springframework.core.convert.converter.Converter#convert(java.lang.Object)
   */
  @Override
  public Email convert(String source) {
    return Email.of(source);
  }



}
