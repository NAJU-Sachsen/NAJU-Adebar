package de.naju.adebar.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import de.naju.adebar.model.core.Email;
import org.junit.Test;

public class EmailUnitTests {

  /*
   * The validation tests here overlap with those described in the ValidationUnitTest. In fact they
   * are copy-pasta.
   *
   * This is mainly for regression testing - and pretty bad style..
   */

  private String[] validEmails = {"max.muster@web.de", "max_muster@web.de", "max123@web.de",
      "max123max@web.de", "MAXIMUM@googlemail.com"};

  private String[] invalidEmails = {"@web.de", "max@web", "do$$ars@aol.com", "max@.de", "ab@bc.d"};

  @Test
  public void acceptsValidEmails() {
    for (String email : validEmails) {
      Email.of(email);
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void rejectsNull() {
    Email.of(null);
  }

  @Test
  public void rejectsInvalidEmails() {
    for (String email : invalidEmails) {
      assertThatIllegalArgumentException().isThrownBy(() -> Email.of(email));
    }
  }

  @Test
  public void normalizesEmails() {
    Email lowercase = Email.of("max.muster@web.de");
    Email camelCase = Email.of("Max.Muster@web.de");

    assertThat(lowercase).isEqualTo(camelCase);
  }

}
