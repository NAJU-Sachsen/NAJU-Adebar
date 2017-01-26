package de.naju.adebar.model.human;

import java.time.LocalDate;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository to access {@link Person} instances
 * @author Rico Bergmann
 * @see Person
 */
@Repository
public interface PersonRepository extends CrudRepository<Person, PersonId> {

    /**
     * @param email the email address to query for
     * @return the persons with that email address. As one email address may belong to several persons (e.g. for
     * families) an {@link Iterable} is returned
     */
	Iterable<Person> findByEmail(String email);

    /**
     * @param firstName the first name to query for
     * @param lastName the last name to query for
     * @return all persons with the given name
     */
	Iterable<Person> findByFirstNameAndLastName(String firstName, String lastName);

    /**
     * @param gender the gender to query for
     * @return all persons with the given gender
     */
	Iterable<Person> findByGender(Gender gender);

    /**
     * @param date the date of birth to query for
     * @return all persons with that birthday
     */
    Iterable<Person> findByDateOfBirth(LocalDate date);

    /**
     * @param date the date of birth to query for
     * @return all persons that are younger than the given date
     */
	Iterable<Person> findByDateOfBirthIsAfter(LocalDate date);

    /**
     * @param date the date to query for
     * @return all persons that are older than the given date
     */
	Iterable<Person> findByDateOfBirthIsBefore(LocalDate date);

    /**
     * @param address the address to query for
     * @return all persons with the given address
     */
	Iterable<Person> findByAddress(Address address);

    /**
     * @param city the city to query for
     * @return all persons which live in that city
     */
	Iterable<Person> findByAddressCity(String city);

    /**
     * @param zip the zip to query for
     * @return all persons which live in that area
     */
	Iterable<Person> findByAddressZip(String zip);

    /**
     * @return all persisted persons as a stream. Nice for accessing them in a functional way
     * @see Stream
     * @see <a href="https://en.wikipedia.org/wiki/Functional_programming">Functional programming</a>
     */
	@Query("select p from Person p")
	Stream<Person> streamAll();
}
