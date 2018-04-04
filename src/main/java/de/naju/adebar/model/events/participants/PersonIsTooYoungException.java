package de.naju.adebar.model.events.participants;

/**
 * Exception to indicate that a person is too young to participate in a camp
 *
 * @author Rico Bergmann
 */
public class PersonIsTooYoungException extends RuntimeException {

  private static final long serialVersionUID = 130980750559664858L;

  public PersonIsTooYoungException() {}

  public PersonIsTooYoungException(String message) {
    super(message);
  }

  public PersonIsTooYoungException(String message, Throwable cause) {
    super(message, cause);
  }

  public PersonIsTooYoungException(Throwable cause) {
    super(cause);
  }

  public PersonIsTooYoungException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
