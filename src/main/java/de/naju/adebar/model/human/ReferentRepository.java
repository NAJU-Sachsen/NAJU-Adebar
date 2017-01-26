package de.naju.adebar.model.human;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

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
}