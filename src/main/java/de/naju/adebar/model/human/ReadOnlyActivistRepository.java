package de.naju.adebar.model.human;

import de.naju.adebar.infrastructure.ReadOnlyRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.stream.Stream;

/**
 * A repository that provides read-only access to the saved activists
 * @author Rico Bergmann
 */
public interface ReadOnlyActivistRepository extends ReadOnlyRepository<Activist, PersonId> {

    /**
     * @return all persisted activists as a stream. Nice for accessing them in a functional manner.
     * @see Stream
     * @see <a href="https://en.wikipedia.org/wiki/Functional_programming">Functional programming</a>
     */
    @Query("select a from Activist a")
    Stream<Activist> streamAll();
}
