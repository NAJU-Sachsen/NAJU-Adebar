package de.naju.adebar.services.conversion.app;

import org.springframework.core.convert.converter.Converter;
import de.naju.adebar.app.security.user.Username;

/**
 * Service to conveniently convert a {@code String} to corresponding {@link Username} instances.
 * 
 * @author Rico Bergmann
 */
public class UsernameConverter implements Converter<String, Username> {

  /*
   * (non-Javadoc)
   *
   * @see org.springframework.core.convert.converter.Converter#convert(java.lang.Object)
   */
  @Override
  public Username convert(String source) {
    if (source.isEmpty()) {
      return null;
    }
    return Username.of(source);
  }

}
