package de.naju.adebar.model.newsletter;

import de.naju.adebar.controller.NewsletterController;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

/**
 * Basic behavior testing for the @{link {@link NewsletterController}}
 * @author Rico Bergmann
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class NewsletterIntegrationTest {

    @Autowired private NewsletterManager newsletterManager;
    @Autowired private SubscriberManager subscriberManager;
    @Autowired private NewsletterRepository newsletterRepo;
    @Autowired private SubscriberRepository subscriberRepo;
    private Newsletter hifaNewsletter, bwcNewsletter;
    private Subscriber hans, berta, claus;

    @Before public void setUp() {
        hifaNewsletter = new Newsletter("HIFA");
        hans = new Subscriber("Hans", "Wurst", "hans.wurst@web.de");
        berta = new Subscriber("Berta", "Beate", "bbeate@gmail.com");
        claus = new Subscriber("cccclllaaus@gmx.net");

        newsletterRepo.save(hifaNewsletter);
        subscriberRepo.save(Arrays.asList(new Subscriber[]{hans, berta, claus}));
    }

    @Test public void testSubscription() {
        Assert.assertFalse(String.format("%s should not have subscribed already", hans), hifaNewsletter.hasSubscriber(hans));
        newsletterManager.subscribe(hans, hifaNewsletter);
        Assert.assertTrue(String.format("%s should have subscribed", hans), hifaNewsletter.hasSubscriber(hans));
    }

    @Test public void testDeletion() {
        newsletterManager.subscribe(hans, hifaNewsletter);
        newsletterManager.unsubscribe(hans, hifaNewsletter);
        Assert.assertFalse(String.format("%s should have unsubscribed", hans), hifaNewsletter.hasSubscriber(hans));
    }

    /**
     * deleting a newsletter will delete subscriber as well, if he has no other signed up newsletters
     */
    @Test public void testNewsletterDeletion() {
        newsletterManager.subscribe(hans, hifaNewsletter);
        newsletterManager.deleteNewsletter(hifaNewsletter.getId());
        Assert.assertFalse(String.format("%s should be deleted", hans), subscriberRepo.exists(hans.getId()));
    }

    /**
     * removing a subscriber from all newsletters will delete him
     */
    @Test public void testUnsubscription() {
        newsletterManager.subscribe(hans, hifaNewsletter);
        newsletterManager.unsubscribe(hans, hifaNewsletter);
        Assert.assertFalse(String.format("%s should be deleted", hans), subscriberRepo.exists(hans.getId()));
    }

}
