package de.naju.adebar.app.newsletter;

import de.naju.adebar.model.core.Email;
import de.naju.adebar.model.newsletter.ExistingSubscriberException;
import de.naju.adebar.model.newsletter.Subscriber;
import org.springframework.stereotype.Service;

/**
 * Service to take care of {@link Subscriber Subscribers} and more complex management operations
 *
 * @author Rico Bergmann
 */
@Service
public interface SubscriberManager {

  /**
   * Saves a new subscriber
   * <p>
   * As the subscriber's data may overlap with an existing one, we need some rules:
   * <ul>
   * <li>If the email address is completely new, we will just add the new subscriber.</li>
   * <li>If the existing subscriber has no name, we will just use the new subscriber and use his
   * name. This will update all existing subscriptions.</li>
   * <li>If the existing subscriber has some parts of its name set, and the new one has as well, we
   * will reject the new subscriber. We may not determine for certain, whether it was a wrong input,
   * or whether the subscriber's data should be changed as well.</li>
   * <li>If the new subscriber has no name set, but the existing one has, we will use the existing
   * data.</li>
   * </ul>
   * It is important to remember that 'subscriber' usually applies to a {@link Subscriber} object
   * registered in the database and not to a person that subscribed to the specific newsletter.
   * </p>
   *
   * @param subscriber the subscriber to save
   * @return The saved subscriber object. It is very important to actually use this instance for
   *     future operations, as internal data may differ after saving the object.
   *
   * @throws ExistingSubscriberException if a subscriber does already exist and may not be
   *     harmonized with the new data
   */
  Subscriber saveSubscriber(Subscriber subscriber);

  /**
   * Creates a new subscriber object. The same rules as in {@link #saveSubscriber(Subscriber)} are
   * applied.
   *
   * @param firstName the subscriber's first name, may be empty
   * @param lastName the subscriber's last name, may be empty
   * @param email the subscriber's email
   * @return The saved subscriber object. It is very important to actually use this instance for
   *     future operations, as internal data may differ after saving the object.
   *
   * @see #saveSubscriber(Subscriber)
   */
  Subscriber createSubscriber(String firstName, String lastName, Email email);

  /**
   * Replaces a subscriber's data
   *
   * @param oldSubscriber the current subscriber
   * @param newSubscriber the new data for the subscriber
   * @return The updated subscriber. It is very important to actually use this instance for future
   *     operations, as internal data may differ after saving the object.
   */
  Subscriber updateSubscriber(Subscriber oldSubscriber, Subscriber newSubscriber);

  /**
   * Sets a subscriber's first name
   *
   * @param subscriber the subscriber
   * @param firstName the new first name
   * @return The updated subscriber. It is very important to actually use this instance for future
   *     operations, as internal data may differ after saving the object.
   */
  Subscriber updateSubscriberFirstName(Subscriber subscriber, String firstName);

  /**
   * Sets a subscriber's last name
   *
   * @param subscriber the subscriber
   * @param lastName the new last name
   * @return The updated subscriber. It is very important to actually use this instance for future
   *     operations, as internal data may differ after saving the object.
   */
  Subscriber updateSubscriberLastName(Subscriber subscriber, String lastName);

  /**
   * Sets a subscriber's email
   *
   * @param subscriber the subscriber
   * @param email the new email
   * @return The updated subscriber. It is very important to actually use this instance for future
   *     operations, as internal data may differ after saving the object.
   */
  Subscriber updateSubscriberEmail(Subscriber subscriber, Email email);

  /**
   * Removes a subscriber
   *
   * @param subscriber the subscriber to remove
   */
  void deleteSubscriber(Subscriber subscriber);

  /**
   * Removes a subscriber
   *
   * @param email the subscriber's email
   */
  void deleteSubscriber(Email email);

}
