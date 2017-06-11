package de.naju.adebar.model.human;

import de.naju.adebar.infrastructure.ReadOnlyRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

/**
 * Repository to provide read-only access to {@link Person} instances
 * @author Rico Bergmann
 */
@Repository("ro_personRepo")
public interface ReadOnlyPersonRepository extends ReadOnlyRepository<Person, PersonId> {

    /**
     * @return all non-archived persons
     */
    @Query("SELECT p FROM person p WHERE p.archived=0")
    Iterable<Person> findAll();

    /**
     * @return all activists
     */
    @Query("SELECT p FROM person p WHERE p.id IN (SELECT personId FROM activist)")
    Iterable<Person> findAllActivists();

    /**
     * @param id must not be {@code null}.
     * @return the person with that ID. This person is not archived
     */
    @Query("SELECT p FROM person p WHERE p.id=?1 AND p.archived=0")
    Person findOne(PersonId id);

    /**
     * @return the first 25 non-archived persons, ordered by their last name
     */
    @Query(nativeQuery = true, value = "SELECT TOP 25 p.* FROM person p WHERE p.archived=0 ORDER BY p.last_name")
    Iterable<Person> findFirst25();

    /**
     * @return all persons
     */
    @Query("SELECT p FROM person p")
    Iterable<Person> allEntries();

    /**
     * @param id must not be {@code null}.
     * @return the person with that ID
     */
    @Query("SELECT p FROM person p WHERE p.id=?1")
    Person findEntry(PersonId id);

    @Query("SELECT p FROM person p WHERE p.archived=0")
    Stream<Person> streamAll();
}
