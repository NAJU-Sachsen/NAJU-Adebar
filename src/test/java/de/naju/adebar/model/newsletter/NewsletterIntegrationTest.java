package de.naju.adebar.model.newsletter;

import de.naju.adebar.app.newsletter.PersistentNewsletterManager;
import de.naju.adebar.model.core.Email;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Basic behavior testing for the {@link PersistentNewsletterManager}
 *
 * @author Rico Bergmann
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback
@Component
public class NewsletterIntegrationTest {

  @Autowired
  private PersistentNewsletterManager newsletterManager;
  @Autowired
  private NewsletterRepository newsletterRepo;
  @Autowired
  private SubscriberRepository subscriberRepo;
  private Newsletter hifaNewsletter;
  private Subscriber hans;

  @Before
  public void setUp() {
    hifaNewsletter = new Newsletter("HIFA");
    hans = new Subscriber("Hans", "Wurst", Email.of("hans.wurst@web.de"));

    newsletterRepo.save(hifaNewsletter);
    subscriberRepo.save(hans);
  }

  @Test
  public void testSubscription() {
    Assert.assertFalse(String.format("%s should not have subscribed already", hans),
        hifaNewsletter.hasSubscriber(hans));
    newsletterManager.subscribe(hans, hifaNewsletter);
    Assert.assertTrue(String.format("%s should have subscribed", hans),
        hifaNewsletter.hasSubscriber(hans));
  }

  @Test
  public void testDeletion() {
    newsletterManager.subscribe(hans, hifaNewsletter);
    newsletterManager.unsubscribe(hans, hifaNewsletter);
    Assert.assertFalse(String.format("%s should have unsubscribed", hans),
        hifaNewsletter.hasSubscriber(hans));
  }

  /**
   * deleting a newsletter will delete subscriber as well, if he has no other signed up newsletters
   */
  @Test
  public void testNewsletterDeletion() {
    newsletterManager.subscribe(hans, hifaNewsletter);
    newsletterManager.deleteNewsletter(hifaNewsletter.getId());
    Assert.assertFalse(String.format("%s should be deleted", hans),
        subscriberRepo.existsById(hans.getId()));
  }

  /**
   * removing a subscriber from all newsletters will delete him
   */
  @Test
  public void testUnsubscription() {
    newsletterManager.subscribe(hans, hifaNewsletter);
    newsletterManager.unsubscribe(hans, hifaNewsletter);
    Assert.assertFalse(String.format("%s should be deleted", hans),
        subscriberRepo.existsById(hans.getId()));
  }

}
