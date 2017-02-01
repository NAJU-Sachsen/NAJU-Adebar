package de.naju.adebar.model.human;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Repository to access {@link Referent} instances
 * @author Rico Bergmann
 */
@Repository
public interface ReferentRepository extends CrudRepository<Referent, PersonId> {

    /**
     * @param qualification the qualification to query for
     * @return all referents with the given qualification
     */
    Iterable<Person> findByQualifications(Qualification qualification);

    /**
     * @param qualifications the qualifications to query for
     * @return all referents with the given qualifications
     */
    Iterable<Person> findByQualificationsContains(Iterable<Qualification> qualifications);

    /**
     * @return all persisted referents as a stream. Nice for accessing them in a functional way
     * @see Stream
     * @see <a href="https://en.wikipedia.org/wiki/Functional_programming">Functional programming</a>
     */
    @Query("select r from Referent r")
    Stream<Referent> streamAll();
}
