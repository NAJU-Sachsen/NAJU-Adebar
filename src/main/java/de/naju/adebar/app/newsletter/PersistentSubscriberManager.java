package de.naju.adebar.app.newsletter;

import de.naju.adebar.model.core.Email;
import de.naju.adebar.model.newsletter.ExistingSubscriberException;
import de.naju.adebar.model.newsletter.NoSuchSubscriberException;
import de.naju.adebar.model.newsletter.Subscriber;
import de.naju.adebar.model.newsletter.SubscriberRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * A {@link SubscriberManager} that persists the data in a database
 *
 * @author Rico Bergmann
 */
@Service
public class PersistentSubscriberManager implements SubscriberManager {

  private SubscriberRepository subscriberRepo;

  @Autowired
  public PersistentSubscriberManager(SubscriberRepository subscriberRepo) {
    this.subscriberRepo = subscriberRepo;
  }

  @Override
  public Subscriber saveSubscriber(Subscriber subscriber) {
    Optional<Subscriber> existingSubscriber = subscriberRepo.findByEmail(subscriber.getEmail());

    if (existingSubscriber.isPresent()) {
      if (existingSubscriber.get().equals(subscriber)) {
        return existingSubscriber.get();
      } else if (existingSubscriber.get().hasName() && subscriber.hasName()) {
        throw new ExistingSubscriberException(String.format(
            "Cannot create subscriber %s: "
                + "There is already a subscriber with the same email but different data: %s",
            subscriber, existingSubscriber), existingSubscriber.get());
      } else if (subscriber.hasName() && !existingSubscriber.get().hasName()) {
        return updateSubscriber(existingSubscriber.get(), subscriber);
      } else {
        return existingSubscriber.get();
      }
    }

    subscriberRepo.save(subscriber);
    return subscriber;
  }

  @Override
  public Subscriber createSubscriber(String firstName, String lastName, Email email) {
    return saveSubscriber(new Subscriber(firstName, lastName, email));
  }

  @Override
  public Subscriber updateSubscriber(Subscriber oldSubscriber, Subscriber newSubscriber) {
    if (newSubscriber.equals(oldSubscriber)) {
      return oldSubscriber;
    }

    if (!oldSubscriber.getEmail().equals(newSubscriber.getEmail())) {
      subscriberRepo.delete(oldSubscriber);
      newSubscriber = subscriberRepo.save(newSubscriber);
      return newSubscriber;
    }

    if (!oldSubscriber.getFirstName().equals(newSubscriber.getFirstName())) {
      oldSubscriber.setFirstName(newSubscriber.getFirstName());
    }
    if (!oldSubscriber.getLastName().equals(newSubscriber.getLastName())) {
      oldSubscriber.setLastName(newSubscriber.getLastName());
    }
    return subscriberRepo.save(oldSubscriber);
  }

  @Override
  public Subscriber updateSubscriberFirstName(Subscriber subscriber, String firstName) {
    if (!firstName.equals(subscriber.getFirstName())) {
      subscriber.setFirstName(firstName);
      return subscriberRepo.save(subscriber);
    }
    return subscriber;
  }

  @Override
  public Subscriber updateSubscriberLastName(Subscriber subscriber, String lastName) {
    if (!lastName.equals(subscriber.getLastName())) {
      subscriber.setLastName(lastName);
      return subscriberRepo.save(subscriber);
    }
    return subscriber;
  }

  @Override
  public Subscriber updateSubscriberEmail(Subscriber subscriber, Email email) {
    if (!email.equals(subscriber.getEmail())) {
      subscriber.setEmail(email);
      return subscriberRepo.save(subscriber);
    }
    return subscriber;
  }

  @Override
  public void deleteSubscriber(Subscriber subscriber) {
    subscriberRepo.delete(subscriber);
  }

  @Override
  public void deleteSubscriber(Email email) {
    Optional<Subscriber> subscriber = subscriberRepo.findByEmail(email);
    if (subscriber.isPresent()) {
      subscriberRepo.delete(subscriber.get());
    } else {
      throw new NoSuchSubscriberException("There is no subscriber with email: " + email);
    }
  }

}
