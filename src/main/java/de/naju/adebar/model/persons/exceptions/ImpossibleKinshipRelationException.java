package de.naju.adebar.model.persons.exceptions;

/**
 * Exception to indicate that a kinship relation is not possible, e.g. when a person should be
 * registered as its own parent
 * 
 * @author Rico Bergmann
 */
public class ImpossibleKinshipRelationException extends RuntimeException {
  private static final long serialVersionUID = -7711237163546733974L;

  public ImpossibleKinshipRelationException() {
    super();
  }

  public ImpossibleKinshipRelationException(String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public ImpossibleKinshipRelationException(String message, Throwable cause) {
    super(message, cause);
  }

  public ImpossibleKinshipRelationException(String message) {
    super(message);
  }

  public ImpossibleKinshipRelationException(Throwable cause) {
    super(cause);
  }

}
