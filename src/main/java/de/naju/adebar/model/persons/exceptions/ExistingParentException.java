package de.naju.adebar.model.persons.exceptions;

/**
 * Exception to indicate that a parent is already connected to a person
 *
 * @author Rico Bergmann
 */
public class ExistingParentException extends RuntimeException {

  private static final long serialVersionUID = 799521946635294458L;

  public ExistingParentException() {
  }

  public ExistingParentException(String message) {
    super(message);
  }

  public ExistingParentException(String message, Throwable cause) {
    super(message, cause);
  }

  public ExistingParentException(Throwable cause) {
    super(cause);
  }

  public ExistingParentException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
