package de.naju.adebar.model.newsletter;

import de.naju.adebar.TestUtils;
import de.naju.adebar.model.core.Email;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Basic testing of the {@link Newsletter} class
 *
 * @author Rico Bergmann
 */
public class NewsletterUnitTets {

  private Newsletter hifaNewsletter;
  private Subscriber hans, berta, claus;

  @Before
  public void setUp() {
    hifaNewsletter = new Newsletter("HIFA");
    hans = new Subscriber("Hans", "Wurst", Email.of("hans.wurst@web.de"));
    berta = new Subscriber("Berta", "Beate", Email.of("bbeate@gmail.com"));
    claus = new Subscriber(Email.of("cccclllaaus@gmx.net"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateNameNull() {
    new Newsletter(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetNameNull() {
    hifaNewsletter.setName(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetSubscribersNull() {
    hifaNewsletter.setSubscribers(null);
  }

  @Test
  public void testAddValidSubscriber() {
    hifaNewsletter.addSubscriber(hans);
    Assert.assertTrue(String.format("%s should have been added!", hans),
        hifaNewsletter.hasSubscriber(hans));
  }

  @Test
  public void testAddMultipleSubscriber() {
    hifaNewsletter.addSubscriber(hans);
    hifaNewsletter.addSubscriber(berta);
    hifaNewsletter.addSubscriber(claus);
    Assert.assertTrue(String.format("%s should have been added!", hans),
        TestUtils.iterableContains(hifaNewsletter.getSubscribers(), hans));
    Assert.assertTrue(String.format("%s should have been added!", berta),
        TestUtils.iterableContains(hifaNewsletter.getSubscribers(), berta));
    Assert.assertTrue(String.format("%s should have been added!", claus),
        TestUtils.iterableContains(hifaNewsletter.getSubscribers(), claus));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddNullSubscriber() {
    hifaNewsletter.addSubscriber(null);
  }

  @Test(expected = AlreadySubscribedException.class)
  public void testAddSubscriberTwice() {
    hifaNewsletter.addSubscriber(hans);
    hifaNewsletter.addSubscriber(hans);
  }

  @Test
  public void testRemoveSubscriber() {
    hifaNewsletter.addSubscriber(hans);
    hifaNewsletter.removeSubscriber(hans);
    Assert.assertFalse(String.format("%s should have been removed!", hans),
        hifaNewsletter.hasSubscriber(hans));
  }
}
