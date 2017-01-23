package de.naju.adebar.model.newsletter;

import de.naju.adebar.controller.NewsletterController;
import de.naju.adebar.model.newsletter.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        Assert.isTrue(!hifaNewsletter.hasSubscriber(hans), String.format("%s should not have subscribed already", hans));
        newsletterManager.subscribe(hans, hifaNewsletter);
        Assert.isTrue(hifaNewsletter.hasSubscriber(hans), String.format("%s should have subscribed", hans));
    }

    @Test public void testDeletion() {
        newsletterManager.subscribe(hans, hifaNewsletter);
        newsletterManager.unsubscribe(hans, hifaNewsletter);
        Assert.isTrue(!hifaNewsletter.hasSubscriber(hans), String.format("%s should have unsubscribed", hans));
    }

    /**
     * deleting a newsletter will delete subscriber as well, if he has no other signed up newsletters
     */
    @Test public void testNewsletterDeletion() {
        newsletterManager.subscribe(hans, hifaNewsletter);
        newsletterManager.deleteNewsletter(hifaNewsletter.getId());
        Assert.isTrue(!subscriberRepo.exists(hans.getId()), String.format("%s should be deleted", hans));
    }

    /**
     * removing a subscriber from all newsletters will delete him
     */
    @Test public void testUnsubscription() {
        newsletterManager.subscribe(hans, hifaNewsletter);
        newsletterManager.unsubscribe(hans, hifaNewsletter);
        Assert.isTrue(!subscriberRepo.exists(hans.getId()), String.format("%s should be deleted", hans));
    }

}
