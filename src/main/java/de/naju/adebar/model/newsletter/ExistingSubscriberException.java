package de.naju.adebar.model.newsletter;

/**
 * Exception to indicate that an email address is already associated to a subscriber
 *
 * @author Rico Bergmann
 */
public class ExistingSubscriberException extends RuntimeException {
  private static final long serialVersionUID = 680016776844043448L;

  private final Subscriber existingSubscriber;

  public ExistingSubscriberException(Subscriber theSubscriber) {
    super();
    this.existingSubscriber = theSubscriber;
  }

  public ExistingSubscriberException(String message, Subscriber theSubscriber) {
    super(message);
    this.existingSubscriber = theSubscriber;
  }

  public Subscriber getSubscriber() {
    return existingSubscriber;
  }

  @Override
  public String toString() {
    return super.toString() + " subscriber: " + existingSubscriber;
  }

}
