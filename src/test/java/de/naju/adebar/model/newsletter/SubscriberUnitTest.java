package de.naju.adebar.model.newsletter;

import static org.junit.jupiter.api.Assertions.assertThrows;

import de.naju.adebar.model.core.Email;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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

	@Test
	public void testInvalidEmail() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Subscriber(Email.of("notreallyanemail@.de"));
		});
	}

	@Test
	public void testHasName() {
		Subscriber sub = new Subscriber("Claus", "", Email.of("claus@web.de"));
		Assertions.assertTrue(sub.hasName(), "Subscriber should have a name");
	}

	@Test
	public void testHasNoName() {
		Subscriber sub = new Subscriber("", "", Email.of("claus@web.de"));
		Assertions.assertFalse(sub.hasName(), "Subscriber should not have a name");
	}

}
