package de.naju.adebar.model.newsletter;

import de.naju.adebar.model.core.Email;
import org.junit.Assert;
import org.junit.Test;

/**
 * Basic testing of the {@link Subscriber} class
 *
 * @author Rico Bergmann
 */
public class SubscriberUnitTest {

  @Test
  public void testValidEmail() {
    new Subscriber(Email.of("hans@web.de"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidEmail() {
    new Subscriber(Email.of("notreallyanemail@.de"));
  }

  @Test
  public void testHasName() {
    Subscriber sub = new Subscriber("Claus", "", Email.of("claus@web.de"));
    Assert.assertTrue("Subscriber should have a name", sub.hasName());
  }

  @Test
  public void testHasNoName() {
    Subscriber sub = new Subscriber("", "", Email.of("claus@web.de"));
    Assert.assertFalse("Subscriber should not have a name", sub.hasName());
  }

}
