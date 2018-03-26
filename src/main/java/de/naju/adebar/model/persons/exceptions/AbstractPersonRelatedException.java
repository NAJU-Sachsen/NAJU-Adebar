package de.naju.adebar.model.persons.exceptions;

import de.naju.adebar.model.persons.PersonId;

/**
 * Common base class for all exceptions about persons. Takes track of the person's ID and name
 *
 * @see de.naju.adebar.model.persons.Person
 */
public abstract class AbstractPersonRelatedException extends RuntimeException {

  private static final long serialVersionUID = -4638575221246619942L;

  private final PersonId id;
  private final String name;

  /**
   * @param id the ID of the person
   * @param name the person's name
   */
  protected AbstractPersonRelatedException(PersonId id, String name) {
    this.id = id;
    this.name = name;
  }

  /**
   * @param message the detail message
   * @param id the ID of the person
   * @param name the person's name
   */
  protected AbstractPersonRelatedException(String message, PersonId id, String name) {
    super(message);
    this.id = id;
    this.name = name;
  }

  /**
   * @param message the detail message
   * @param cause the cause
   * @param id the ID of the person
   * @param name the person's name
   */
  protected AbstractPersonRelatedException(String message, Throwable cause, PersonId id,
      String name) {
    super(message, cause);
    this.id = id;
    this.name = name;
  }

  /**
   * @param cause the cause
   * @param id the ID of the person
   * @param name the person's name
   */
  protected AbstractPersonRelatedException(Throwable cause, PersonId id, String name) {
    super(cause);
    this.id = id;
    this.name = name;
  }

  /**
   * @param message the detail message
   * @param cause the cause
   * @param enableSuppression whether or not suppression is enabled or disabled
   * @param writableStackTrace whether or not the stack trace should be writable
   * @param id the ID of the person
   * @param name the person's name
   */
  protected AbstractPersonRelatedException(String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace, PersonId id, String name) {
    super(message, cause, enableSuppression, writableStackTrace);
    this.id = id;
    this.name = name;
  }

  /**
   * @return the ID of the person that caused the exception
   */
  public PersonId getId() {
    return id;
  }

  /**
   * @return the name of the person that caused the exception. Just to display a prettier error
   *         message
   */
  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return super.toString() + " [personId: " + id + ", personName: " + name + "]";
  }
}
