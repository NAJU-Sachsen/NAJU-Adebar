package de.naju.adebar.model.human;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Service to take care of {@link Person Persons}
 * @author Rico Bergmann
 * @see Person
 */
@Service
public interface PersonManager {

    /**
     * Saves a given person. It may or may not be saved already. If it has no ID specified, one will automatically
     * be generated
     * @param person the person to save
     * @return the saved person. As its internal state may differ after the save, this instance should be used
     * for future operations
     */
    Person savePerson(Person person);

    /**
     * Creates a new person
     * @param firstName the person's first name
     * @param lastName the person's last name
     * @param email the person's email
     * @param gender the person's gender
     * @param address the person's address
     * @param dob the person's date of birth
     * @return the freshly created person instance
     */
    Person createPerson(String firstName, String lastName, String email, Gender gender, Address address, LocalDate dob);

    /**
     * Changes the state of a saved person
     * @param personId the person to update
     * @param newPerson the new person data
     * @return the updated (and saved) person
     */
    Person updatePerson(PersonId personId, Person newPerson);

    /**
     * Provides access to the underlying data
     * @return a read only repository instance
     */
    ReadOnlyPersonRepository repository();
}
