package de.naju.adebar.model.persons;

import com.querydsl.core.types.Predicate;
import de.naju.adebar.infrastructure.ReadOnlyRepository;
import de.naju.adebar.model.core.Email;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository to provide read-only access to {@link Person} instances
 *
 * @author Rico Bergmann
 */
@Repository("ro_personRepo")
public interface ReadOnlyPersonRepository extends //
    ReadOnlyRepository<Person, PersonId>, //
    QuerydslPredicateExecutor<Person>, //
    PagingAndSortingRepository<Person, PersonId> {

  /**
   * @return all non-archived persons
   */
  @Override
  @Query("SELECT p FROM person p WHERE p.archived=0")
  Iterable<Person> findAll();

  /**
   * @return all non-archived persons
   */
  @Query("SELECT p FROM person p WHERE p.archived=0 ORDER BY p.lastName")
  Iterable<Person> findAllOrderByLastName();

  /**
   * @return all activists
   */
  @Query("SELECT p FROM person p WHERE p.id IN (SELECT personId FROM activist)")
  Iterable<Person> findAllActivists();

  /**
   * @param pageable the pageable to put the result into
   * @return all non-archived persons
   */
  @Query("SELECT p FROM person p WHERE p.archived=0 ORDER BY p.lastName")
  Page<Person> findAllPagedOrderByLastName(Pageable pageable);

  /**
   * @param id must not be {@code null}.
   * @return the person with that ID. This person is not archived
   */
  @Query("SELECT p FROM person p WHERE p.id=?1 AND p.archived=0")
  Person findOne(PersonId id);

  /**
   * @param id the must not be {code null}
   * @return the person with that ID. The person may not be archived.
   */
  @Override
  @Query("SELECT p FROM person p WHERE  p.id=?1 AND p.archived=0")
  Optional<Person> findById(PersonId id);

  /**
   * @return the first 25 non-archived persons, ordered by their last name
   */
  @Query(nativeQuery = true,
      value = "SELECT p.* FROM person p WHERE p.archived=0 ORDER BY p.last_name LIMIT 25")
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

  /**
   * @return all non-archived persons
   */
  @Query("SELECT p FROM person p WHERE p.archived=0")
  Stream<Person> streamAll();

  /**
   * @param predicate the predicate
   * @return all persons which matched the predicate
   */
  @Override
  List<Person> findAll(Predicate predicate);

  /**
   * @param firstName the first name
   * @param lastName the last name
   * @return all persons with the given name
   */
  Iterable<Person> findByFirstNameAndLastName(String firstName, String lastName);

  /**
   * @param email the email
   * @return all persons with the given email address
   */
  Iterable<Person> findByEmail(Email email);

  /**
   * @param firstName the first name
   * @param lastName the last name
   * @param email the email address
   * @return the matching person
   */
  Person findByFirstNameAndLastNameAndEmail(String firstName, String lastName, Email email);

  @Override
  boolean existsById(PersonId id);
}
