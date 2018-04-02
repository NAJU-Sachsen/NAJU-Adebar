package de.naju.adebar.model.core;

import de.naju.adebar.documentation.ddd.ValueObject;
import de.naju.adebar.util.Validation;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import org.springframework.util.Assert;

/**
 * Abstraction of a phone number. Basically just a wrapper for some String.
 * <p>
 * Each instance will try to unify the way the phone number is being represented - independently of
 * the format it as input.
 * <p>
 * Upon creation some basic sanity checks on the credibility of the phone number will be created,
 * however those may not give complete accuracy.
 *
 * @author Rico Bergmann
 */
@ValueObject
@Embeddable
public class PhoneNumber {

  public static final String FILLER = " ";
  public static final int MINIMUM_LENGTH = 10;
  public static final int DIALING_CODE_LENGTH = 4;

  @Column(name = "phone")
  private String value;

  /**
   * Just the constructor, nothing special about it.
   *
   * @param number the number
   * @throws IllegalArgumentException if the phone number is not valid
   */
  private PhoneNumber(String number) {
    assertValidNumber(number);
    this.value = normalizePhoneNumber(number);

  }

  /**
   * Default constructor just for JPA's sake
   */
  private PhoneNumber() {
  }

  /**
   * Creates a new phone number
   *
   * @param number the phone number
   * @return the wrapped number
   *
   * @throws IllegalArgumentException if the phone number is not valid
   */
  public static PhoneNumber of(String number) {
    return new PhoneNumber(number);
  }

  /**
   * Provides an equivalent representation of a phone number. All numbers will be formatted the same
   * way.
   *
   * @param rawNumber the number to format
   * @return the normalized number
   *
   * @throws IllegalArgumentException if the phone number is malformed
   */
  public static String normalizePhoneNumber(String rawNumber) {
    Validation.isPhoneNumber(rawNumber);
    StringBuilder normalizedNumber = extractDigitsFrom(rawNumber);

    Assert.isTrue(normalizedNumber.length() >= MINIMUM_LENGTH, "Phone number is too short");
    normalizedNumber = insertFillerInto(normalizedNumber);

    return normalizedNumber.toString();
  }

  /**
   * Removes all character from a phone number which do not carry information.
   *
   * @param rawNumber the number to modify
   * @return the formatted number
   */
  private static StringBuilder extractDigitsFrom(String rawNumber) {
    StringBuilder extractedNumber = new StringBuilder();
    for (int idx = 0; idx < rawNumber.length(); ++idx) {
      if (includeCharacter(rawNumber, idx)) {
        extractedNumber.append(rawNumber.charAt(idx));
      }
    }
    return extractedNumber;
  }

  /**
   * Inserts a separator between dialing and phone number.
   *
   * @param digitOnlyNumber the number to modify. It may only contain digits and optionally a
   *     leading '{@code +}' character
   * @return the pretty formatted number
   */
  private static StringBuilder insertFillerInto(StringBuilder digitOnlyNumber) {
    StringBuilder number = new StringBuilder(digitOnlyNumber);
    for (int idx = 0; idx < digitOnlyNumber.length(); ++idx) {
      if (shouldInsertFillerAfter(idx, digitOnlyNumber)) {
        ++idx;
        number.insert(idx, FILLER);
      }
    }
    return number;
  }

  /**
   * Checks, whether a character is just "filler" or actually part of the phone number itself.
   *
   * <p> Although the parameters seem a bit weird, it is necessary to provide both the number and
   * the character as the importance of a character depends on its context (e.g. a plus-symbol
   * ('{@code +}') is necessary if it appears as first digit but otherwise it is invalid. As each
   * character may appear multiple times, it is also necessary to know the exact position rather
   * than just the character by itself.
   *
   * @param rawPhoneNumber the number which contains the character
   * @param characterIdx the position of the character to check
   * @return whether the character is an integral part of the number
   */
  private static boolean includeCharacter(String rawPhoneNumber, int characterIdx) {
    return isLeadingPlus(rawPhoneNumber, characterIdx) //
        || Character.isDigit(rawPhoneNumber.charAt(characterIdx));
  }

  /**
   * Whether a filler character should be inserted after a given position
   *
   * @param idx the position
   * @param number the number to check
   * @return whether a filler should be inserted
   */
  private static boolean shouldInsertFillerAfter(int idx, CharSequence number) {
    int fillerOffset = DIALING_CODE_LENGTH;

    if (isInInternationalFormat(number)) {
      fillerOffset += 2;
    }

    // mind that a String's index starts at 0 - hence we got to add 1
    return idx + 1 == fillerOffset;
  }

  /**
   * Whether a phone number starts with a plus character ('{@code +}'). Mainly for idiomatic
   * reasons.
   *
   * @param number the number to check
   * @param characterIdx the position
   * @return whether the first character is a plus
   */
  @Transient
  private static boolean isLeadingPlus(String number, int characterIdx) {
    return characterIdx == 0 && number.charAt(characterIdx) == '+';
  }

  /**
   * @param number the number to check
   * @return whether it is in international format
   *
   * @see #isInInternationalFormat()
   */
  @Transient
  private static boolean isInInternationalFormat(CharSequence number) {
    return number.length() > 0 && number.charAt(0) == '+';
  }

  /**
   * @return the (now unified) phone number
   */
  public String getValue() {
    return value;
  }

  /**
   * @param number the phone number. Just for JPA's sake.
   */
  private void setValue(String number) {
    assertValidNumber(number);
    this.value = normalizePhoneNumber(number);
  }

  /**
   * @return whether the number is for mobile phones and in international format
   */
  @Transient
  public boolean isInInternationalFormat() {
    return isInInternationalFormat(value);
  }

  /**
   * @param number the number to check
   * @throws IllegalArgumentException if the phone number is too short or otherwise malformed
   */
  private void assertValidNumber(String number) {
    Assert.notNull(number, "number may not be null");
    Assert.isTrue(Validation.isPhoneNumber(number),
        "Apparently the number is not a valid phone number: " + number);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PhoneNumber that = (PhoneNumber) o;
    return Objects.equals(value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }

  @Override
  public String toString() {
    return value;
  }
}
