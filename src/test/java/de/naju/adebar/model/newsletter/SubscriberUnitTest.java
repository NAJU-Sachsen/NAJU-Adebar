package de.naju.adebar.model.newsletter;

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
    new Subscriber("hans@web.de");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidEmail() {
    new Subscriber("notreallyanemail@.de");
  }

  @Test
  public void testHasName() {
    Subscriber sub = new Subscriber("Claus", "", "claus@web.de");
    Assert.assertTrue("Subscriber should have a name", sub.hasName());
  }

  @Test
  public void testHasNoName() {
    Subscriber sub = new Subscriber("", "", "claus@web.de");
    Assert.assertFalse("Subscriber should not have a name", sub.hasName());
  }


}
