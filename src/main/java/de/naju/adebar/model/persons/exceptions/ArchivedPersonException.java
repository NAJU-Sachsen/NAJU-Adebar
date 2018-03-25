package de.naju.adebar.model.persons.exceptions;

import de.naju.adebar.model.persons.Person;
import de.naju.adebar.model.persons.PersonId;

/**
 * Exception to indicate that some operation failed because the person was archived.
 *
 * @author Rico Bergmann
 */
public class ArchivedPersonException extends RuntimeException {

  private static final long serialVersionUID = 4435198878340304372L;

  /*
   * We do not reference the person itself here as it's not serializable
   */

  private final PersonId id;
  private final String name;

  /**
   * Creates a new exception
   *
   * @param person the person that caused the exception
   */
  public ArchivedPersonException(Person person) {
    super();
    this.id = person.getId();
    this.name = person.getName();
  }

  /**
   * Creates a new exception
   *
   * @param person the person that caused the exception
   * @param message more details about the exception
   */
  public ArchivedPersonException(Person person, String message) {
    super(message);
    this.id = person.getId();
    this.name = person.getName();
  }

  /**
   * @return the person's id
   */
  public PersonId getId() {
    return id;
  }

  /**
   * @return the person's name
   */
  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return super.toString() + " [personId: " + id + ", personName: " + name + "]";
  }

}
