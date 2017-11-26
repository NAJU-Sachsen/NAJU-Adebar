package de.naju.adebar.model.chapter;

/**
 * Exception to indicate that an activist is already member of a local group
 * 
 * @author Rico Bergmann
 */
public class ExistingMemberException extends RuntimeException {
  private static final long serialVersionUID = 6311553628832999669L;

  public ExistingMemberException() {}

  public ExistingMemberException(String message) {
    super(message);
  }

  public ExistingMemberException(String message, Throwable cause) {
    super(message, cause);
  }

  public ExistingMemberException(Throwable cause) {
    super(cause);
  }

  public ExistingMemberException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
