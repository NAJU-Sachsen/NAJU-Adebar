package de.naju.adebar.util;

import java.util.regex.Pattern;
import org.springframework.util.Assert;

/**
 * Collection of simple validation methods
 * 
 * @author Rico Bergmann
 */
public class Validation {

  /**
   * Regular expression used to validate email addresses.
   */
  private final static Pattern EMAIL_REGEX =
      Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

  /**
   * Checks, whether the given text may be interpreted as a valid email address
   * 
   * @param text the text to check
   * @return {@code true} if the text is a valid email address and {@code false} otherwise
   * @see <a href="https://tools.ietf.org/html/rfc5322">RFC 5322</a>
   * @throws IllegalArgumentException if {@code text == null}
   */
  public static boolean isEmail(String text) {
    Assert.notNull(text, "Text to validate may not be null!");
    return EMAIL_REGEX.matcher(text).find();
  }

}
