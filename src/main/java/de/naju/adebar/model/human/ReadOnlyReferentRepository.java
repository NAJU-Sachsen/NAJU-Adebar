package de.naju.adebar.model.human;

import de.naju.adebar.infrastructure.ReadOnlyRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.stream.Stream;

/**
 * Repository that provides read only access to the saved referents.
 * @author Rico Bergmann
 */
public interface ReadOnlyReferentRepository extends ReadOnlyRepository<Referent, PersonId> {

    /**
     * @return all persisted referents as a stream. Nice for accessing them in a functional manner.
     * @see Stream
     * @see <a href="https://en.wikipedia.org/wiki/Functional_programming">Functional programming</a>
     */
    @Query("select r from Referent r")
    Stream<Referent> streamAll();
}
