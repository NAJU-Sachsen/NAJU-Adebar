package de.naju.adebar.app.newsletter;

import de.naju.adebar.model.core.Email;
import de.naju.adebar.model.newsletter.Newsletter;
import de.naju.adebar.model.newsletter.Subscriber;
import de.naju.adebar.model.persons.Person;
import org.springframework.stereotype.Service;

/**
 * Service to take care of {@link Newsletter Newsletters} and more complex management operations
 *
 * @author Rico Bergmann
 */
@Service
public interface NewsletterManager {

  /**
   * Saves the given newsletter. It may or may not be saved already
   *
   * @param newsletter the newsletter to save
   * @return the saved newsletter. As its internal state (especially concrete implementations of
   *     interfaces) may differ after the save, this instance should be used for future operations
   */
  Newsletter saveNewsletter(Newsletter newsletter);

  /**
   * Creates a new newsletter
   *
   * @param name the newsletter's name
   * @return the freshly created newsletter instance
   */
  Newsletter createNewsletter(String name);

  /**
   * Removes a newsletter
   *
   * @param id the newsletter's id
   */
  void deleteNewsletter(long id);

  /**
   * Adds a subscription to a newsletter
   *
   * @param subscriber the subscriber
   * @param newsletter the newsletter
   */
  void subscribe(Subscriber subscriber, Newsletter newsletter);

  /**
   * Subscribes a {@link Person} to a newsletter
   *
   * @param person the subscribing person
   * @param newsletter the newsletter to subscribe to
   */
  void subscribePersonToNewsletter(Person person, Newsletter newsletter);

  /**
   * Removes a subscription from a newsletter
   *
   * @param subscriber the subscriber to remove
   * @param newsletter the subscribed newsletter
   */
  void unsubscribe(Subscriber subscriber, Newsletter newsletter);

  /**
   * Removes a subscription from a newsletter
   *
   * @param email the email of the subscriber to remove
   * @param newsletter the subscribed newsletter
   */
  void unsubscribe(Email email, Newsletter newsletter);

  /**
   * Signs the subscribed up to the given newsletters. This will remove the subscriptions from
   * newsletters that are not specified
   *
   * @param subscriber the subscriber to update
   * @param subscriptions the new subscriptions
   */
  void updateSubscriptions(Subscriber subscriber, Iterable<Newsletter> subscriptions);
}
