package de.naju.adebar.model.newsletter;

import static org.junit.jupiter.api.Assertions.assertThrows;

import de.naju.adebar.TestUtils;
import de.naju.adebar.model.core.Email;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Basic testing of the {@link Newsletter} class
 *
 * @author Rico Bergmann
 */
public class NewsletterUnitTets {

	private Newsletter hifaNewsletter;
	private Subscriber hans, berta, claus;

	@BeforeEach
	public void setUp() {
		hifaNewsletter = new Newsletter("HIFA");
		hans = new Subscriber("Hans", "Wurst", Email.of("hans.wurst@web.de"));
		berta = new Subscriber("Berta", "Beate", Email.of("bbeate@gmail.com"));
		claus = new Subscriber(Email.of("cccclllaaus@gmx.net"));
	}

	@Test
	public void testCreateNameNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Newsletter(null);
		});
	}

	@Test
	public void testSetNameNull() {
		assertThrows(IllegalArgumentException.class, () ->
			hifaNewsletter.setName(null));
	}

	@Test
	public void testSetSubscribersNull() {
		assertThrows(IllegalArgumentException.class, () ->
			hifaNewsletter.setSubscribers(null));
	}

	@Test
	public void testAddValidSubscriber() {
		hifaNewsletter.addSubscriber(hans);
		Assertions.assertTrue(hifaNewsletter.hasSubscriber(hans),
				String.format("%s should have been added!", hans));
	}

	@Test
	public void testAddMultipleSubscriber() {
		hifaNewsletter.addSubscriber(hans);
		hifaNewsletter.addSubscriber(berta);
		hifaNewsletter.addSubscriber(claus);
		Assertions.assertTrue(TestUtils.iterableContains(hifaNewsletter.getSubscribers(), hans),
				String.format("%s should have been added!", hans));
		Assertions.assertTrue(TestUtils.iterableContains(hifaNewsletter.getSubscribers(), berta),
				String.format("%s should have been added!", berta));
		Assertions.assertTrue(TestUtils.iterableContains(hifaNewsletter.getSubscribers(), claus),
				String.format("%s should have been added!", claus));
	}

	@Test
	public void testAddNullSubscriber() {
		assertThrows(IllegalArgumentException.class, () ->
			hifaNewsletter.addSubscriber(null));
	}

	@Test
	public void testAddSubscriberTwice() {
		assertThrows(AlreadySubscribedException.class, () -> {
			hifaNewsletter.addSubscriber(hans);
			hifaNewsletter.addSubscriber(hans);
		});
	}

	@Test
	public void testRemoveSubscriber() {
		hifaNewsletter.addSubscriber(hans);
		hifaNewsletter.removeSubscriber(hans);
		Assertions.assertFalse(hifaNewsletter.hasSubscriber(hans),
				String.format("%s should have been removed!", hans));
	}
}
