package de.naju.adebar.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class PhoneNumberUnitTests {

  private String[] rawNumbers = {
      "+123456789", "+9876 54321", "+369-258741", "+8521/74126",
      "0147523930", "9874/321456", "6547-893210", "9632 147896",
      "+49 30 12345-67", "+49 30 1234567", "+49 (0)30 12345-67", "+49 (30) 12345 - 67",
      "0123 4567890"};
  private String[] normalizedNumbers = {
      "+12345 6789", "+98765 4321", "+36925 8741", "+85217 4126",
      "0147 523930", "9874 321456", "6547 893210", "9632 147896",
      "+49301 234567", "+49301 234567", "+49030 1234567", "+49301 234567",
      "0123 4567890"};

  @Test
  public void normalizesPhoneNumbersCorrectly() {
    for (int comparisonIdx = 0; comparisonIdx < rawNumbers.length; ++comparisonIdx) {
      PhoneNumber phoneNumber = new PhoneNumber(rawNumbers[comparisonIdx]);
      assertThat(phoneNumber.getNumber()).isEqualTo(normalizedNumbers[comparisonIdx]);
    }
  }

}
