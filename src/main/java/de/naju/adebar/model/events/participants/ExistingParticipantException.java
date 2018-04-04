package de.naju.adebar.model.events.participants;

/**
 * Exception to indicate that a person already participates in an event
 *
 * @author Rico Bergmann
 */
public class ExistingParticipantException extends RuntimeException {

  private static final long serialVersionUID = 1370585756956834005L;

  public ExistingParticipantException() {}

  public ExistingParticipantException(String message) {
    super(message);
  }

  public ExistingParticipantException(String message, Throwable cause) {
    super(message, cause);
  }

  public ExistingParticipantException(Throwable cause) {
    super(cause);
  }

  public ExistingParticipantException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
