package de.naju.adebar.model.newsletter;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import de.naju.adebar.app.newsletter.PersistentNewsletterManager;
import de.naju.adebar.app.newsletter.PersistentSubscriberManager;
import de.naju.adebar.model.core.Email;

/**
 * Basic behavior testing for the {@link PersistentNewsletterManager}
 *
 * @author Rico Bergmann
 */
@SpringBootTest
@Transactional
@Rollback
@Component
public class NewsletterIntegrationTest {

	@Autowired
	private PersistentNewsletterManager newsletterManager;

	@Autowired
	private PersistentSubscriberManager subscriberManager;

	@Autowired
	private NewsletterRepository newsletterRepo;

	@Autowired
	private SubscriberRepository subscriberRepo;

	private Newsletter hifaNewsletter;
	private Subscriber hans;

	@BeforeEach
	public void setUp() {
		hifaNewsletter = new Newsletter("HIFA");
		hans = new Subscriber("Hans", "Wurst", Email.of("hans.wurst@web.de"));

		newsletterRepo.save(hifaNewsletter);
		subscriberRepo.save(hans);
	}

	@Test
	public void testSubscription() {
		Assertions.assertFalse(hifaNewsletter.hasSubscriber(hans),
				String.format("%s should not have subscribed already", hans));
		newsletterManager.subscribe(hans, hifaNewsletter);
		Assertions.assertTrue(hifaNewsletter.hasSubscriber(hans),
				String.format("%s should have subscribed", hans));
	}

	@Test
	public void testDeletion() {
		newsletterManager.subscribe(hans, hifaNewsletter);
		newsletterManager.unsubscribe(hans, hifaNewsletter);
		Assertions.assertFalse(hifaNewsletter.hasSubscriber(hans),
				String.format("%s should have unsubscribed", hans));
	}

	/**
	 * deleting a newsletter will delete subscriber as well, if he has no other signed up newsletters
	 */
	@Test
	public void testNewsletterDeletion() {
		newsletterManager.subscribe(hans, hifaNewsletter);
		newsletterManager.deleteNewsletter(hifaNewsletter.getId());
		Assertions.assertFalse(subscriberRepo.existsById(hans.getId()),
				String.format("%s should be deleted", hans));
	}

	/**
	 * removing a subscriber from all newsletters will delete him
	 */
	@Test
	public void testUnsubscription() {
		newsletterManager.subscribe(hans, hifaNewsletter);
		newsletterManager.unsubscribe(hans, hifaNewsletter);
		Assertions.assertFalse(subscriberRepo.existsById(hans.getId()),
				String.format("%s should be deleted", hans));
	}

	@Test // #59
	public void multipleNewPersonsMaySubscribeToTheSameNewsletter() {
		final int ADDITIONAL_SUBS = 2;

		hifaNewsletter =
				newsletterRepo.findById(hifaNewsletter.getId()).orElseThrow(AssertionError::new);

		int currentSubs = hifaNewsletter.getSubscribersCount();
		int expectedSubs = currentSubs + ADDITIONAL_SUBS;

		Subscriber firstSub = new Subscriber("Hans", "Wurst", Email.of("hw@web.de"));
		Subscriber secondSub = new Subscriber("Berta", "Beate", Email.of("bbeate@gmx.de"));

		firstSub = subscriberManager.saveSubscriber(firstSub);
		secondSub = subscriberManager.saveSubscriber(secondSub);

		newsletterManager.subscribe(firstSub, hifaNewsletter);
		newsletterManager.subscribe(secondSub, hifaNewsletter);

		assertThat(hifaNewsletter.getSubscribersCount()).isEqualTo(expectedSubs);
	}

}
