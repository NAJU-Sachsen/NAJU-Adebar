package de.naju.adebar.model.human;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Repository to access {@link Activist} instances
 * @author Rico Bergmann
 */
@Repository
public interface ActivistRepository extends CrudRepository<Activist, PersonId> {

    /**
     * @return all active activists
     */
    Iterable<Activist> findAllByActiveIsTrue();

    /**
     * @param date the JuLeiCa expiry date to query for
     * @return all activists with the given JuLeiCa expiry date
     */
    Iterable<Activist> findByJuleicaExpiryDate(LocalDate date);

    /**
     * @param date the JuLeiCa expiry date to query for
     * @return all activists with an JuLeiCa expiry date before the given one
     */
    Iterable<Activist> findByJuleicaExpiryDateIsBefore(LocalDate date);

    /**
     * @param date the JuLeiCa expiry date to query for
     * @return all activists with an JuLeiCa expiry date after the given one
     */
    Iterable<Activist> findByJuleicaExpiryDateIsAfter(LocalDate date);

    /**
     * @return all activists with a JuLeiCa expiry date
     */
    Iterable<Activist> findByJuleicaExpiryDateIsNotNull();

    /**
     * @return all persisted activists as a stream. Nice for accessing them in a functional way
     * @see Stream
     * @see <a href="https://en.wikipedia.org/wiki/Functional_programming">Functional programming</a>
     */
    @Query("select a from Activist a")
    Stream<Activist> streamAll();
}
