package de.naju.adebar.model.events;

/**
 * Exception to indicate that the event is booked out - i. e. no more persons may participate
 * 
 * @author Rico Bergmann
 */
public class BookedOutException extends RuntimeException {
  private static final long serialVersionUID = 1129424221360681755L;

  public BookedOutException() {}

  public BookedOutException(String message) {
    super(message);
  }

  public BookedOutException(String message, Throwable cause) {
    super(message, cause);
  }

  public BookedOutException(Throwable cause) {
    super(cause);
  }

  public BookedOutException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
