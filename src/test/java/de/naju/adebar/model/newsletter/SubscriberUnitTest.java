package de.naju.adebar.model.newsletter;

import org.junit.Test;
import org.springframework.util.Assert;

/**
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

    @Test public void testHasName() {
        Subscriber sub = new Subscriber("Claus", "", "claus@web.de");
        Assert.isTrue(sub.hasName());
    }

    @Test public void testHasNoName() {
        Subscriber sub = new Subscriber("", "", "claus@web.de");
        Assert.isTrue(!sub.hasName());
    }


}
