package de.naju.adebar.model.human;

import de.naju.adebar.infrastructure.ReadOnlyRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.io.Serializable;
import java.util.stream.Stream;

/**
 * A repository that provides read only access to the saved persons
 * @author Rico Bergmann
 */
public interface ReadOnlyPersonRepository extends ReadOnlyRepository<Person, PersonId> {

    /**
     * @return all non-disabled persons. Should be used for nearly all iterations
     */
    Iterable<Person> findAllByActiveIsTrue();

    /**
     * Equivalent of {@link CrudRepository#findOne(Serializable)}, just for non-disabled persons
     * @param personId the person's id
     * @return the person associated to the ID or {@code null} if there is no such person
     */
    Person findOneByIdAndActiveIsTrue(PersonId personId);

    /**
     * @return the first 25 persons
     */
    Iterable<Person> findFirst25ByOrderByLastName();

    /**
     * @return the first 25 non-disabled persons
     */
    Iterable<Person> findFirst25ByActiveIsTrueOrderByLastName();

    /**
     * @return all persisted persons as a stream. Nice for accessing them in a functional way
     * @see Stream
     * @see <a href="https://en.wikipedia.org/wiki/Functional_programming">Functional programming</a>
     */
    @Query("select p from Person p")
    Stream<Person> streamAll();

    /**
     * Equivalent of normal {@link #streamAll()}, just for non-disabled persons
     * @return all persisted, non-disabled persons as a stream. Nice for accessing them in a functional way
     */
    @Query("select p from Person p where p.active=true")
    Stream<Person> streamAllByActiveIsTrue();
}
