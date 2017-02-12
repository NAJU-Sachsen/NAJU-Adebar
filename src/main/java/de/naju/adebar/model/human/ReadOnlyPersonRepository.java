package de.naju.adebar.model.human;

import de.naju.adebar.infrastructure.ReadOnlyRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.stream.Stream;

/**
 * A repository that provides read only access to the saved persons
 * @author Rico Bergmann
 */
public interface ReadOnlyPersonRepository extends ReadOnlyRepository<Person, PersonId> {

    /**
     * @return the first 25 persons
     */
    Iterable<Person> findFirst25ByOrderByLastName();

    /**
     * @return all persisted persons as a stream. Nice for accessing them in a functional way
     * @see Stream
     * @see <a href="https://en.wikipedia.org/wiki/Functional_programming">Functional programming</a>
     */
    @Query("select p from Person p")
    Stream<Person> streamAll();
}
