package de.naju.adebar.model.chapter;

/**
 * Exception to indicate that an activist does already contribute to a project.
 * 
 * @author Rico Bergmann
 */
public class ExistingContributorException extends RuntimeException {
  private static final long serialVersionUID = -2196435583324697128L;

  public ExistingContributorException() {}

  public ExistingContributorException(String message) {
    super(message);
  }

  public ExistingContributorException(String message, Throwable cause) {
    super(message, cause);
  }

  public ExistingContributorException(Throwable cause) {
    super(cause);
  }

  public ExistingContributorException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
