package de.naju.adebar.model.persons.exceptions;

/**
 * Exception to indicate that a person is no camp participant
 * 
 * @author Rico Bergmann
 */
public class NoParticipantException extends RuntimeException {
  private static final long serialVersionUID = 3777085513175599784L;

  public NoParticipantException() {}

  public NoParticipantException(String message) {
    super(message);
  }

  public NoParticipantException(String message, Throwable cause) {
    super(message, cause);
  }

  public NoParticipantException(Throwable cause) {
    super(cause);
  }

  public NoParticipantException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
