package de.naju.adebar.api.data;

import de.naju.adebar.model.newsletter.Subscriber;

/**
 * JSON-object for subscribers. In contrast to the normal {@link Subscriber}, this one does also
 * contain (technical) information about signed up newsletters - i.e. a list of newsletter id's.
 * 
 * @author Rico Bergmann
 */
public class TechnicalSubscriberJSON {

  private Subscriber subscriber;
  private Iterable<Long> subscribedNewsletters;

  /**
   * @param subscriber the normal subscriber object
   * @param subscribedNewsletters the newsletters the subscriber signed up to
   */
  public TechnicalSubscriberJSON(Subscriber subscriber, Iterable<Long> subscribedNewsletters) {
    this.subscriber = subscriber;
    this.subscribedNewsletters = subscribedNewsletters;
  }

  /**
   * @return the subscriber
   */
  public Subscriber getSubscriber() {
    return subscriber;
  }

  /**
   * @return the subscribed newsletters
   */
  public Iterable<Long> getSubscribedNewsletters() {
    return subscribedNewsletters;
  }

}
