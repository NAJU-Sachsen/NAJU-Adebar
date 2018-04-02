package de.naju.adebar.app.newsletter;

import com.google.common.collect.Iterables;
import de.naju.adebar.model.core.Email;
import de.naju.adebar.model.newsletter.AlreadySubscribedException;
import de.naju.adebar.model.newsletter.ExistingSubscriberException;
import de.naju.adebar.model.newsletter.Newsletter;
import de.naju.adebar.model.newsletter.NewsletterRepository;
import de.naju.adebar.model.newsletter.NoSuchSubscriberException;
import de.naju.adebar.model.newsletter.Subscriber;
import de.naju.adebar.model.newsletter.SubscriberRepository;
import de.naju.adebar.model.persons.Person;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * A {@link NewsletterManager} that persists the data in a database
 *
 * @author Rico Bergmann
 */
@Service
public class PersistentNewsletterManager implements NewsletterManager {

  private static final int INITIAL_NEWSLETTER_COUNT = 15;

  private NewsletterRepository newsletterRepo;
  private SubscriberRepository subscriberRepo;

  @Autowired
  public PersistentNewsletterManager(NewsletterRepository newsletterRepo,
      SubscriberRepository subscriberRepo) {
    this.newsletterRepo = newsletterRepo;
    this.subscriberRepo = subscriberRepo;
  }

  @Override
  public Newsletter saveNewsletter(Newsletter newsletter) {
    return newsletterRepo.save(newsletter);
  }

  @Override
  public Newsletter createNewsletter(String name) {
    Newsletter newsletter = new Newsletter(name);
    newsletterRepo.save(newsletter);
    return newsletter;
  }

  @Override
  public void deleteNewsletter(long id) {
    Newsletter newsletter = newsletterRepo.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("No newsletter with ID " + id));
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
      throw new AlreadySubscribedException(
          subscriber.toString() + " already subscribed to " + newsletter);
    }
    newsletter.addSubscriber(subscriber);
    newsletterRepo.save(newsletter);
  }

  @Override
  public void subscribePersonToNewsletter(Person person, Newsletter newsletter) {
    if (!person.hasEmail()) {
      throw new IllegalStateException("The person has no email-address: " + person);
    }
    Subscriber subscriber =
        new Subscriber(person.getFirstName(), person.getLastName(), person.getEmail());
    if (!subscriberRepo.findByEmail(person.getEmail()).isPresent()) {
      subscriber = subscriberRepo.save(subscriber);
    } else {
      if (!subscriber.equals(subscriberRepo.findById(subscriber.getId()).orElse(null))) {
        throw new ExistingSubscriberException(String.format(
            "There is already a subscriber with this email address, but different data: "
                + "existing: %s new: %s",
            subscriberRepo.findById(subscriber.getId()).orElse(null), subscriber), subscriber);
      }
    }

    newsletter.addSubscriber(subscriber);
    newsletterRepo.save(newsletter);
  }

  @Override
  public void updateSubscriptions(Subscriber subscriber, Iterable<Newsletter> newSubscriptions) {
    ArrayList<Newsletter> subscriptions = new ArrayList<>(INITIAL_NEWSLETTER_COUNT); // we will
    // store the
    // newsletters
    // to
    // deal with
    // here
    newSubscriptions.forEach(subscriptions::add);

    // remove old subscriptions
    for (Newsletter oldSubscription : newsletterRepo.findBySubscribersContains(subscriber)) {
      if (!subscriptions.contains(oldSubscription)) {
        oldSubscription.removeSubscriber(subscriber);
        newsletterRepo.save(oldSubscription);
      } else {
        // if the subscription should remain active, we do not have to
        // deal with this newsletter further
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

    // if this was the last newsletter the subscriber read, we will delete
    // it
    if (Iterables.isEmpty(newsletterRepo.findBySubscribersContains(subscriber))) {
      subscriberRepo.delete(subscriber);
    }
  }

  @Override
  public void unsubscribe(Email email, Newsletter newsletter) {
    Optional<Subscriber> subscriber = subscriberRepo.findByEmail(email);
    if (subscriber.isPresent()) {
      unsubscribe(subscriber.get(), newsletter);
    } else {
      throw new NoSuchSubscriberException("No subscriber registered for email: " + email);
    }
  }

}
