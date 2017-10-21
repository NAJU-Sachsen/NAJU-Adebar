package de.naju.adebar.util;

import org.junit.Test;
import org.springframework.util.Assert;

/**
 * Basic testing of the {@link Validation} functions
 *
 * @author Rico Bergmann
 */
public class ValidationUnitTest {

  private String[] validEmails = {"max.muster@web.de", "max_muster@web.de", "max123@web.de",
      "max123max@web.de", "MAXIMUM@googlemail.com"};
  private String[] invalidEmails = {"@web.de", "max@web", "do$$ars@aol.com", "max@.de", "ab@bc.d"};

  @Test(expected = IllegalArgumentException.class)
  public void testNullEmail() {
    Validation.isEmail(null);
  }

  @Test
  public void testValidEmails() {
    for (String email : validEmails) {
      Assert.isTrue(Validation.isEmail(email), "Should be a valid email: " + email);
    }
  }

  @Test
  public void testInvalidEmails() {
    for (String email : invalidEmails) {
      Assert.isTrue(!Validation.isEmail(email),
          "Email should not be recognized as valid: " + email);
    }
  }

}
