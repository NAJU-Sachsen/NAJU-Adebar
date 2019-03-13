package de.naju.adebar.model.persons.exceptions;

import de.naju.adebar.model.persons.Person;
import de.naju.adebar.model.persons.PersonId;

/**
 * Exception thrown if an under-aged participant did not specify his or her gender.
 *
 * <p>
 * Each participant that is of minor-age needs to have information about its gender due to legal
 * reasons.
 */
public class GenderIsRequiredForMinorsException extends AbstractPersonRelatedException {

  private static final long serialVersionUID = 7276367870257718312L;

  /**
   * @param personId the ID of the person that caused the exception
   */
  public GenderIsRequiredForMinorsException(PersonId personId) {
    super(personId, "");
  }

  public GenderIsRequiredForMinorsException(Person person) {
    super(person.getId(), person.getName());
  }

}
