package de.naju.adebar.app;

/**
 * Exception thrown to indicate that the ID of some entity could not be updated.
 * 
 * @author Rico Bergmann
 *
 */
public class IdUpdateFailedException extends RuntimeException {

  private static final long serialVersionUID = 8975430809599424937L;

  public IdUpdateFailedException() {
    super();
  }

  public IdUpdateFailedException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public IdUpdateFailedException(String message, Throwable cause) {
    super(message, cause);
  }

  public IdUpdateFailedException(String message) {
    super(message);
  }

  public IdUpdateFailedException(Throwable cause) {
    super(cause);
  }

}
