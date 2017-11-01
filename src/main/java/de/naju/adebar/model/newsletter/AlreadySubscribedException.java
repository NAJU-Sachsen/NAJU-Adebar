package de.naju.adebar.model.newsletter;

/**
 * Exception to indicate, that a subscriber is already subscribed to a newsletter
 * 
 * @author Rico Bergmann
 */
public class AlreadySubscribedException extends RuntimeException {
  private static final long serialVersionUID = 566604261421931560L;

  public AlreadySubscribedException() {
    super();
  }

  public AlreadySubscribedException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public AlreadySubscribedException(String message, Throwable cause) {
    super(message, cause);
  }

  public AlreadySubscribedException(String message) {
    super(message);
  }

  public AlreadySubscribedException(Throwable cause) {
    super(cause);
  }

}
