package de.naju.adebar.app.filter;

/**
 * Exception to indicate that the specified filter criteria contradict each other, e.g. if only
 * activists which are no activists should be considered
 * 
 * @author Rico Bergmann
 */
public class ConflictingFilterCriteriaException extends RuntimeException {
  private static final long serialVersionUID = -8483028886172543998L;

  public ConflictingFilterCriteriaException() {}

  public ConflictingFilterCriteriaException(String message) {
    super(message);
  }

  public ConflictingFilterCriteriaException(String message, Throwable cause) {
    super(message, cause);
  }

  public ConflictingFilterCriteriaException(Throwable cause) {
    super(cause);
  }

  public ConflictingFilterCriteriaException(String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
