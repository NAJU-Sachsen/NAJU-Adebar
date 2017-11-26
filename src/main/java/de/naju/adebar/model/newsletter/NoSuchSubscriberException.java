package de.naju.adebar.model.newsletter;

/**
 * Exception to indicate that the specified {@link Subscriber} does not exist
 * 
 * @author Rico Bergmann
 */
public class NoSuchSubscriberException extends RuntimeException {
  private static final long serialVersionUID = -7455697221282134967L;

  public NoSuchSubscriberException() {}

  public NoSuchSubscriberException(String message) {
    super(message);
  }

  public NoSuchSubscriberException(String message, Throwable cause) {
    super(message, cause);
  }

  public NoSuchSubscriberException(Throwable cause) {
    super(cause);
  }

  public NoSuchSubscriberException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
