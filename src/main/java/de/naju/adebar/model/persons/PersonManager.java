package de.naju.adebar.model.persons;

import de.naju.adebar.model.core.Email;
import de.naju.adebar.model.persons.exceptions.NoReferentException;
import de.naju.adebar.model.persons.qualifications.Qualification;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 * Service to take care of {@link Person Persons}
 *
 * @author Rico Bergmann
 * @see Person
 */
@Service
public interface PersonManager {

  /**
   * Saves a given person. It may or may not be saved already. If it has no ID specified, one will
   * automatically be generated
   *
   * @param person the person to save
   * @return the saved person. As its internal state may differ after the save, this instance should
   *     be used for future operations
   */
  Person savePerson(Person person);

  /**
   * Creates a new person
   *
   * @param firstName the person's first name
   * @param lastName the person's last name
   * @param email the person's email
   * @return the freshly created person instance
   */
  Person createPerson(String firstName, String lastName, Email email);

  /**
   * Creates a new person
   *
   * @param firstName the person's first name
   * @param lastName the person's last name
   * @param email the person's email
   * @param participant whether the person is a camp participant
   * @param activist whether the person is an activist
   * @param referent whether the person it a referent
   * @return the freshly created person instance
   */
  Person createPerson(String firstName, String lastName, Email email, boolean participant,
      boolean activist, boolean referent);

  /**
   * Changes the state of a saved person
   *
   * @param personId the person to update
   * @param newPerson the new person data
   * @return the updated (and saved) person
   */

  Person updatePerson(Person person);

  /**
   * Queries for a specific person
   *
   * @param id the person's id
   * @return an optional containing the person if it exists, otherwise the optional is empty
   */
  Optional<Person> findPerson(String id);

  /**
   * Disables a person. It may/should not be available as a potential camp participant, etc. any
   * more afterwards. To keep statistics correct, persons should not be deleted but instead only
   * disabled (and anonymized)
   *
   * @param person the person to disable
   * @throws IllegalStateException if the person may not be deactivated (e.g. because it is an
   *     activist or referent)
   */
  void deactivatePerson(Person person);

  /**
   * Adds a qualification to a referent
   *
   * @param person the person to add the qualifcation to
   * @param qualification the qualification to add
   * @throws NoReferentException if the person is no referent
   */
  void addQualificationToPerson(Person person, Qualification qualification);

  /**
   * Provides access to the underlying data
   *
   * @return a read only repository instance
   */
  ReadOnlyPersonRepository repository();
}
