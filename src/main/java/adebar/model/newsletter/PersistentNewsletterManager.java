package adebar.model.newsletter;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import adebar.model.human.Person;

@Service
public class PersistentNewsletterManager implements NewsletterManager {
	
	private NewsletterRepository newsletterRepo;
	private SubscriberRepository subscriberRepo;
	
	public PersistentNewsletterManager(@Autowired NewsletterRepository newsletterRepo, @Autowired SubscriberRepository subscriberRepo) {
		this.newsletterRepo = newsletterRepo;
		this.subscriberRepo = subscriberRepo;
	}
	
	@Override
	public Newsletter createNewsletter(String name) {
		Newsletter newsletter = new Newsletter(name);
		newsletterRepo.save(newsletter);
		return newsletter;
	}

	@Override
	public void subscribeToNewsletter(Person person, Newsletter newsletter) {
		Subscriber subscriber = null;
		subscriber = new Subscriber(person.getFirstName(), person.getLastName(), person.getEmail());
		if (!subscriberRepo.exists(person.getEmail())) {
			subscriber = subscriberRepo.save(subscriber);
		} else {
			if (!subscriberRepo.findOne(subscriber.getEmail()).equals(subscriber)) {
				throw new IllegalArgumentException(
						String.format("There is already a subscriber with this email address, but different data: " +
									"existing: %s new: %s", subscriberRepo.findOne(subscriber.getEmail()), subscriber));
			}
		}
		
		newsletter.addSubscriber(subscriber);
		newsletterRepo.save(newsletter);
	}

	@Override
	public Iterable<Newsletter> getNewslettersForName(String name) {
		return newsletterRepo.findByName(name);
	}

	@Override
	public void updateSubscriptions(Subscriber subscriber, Iterable<Newsletter> newSubscriptions) {
		ArrayList<Newsletter> subscriptions = new ArrayList<>(15); // we will store the newsletters to deal with here
		newSubscriptions.forEach(s -> subscriptions.add(s));
		
		// remove old subscriptions
		for (Newsletter oldSubscription : newsletterRepo.findBySubscribersContains(subscriber)) {
			if (!subscriptions.contains(oldSubscription)) {
				oldSubscription.removeSubscriber(subscriber);
				newsletterRepo.save(oldSubscription);
			} else {
				// if the subscription should remain active, we do not have to deal with this newsletter further
				subscriptions.remove(oldSubscription);
			}
		}
		
		// add new subscriptions
		for (Newsletter newSubscription : subscriptions) {
			newSubscription.addSubscriber(subscriber);
			newsletterRepo.save(newSubscription);
		}
	}

}
