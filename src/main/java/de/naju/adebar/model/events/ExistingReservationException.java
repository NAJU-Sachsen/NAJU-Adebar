package de.naju.adebar.model.events;

/**
 * Exception to indicate that there already is an reservation with that description
 * 
 * @author Rico Bergmann
 */
public class ExistingReservationException extends RuntimeException {
  private static final long serialVersionUID = -4276021814884682189L;

  public ExistingReservationException() {
    super();
  }

  public ExistingReservationException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public ExistingReservationException(String message, Throwable cause) {
    super(message, cause);
  }

  public ExistingReservationException(String message) {
    super(message);
  }

  public ExistingReservationException(Throwable cause) {
    super(cause);
  }
}
