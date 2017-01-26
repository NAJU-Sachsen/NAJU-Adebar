package de.naju.adebar.model.human;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Repository to access {@link Activist} instances
 * @author Rico Bergmann
 */
@Repository
public interface ActivistRepository extends CrudRepository<Activist, PersonId> {

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
}
