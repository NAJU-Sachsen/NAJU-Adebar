package de.naju.adebar.model.newsletter;

/**
 * Exception to indicate that an email address is already associated to a subscriber
 *
 * @author Rico Bergmann
 */
public class ExistingSubscriberException extends RuntimeException {

  private static final long serialVersionUID = 680016776844043448L;

  private Subscriber existingSubscriber;

  public ExistingSubscriberException() {
    super();
  }

  public ExistingSubscriberException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public ExistingSubscriberException(String message, Throwable cause) {
    super(message, cause);
  }

  public ExistingSubscriberException(String message) {
    super(message);
  }

  public ExistingSubscriberException(Throwable cause) {
    super(cause);
  }

  /**
   * @return the existing subscriber
   */
  public Subscriber getExistingSubscriber() {
    return existingSubscriber;
  }

  /**
   * @param existingSubscriber the subscriber that does already exist
   */
  public void setExistingSubscriber(Subscriber existingSubscriber) {
    this.existingSubscriber = existingSubscriber;
  }


}
