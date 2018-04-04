package de.naju.adebar.model.events.participants;

/**
 * Exception to indicate that an event does not have the required amount of available slots
 *
 * @author Rico Bergmann
 */
public class TooFewEmptySlotsException extends RuntimeException {

  private static final long serialVersionUID = -4668766134559857859L;

  public TooFewEmptySlotsException() {
    super();
  }

  public TooFewEmptySlotsException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public TooFewEmptySlotsException(String message, Throwable cause) {
    super(message, cause);
  }

  public TooFewEmptySlotsException(String message) {
    super(message);
  }

  public TooFewEmptySlotsException(Throwable cause) {
    super(cause);
  }

}
