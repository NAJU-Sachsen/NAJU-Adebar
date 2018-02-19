package de.naju.adebar.services.conversion;

/**
 * Exception to indicate that a form could not be analyzed correctly.
 *
 * @author Rico Bergmann
 */
public class FormInspectionError extends RuntimeException {

  private static final long serialVersionUID = -1513007234150915263L;

  /**
   * Constructs a new FormInspectionError with a detailed message and cause
   * 
   * @param message the message
   * @param cause the cause
   */
  public FormInspectionError(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Constructs a new FormInspectionError with a detailed message
   * 
   * @param message the message
   */
  public FormInspectionError(String message) {
    super(message);
  }

  /**
   * Constructs a new FormInspectionError with its cause
   * 
   * @param cause the cause
   */
  public FormInspectionError(Throwable cause) {
    super(cause);
  }

}
