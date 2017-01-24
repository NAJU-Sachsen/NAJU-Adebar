package de.naju.adebar.model.newsletter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Optional;

import de.naju.adebar.util.Iteration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.naju.adebar.model.human.Person;

/**
 * A {@link NewsletterManager} that persists the data in a database
 * @author Rico Bergmann
 */
@Service
public class PersistentNewsletterManager implements NewsletterManager {
	private final static int INITIAL_NEWSLETTER_COUNT = 15;

	private NewsletterRepository newsletterRepo;
	private SubscriberRepository subscriberRepo;

	@Autowired
	public PersistentNewsletterManager(NewsletterRepository newsletterRepo, SubscriberRepository subscriberRepo) {
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
    public void deleteNewsletter(long id) {
	    Newsletter newsletter = newsletterRepo.findOne(id);
        LinkedList<Subscriber> subscribers = new LinkedList<>();
        newsletter.getSubscribers().forEach(subscribers::add);
	    for (Subscriber subscriber : subscribers) {
	        unsubscribe(subscriber, newsletter);
        }
        newsletterRepo.delete(newsletter);
    }

	@Override
	public void subscribe(Subscriber subscriber, Newsletter newsletter) {
		if (newsletter.hasSubscriber(subscriber)) {
			throw new AlreadySubscribedException(subscriber.toString() + " already subscribed to " + newsletter);
		}
		newsletter.addSubscriber(subscriber);
		newsletterRepo.save(newsletter);
	}
	
	@Override
	public void subscribePersonToNewsletter(Person person, Newsletter newsletter) {
		Subscriber subscriber = new Subscriber(person.getFirstName(), person.getLastName(), person.getEmail());
		if (!subscriberRepo.findByEmail(person.getEmail()).isPresent()) {
			subscriber = subscriberRepo.save(subscriber);
		} else {
			if (!subscriberRepo.findOne(subscriber.getId()).equals(subscriber)) {
				throw new ExistingSubscriberException(
						String.format("There is already a subscriber with this email address, but different data: " +
									"existing: %s new: %s", subscriberRepo.findOne(subscriber.getId()), subscriber));
			}
		}
		
		newsletter.addSubscriber(subscriber);
		newsletterRepo.save(newsletter);
	}

	@Override
	public void updateSubscriptions(Subscriber subscriber, Iterable<Newsletter> newSubscriptions) {
		ArrayList<Newsletter> subscriptions = new ArrayList<>(INITIAL_NEWSLETTER_COUNT); // we will store the newsletters to deal with here
		newSubscriptions.forEach(subscriptions::add);
		
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

	@Override
	public void unsubscribe(Subscriber subscriber, Newsletter newsletter) {
        newsletter.removeSubscriber(subscriber);
        newsletterRepo.save(newsletter);

        // if this was the last newsletter the subscriber read, we will delete it
        if (Iteration.isEmpty(newsletterRepo.findBySubscribersContains(subscriber))) {
            subscriberRepo.delete(subscriber);
        }
    }

    @Override
    public void unsubscribe(String email, Newsletter newsletter) {
        Optional<Subscriber> subscriber = subscriberRepo.findByEmail(email);
        if (subscriber.isPresent()) {
            unsubscribe(subscriber.get(), newsletter);
        } else {
            throw new NoSuchSubscriberException("No subscriber registered for email: " + email);
        }
    }

}
