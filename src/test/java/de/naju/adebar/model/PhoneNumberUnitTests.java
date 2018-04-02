package de.naju.adebar.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import de.naju.adebar.model.core.PhoneNumber;
import org.junit.Test;

public class PhoneNumberUnitTests {

  /*
   * The validation tests here overlap with those described in the ValidationUnitTest. In fact they
   * are copy-pasta.
   *
   * This is mainly for regression testing.
   */

  private String[] validPhoneNumbers = {"+123456789", "+9876 54321", "+369-258741", "+8521/74126",
      "014752393987", "9874/321456", "6547-893210", "9632 147896"};
  private String[] extendedValidPhoneNumbers =
      {"+49 30 12345-67", "+49 30 1234567", "+49 (0)30 12345-67", "+49 (30) 12345 - 67"};
  private String[] invalidPhoneNumbers = {"++123456789", "2134a498", "1234   56978", "3692#58741",
      "741258//321469", "13254+78941", "123456"};

  private String[] rawNumbers = {"+123456789", "+9876 54321", "+369-258741", "+8521/74126",
      "0147523930", "9874/321456", "6547-893210", "9632 147896", "+49 30 12345-67",
      "+49 30 1234567", "+49 (0)30 12345-67", "+49 (30) 12345 - 67", "0123 4567890"};
  private String[] normalizedNumbers = {"+12345 6789", "+98765 4321", "+36925 8741", "+85217 4126",
      "0147 523930", "9874 321456", "6547 893210", "9632 147896", "+49301 234567", "+49301 234567",
      "+49030 1234567", "+49301 234567", "0123 4567890"};

  @Test
  public void acceptsValidPhoneNumbers() {
    for (String phone : validPhoneNumbers) {
      PhoneNumber.of(phone);
    }

    for (String phone : extendedValidPhoneNumbers) {
      PhoneNumber.of(phone);
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void rejectsNullPhoneNumber() {
    PhoneNumber.of(null);
  }

  @Test
  public void rejectsInvalidPhoneNumbers() {
    for (String phone : invalidPhoneNumbers) {
      assertThatIllegalArgumentException().isThrownBy(() -> PhoneNumber.of(phone));
    }
  }

  @Test
  public void normalizesPhoneNumbersCorrectly() {
    for (int comparisonIdx = 0; comparisonIdx < rawNumbers.length; ++comparisonIdx) {
      PhoneNumber phoneNumber = PhoneNumber.of(rawNumbers[comparisonIdx]);
      assertThat(phoneNumber.getValue()) //
          .describedAs("Phone number should be normalized") //
          .isEqualTo(normalizedNumbers[comparisonIdx]);

      assertThat(PhoneNumber.normalizePhoneNumber(rawNumbers[comparisonIdx])) //
          .describedAs("String should be normalized") //
          .isEqualTo(normalizedNumbers[comparisonIdx]);
    }
  }

}
