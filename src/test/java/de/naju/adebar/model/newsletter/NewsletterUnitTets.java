package de.naju.adebar.model.newsletter;

import de.naju.adebar.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.Assert;

/**
 * @author Rico Bergmann
 */
public class NewsletterUnitTets {
    private Newsletter hifaNewsletter;
    private Subscriber hans, berta, claus;

    @Before
    public void setUp() {
        hifaNewsletter = new Newsletter("HIFA");
        hans = new Subscriber("Hans", "Wurst", "hans.wurst@web.de");
        berta = new Subscriber("Berta", "Beate", "bbeate@gmail.com");
        claus = new Subscriber("cccclllaaus@gmx.net");
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

    @Test public void testAddValidSubscriber() {
        hifaNewsletter.addSubscriber(hans);
        Assert.isTrue(hifaNewsletter.hasSubscriber(hans), String.format("%s should have been added!", hans));
    }

    @Test public void testAddMultipleSubscriber() {
        hifaNewsletter.addSubscriber(hans);
        hifaNewsletter.addSubscriber(berta);
        hifaNewsletter.addSubscriber(claus);
        Assert.isTrue(TestUtils.iterableContains(hifaNewsletter.getSubscribers(), hans), String.format("%s should have been added!", hans));
        Assert.isTrue(TestUtils.iterableContains(hifaNewsletter.getSubscribers(), berta), String.format("%s should have been added!", berta));
        Assert.isTrue(TestUtils.iterableContains(hifaNewsletter.getSubscribers(), claus), String.format("%s should have been added!", claus));
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
        Assert.isTrue(!hifaNewsletter.hasSubscriber(hans), String.format("%s should have been removed!", hans));
    }
}
