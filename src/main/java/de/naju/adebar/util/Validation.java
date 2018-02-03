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
  public static final Pattern EMAIL_REGEX =
      Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

  /**
   * Regular expression used to validate phone numbers.
   *
   * <p>
   * A valid phone number may start with a dialing, followed by a sequence of numeric characters
   * which may be grouped by parentheses, white space, hyphen and slashes.
   *
   * <p>
   * The pattern also exposes two groups: the '{@code dialing}' exposes the first part of the
   * number, potentially led by a plus character. The '{@code number}' groups the rest of the phone
   * number.
   */
  public static final Pattern PHONE_REGEX = Pattern.compile( //
      "^(?<dialing>\\+?\\d*)\\s?(?<number>(\\(\\d{1,2}\\))?(([\\s-/]|(\\s-\\s))?(\\d+))+)$");

  public static final int PHONE_MIN_LENGTH = 10;

  private Validation() {}

  /**
   * Checks, whether the given text may be interpreted as a valid email address
   *
   * @param text the text to check
   * @return {@code true} if the text is a valid email address and {@code false} otherwise
   * @throws IllegalArgumentException if {@code text == null}
   * @see <a href="https://tools.ietf.org/html/rfc5322">RFC 5322</a>
   */
  public static boolean isEmail(String text) {
    Assert.notNull(text, "Text to validate may not be null!");
    return EMAIL_REGEX.matcher(text).find();
  }

  /**
   * Checks, whether the given text may be interpreted as a valid phone number
   *
   * <p>
   * This task actually is much harder than it seems - different countries present phone numbers in
   * different formats. Additionally the same phone number may be represented differently even
   * within the same country and multiple "standards" exist. Therefore this check may only be an
   * approximation and no absolute guarantee may be given.
   *
   * @param number the text to check
   * @return {@code true} if the text is a valid phone number or {@code false} otherwise
   * @throws IllegalArgumentException if {@code text == null}
   * @see #PHONE_REGEX
   */
  public static boolean isPhoneNumber(String number) {
    Assert.notNull(number, "Number to validate may not be null!");
    return number.length() >= PHONE_MIN_LENGTH //
        && PHONE_REGEX.matcher(number).find();
  }

}
