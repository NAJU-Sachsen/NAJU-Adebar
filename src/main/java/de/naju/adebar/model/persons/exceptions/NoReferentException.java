package de.naju.adebar.model.persons.exceptions;

import de.naju.adebar.model.persons.Person;
import de.naju.adebar.model.persons.PersonId;
import de.naju.adebar.model.persons.ReferentProfile;

/**
 * Exception to indicate that a person is not a referent but was treated as one.
 * 
 * @author Rico Bergmann
 * @see Person
 * @see ReferentProfile
 * @see PersonId
 */
public class NoReferentException extends RuntimeException {
  private static final long serialVersionUID = 3205712344614424639L;

  public NoReferentException() {}

  public NoReferentException(String message) {
    super(message);
  }

  public NoReferentException(String message, Throwable cause) {
    super(message, cause);
  }

  public NoReferentException(Throwable cause) {
    super(cause);
  }

  public NoReferentException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
