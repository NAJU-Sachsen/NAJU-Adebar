package de.naju.adebar.util;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

/**
 * Basic testing of the {@link Validation} functions
 *
 * @author Rico Bergmann
 */
public class ValidationUnitTest {

  private String[] validEmails = {"max.muster@web.de", "max_muster@web.de", "max123@web.de",
      "max123max@web.de", "MAXIMUM@googlemail.com"};
  private String[] invalidEmails = {"@web.de", "max@web", "do$$ars@aol.com", "max@.de", "ab@bc.d"};

  private String[] validPhoneNumbers = {"+123456789", "+9876 54321", "+369-258741", "+8521/74126",
      "014752393987", "9874/321456", "6547-893210", "9632 147896"};

  private String[] extendedValidPhoneNumbers =
      {"+49 30 12345-67", "+49 30 1234567", "+49 (0)30 12345-67", "+49 (30) 12345 - 67"};
  private String[] invalidPhoneNumbers = {"++123456789", "2134a498", "1234   56978", "3692#58741",
      "741258//321469", "13254+78941", "123456"};

  @Test(expected = IllegalArgumentException.class)
  public void testNullEmail() {
    Validation.isEmail(null);
  }

  @Test
  public void testValidEmails() {
    for (String email : validEmails) {
      assertThat(Validation.isEmail(email)) //
          .describedAs("Email should be recognized as valid: %s", email) //
          .isTrue();
    }
  }

  @Test
  public void testInvalidEmails() {
    for (String email : invalidEmails) {
      assertThat(Validation.isEmail(email)) //
          .describedAs("Email not should be recognized as valid: %s", email) //
          .isFalse();
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullPhoneNumber() {
    Validation.isPhoneNumber(null);
  }

  @Test
  public void testValidPhoneNumbers() {
    for (String phone : validPhoneNumbers) {
      assertThat(Validation.isPhoneNumber(phone)) //
          .describedAs("Phone number should be recognized as valid: %s", phone) //
          .isTrue();
    }
  }

  @Test
  public void testInvalidPhoneNumbers() {
    for (String phone : invalidPhoneNumbers) {
      assertThat(Validation.isPhoneNumber(phone)) //
          .describedAs("Phone number should not be recognized as valid: %s", phone) //
          .isFalse();
    }
  }

  @Test
  public void testExtendedValidPhoneNumbers() {
    for (String phone : extendedValidPhoneNumbers) {
      assertThat(Validation.isPhoneNumber(phone)) //
          .describedAs("Phone number should be recognized as valid: %s", phone) //
          .isTrue();
    }
  }

}
