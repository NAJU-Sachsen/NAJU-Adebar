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
     * Saves a given person. It may or may not be saved already - however it must have an ID specified
     * @param person the person to save
     * @return the saved person. As its internal state may differ after the save, this instance should be used
     * for future operations
     * @throws IllegalStateException if the person has no ID specified
     */
    Person savePerson(Person person);

    /**
     * Persists a given person. In opposition to {@link #savePerson(Person)} the person may not be part of the database
     * already. (And therefore has no ID specified. Thus {@code person.hasId()} must return {@code false})
     * @param person the person to save
     * @return the saved person. As its internal state may differ after the save, this instance should be used
     * for future operations
     * @throws IllegalStateException if the person already has an ID specified
     */
    Person persistPerson(Person person);

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
}
